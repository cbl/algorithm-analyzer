package com.github.cbl.algorithm_analyzer.trees.AvlTree;

import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.util.Recursive;
import com.github.cbl.algorithm_analyzer.util.TreePrinter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Generic AVL tree implementation
 *
 * @param <T> type of a nodes value
 */
public class AVLTree<T> {

    /****************************
     *          Events          *
     ****************************/

    /** Event anouncing an insertion */
    public record InsertionEvent<T>(T value) implements Event {
        @Override
        public String toString() {
            return "Inserting " + value;
        }
    }
    ;

    /** Event anouncing a removal */
    public record RemovalEvent<T>(T value) implements Event {
        @Override
        public String toString() {
            return "Removing " + value;
        }
    }
    ;

    /** Event transmitting a new tree state */
    public record StateEvent<T>(List<T> values) implements Event {
        @Override
        public String toString() {
            return TreePrinter.printTree(values());
        }
    }
    ;

    /** Event notifying that a left rotation occured */
    public record RotateLeftEvent<T>(T value) implements Event {
        @Override
        public String toString() {
            return "Rotate left around :" + value();
        }
    }
    ;

    /** Event notifying that a right rotation occured */
    public record RotateRightEvent<T>(T value) implements Event {
        @Override
        public String toString() {
            return "Rotate right around :" + value();
        }
    }
    ;

    /****************************
     *  AVL tree implementation *
     ****************************/

    private final Comparator<T> comparator;

    private Node<T> root = new Empty();
    private Consumer<Event> onEvent = e -> {};

    /**
     * Create a new AVLTree for values of type <T>
     *
     * @param comparator used to compare node values of type <T>
     */
    public AVLTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /** @return an AVLTree<Integer> using Comparator.naturalOrder() */
    public static AVLTree<Integer> newIntTree() {
        return new AVLTree<Integer>(Comparator.naturalOrder());
    }

    /** @param c the EventListener to use with this tree */
    public void onEvent(Consumer<Event> c) {
        this.onEvent = c;
    }

    /** Insert a value into this tree */
    public AVLTree<T> insert(T value) {
        onEvent.accept(new InsertionEvent<T>(value));
        root = root.insert(value);
        onEvent.accept(new StateEvent<T>(values()));
        return this;
    }

    /** Removes a value from this tree */
    public AVLTree<T> remove(T value) {
        onEvent.accept(new RemovalEvent<T>(value));
        root = root.remove(value);
        onEvent.accept(new StateEvent<T>(values()));
        return this;
    }

    /** Finds a node in this tree */
    public Optional<? extends Node<T>> find(T value) {
        return root.find(value);
    }

    /** Finds the depth of a node by value */
    public Optional<Integer> depth(T value) {
        return root.depth(value);
    }

    /** Returns the trees height */
    public int height() {
        return root.height();
    }

    /**
     * Collects values in top-to-bottom, left-ro-right order
     *
     * <p>Empty chhild-trees are serialized to {@code null}
     *
     * @return list of size 2^height - 1 of tree values
     */
    @SuppressWarnings("unchecked")
    public List<T> values() {
        Object[] values = new Object[(int) Math.pow(2, root.height()) - 1];

        Recursive<BiConsumer<Node<T>, Integer>> collect = new Recursive<>();
        collect.func =
                (node, index) -> {
                    if (index <= values.length) {
                        values[index - 1] = node.value();
                        collect.func.accept(node.left(), index * 2);
                        collect.func.accept(node.right(), index * 2 + 1);
                    }
                };

        collect.func.accept(root, 1);

        return (List<T>) Arrays.asList(values);
    }

    /**
     * Collects nodes (value + balance) in top-to-bottom, left-ro-right order
     *
     * @return list of size 2^height - 1 of tree values
     */
    @SuppressWarnings("unchecked")
    public List<BalancedNode<T>> nodesWithBalances() {
        BalancedNode<T>[] values = new BalancedNode[(int) Math.pow(2, root.height()) - 1];

        Recursive<BiConsumer<Node<T>, Integer>> collect = new Recursive<>();
        collect.func =
                (node, index) -> {
                    if (index <= values.length) {
                        values[index - 1] = new BalancedNode<T>(node.value(), node.balance());
                        collect.func.accept(node.left(), index * 2);
                        collect.func.accept(node.right(), index * 2 + 1);
                    }
                };

        collect.func.accept(root, 1);

        return (List<BalancedNode<T>>) Arrays.asList(values);
    }

    public record BalancedNode<T>(T value, int balance) {
        @Override
        public String toString() {
            return value.toString() + ":" + balance;
        }
    }
    ;

    /****************************
     *       Node Interface     *
     ****************************/

    private static interface Node<T> {
        T value();

        Node<T> left();

        Node<T> right();

        boolean empty();

        Node<T> insert(T t);

        Node<T> remove(T t);

        Optional<? extends Node<T>> find(T t);

        Optional<Integer> depth(T t);

        default int height() {
            return 1 + Math.max(left().height(), right().height());
        }

        Node<T> rebalance();

        Node<T> rotateRight();

        Node<T> rotateLeft();

        default int balance() {
            return right().height() - left().height();
        }

        default T min() {
            T m = left().min();
            return null != m ? m : value();
        }

        default T successor() {
            return right().min();
        }
    }

    /****************************
     *     Node implementation  *
     ****************************/

    private class TreeNode implements Node<T> {
        private final T value;
        private final Node<T> left;
        private final Node<T> right;

        public TreeNode(T value, Node<T> left, Node<T> right) {
            assert (value != null);
            this.value = value;
            this.left = left;
            this.right = right;
        }

        @Override
        public T value() {
            return value;
        }

        @Override
        public Node<T> right() {
            return right;
        }

        @Override
        public Node<T> left() {
            return left;
        }

        @Override
        public boolean empty() {
            return false;
        }

        @Override
        public TreeNode insert(T value) {
            int cmp = AVLTree.this.comparator.compare(value, this.value);
            if (cmp < 0) {
                return new TreeNode(this.value, left().insert(value), right()).rebalance();
            } else if (cmp > 0) {
                return new TreeNode(this.value, left(), right().insert(value)).rebalance();
            } else {
                return this;
            }
        }

        @Override
        public Node<T> remove(T value) {
            int cmp = AVLTree.this.comparator.compare(value, this.value);
            if (cmp < 0) {
                return new TreeNode(this.value, left().remove(value), right()).rebalance();
            } else if (cmp > 0) {
                return new TreeNode(this.value, left(), right().remove(value)).rebalance();
            } else {
                if (!left().empty() && !right().empty()) {
                    T succ = this.successor();
                    return new TreeNode(succ, left(), right().remove(succ)).rebalance();
                } else if (right().empty()) {
                    return left();
                } else {
                    return right();
                }
            }
        }

        @Override
        public Optional<? extends Node<T>> find(T value) {
            int cmp = AVLTree.this.comparator.compare(value, this.value);
            if (cmp < 0) {
                return left().find(value);
            } else if (cmp > 0) {
                return right().find(value);
            } else {
                return Optional.of(this);
            }
        }

        @Override
        public Optional<Integer> depth(T value) {
            int cmp = AVLTree.this.comparator.compare(value, this.value);
            if (cmp < 0) {
                return left().depth(value).map(d -> d + 1);
            } else if (cmp > 0) {
                return right().depth(value).map(d -> d + 1);
            } else {
                return Optional.of(1);
            }
        }

        @Override
        public TreeNode rebalance() {
            switch (balance()) {
                case -2:
                    if (left() != null && left().balance() == 1) {
                        return new TreeNode(value, left().rotateLeft(), right()).rotateRight();
                    } else {
                        return this.rotateRight();
                    }
                case 2:
                    if (right() != null && right().balance() == -1) {
                        return new TreeNode(value, left(), right().rotateRight()).rotateLeft();
                    } else {
                        return this.rotateLeft();
                    }

                default:
                    return this;
            }
        }

        @Override
        public TreeNode rotateRight() {
            AVLTree.this.onEvent.accept(new AVLTree.RotateRightEvent<T>(value()));
            return new TreeNode(
                    left().value(), left().left(), new TreeNode(value, left().right(), right()));
        }

        @Override
        public TreeNode rotateLeft() {
            AVLTree.this.onEvent.accept(new AVLTree.RotateLeftEvent<T>(value()));
            return new TreeNode(
                    right().value(), new TreeNode(value, left(), right().left()), right().right());
        }
    }

    /*****************************
     * Empty node implementation *
     *****************************/

    private class Empty implements Node<T> {
        @Override
        public T value() {
            return null;
        }

        @Override
        public Node<T> left() {
            return new Empty();
        }

        @Override
        public Node<T> right() {
            return new Empty();
        }

        @Override
        public boolean empty() {
            return true;
        }

        @Override
        public int height() {
            return 0;
        }

        @Override
        public TreeNode insert(T value) {
            return new TreeNode(value, new Empty(), new Empty());
        }

        @Override
        public Node<T> remove(T value) {
            return this;
        }

        @Override
        public Optional<Node<T>> find(T value) {
            return Optional.empty();
        }

        @Override
        public Optional<Integer> depth(T value) {
            return Optional.empty();
        }

        @Override
        public T min() {
            return null;
        }

        @Override
        public Node<T> rebalance() {
            assert (false);
            return this;
        }

        @Override
        public Node<T> rotateRight() {
            assert (false);
            return this;
        }

        @Override
        public Node<T> rotateLeft() {
            assert (false);
            return this;
        }
    }
}
