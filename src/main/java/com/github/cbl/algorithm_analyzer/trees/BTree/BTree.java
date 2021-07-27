package com.github.cbl.algorithm_analyzer.trees.BTree;

import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.util.ArrayWriter;
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
public class BTree<T> {

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

    /****************************
     *  AVL tree implementation *
     ****************************/

    private final Comparator<T> comparator;
    private final int m;
    private Block<T> root = new TreeBlock();
    private ArrayWriter w = new ArrayWriter();
    private Consumer<Event> onEvent = e -> {};

    /**
     * Create a new AVLTree for values of type <T>
     *
     * @param comparator used to compare Block values of type <T>
     */
    public BTree(Comparator<T> comparator, int m) {
        this.comparator = comparator;
        this.m = m;
    }

    /*/** @return an AVLTree<Integer> using Comparator.naturalOrder() */
    /*public static BTree<Integer> newIntTree(int m) {
        return new BTree<Integer>(Comparator.naturalOrder(),m,all);
    }*/

    /** @param c the EventListener to use with this tree */
    public void onEvent(Consumer<Event> c) {
        this.onEvent = c;
    }

    /** Insert a value into this tree */
    public BTree<T> insert(T value) {
        onEvent.accept(new InsertionEvent<T>(value));
        Block<T> el = BTree.this.root;
        Returninformation info = BTree.this.searchBlock(BTree.this.root, value);
        el = info.el();
        if(info.succes())System.out.println("Element already exists");
        else{
            BTree.this.insertBlock(info.el(), value, null);
            while(info.el().full() > 2*BTree.this.m){
                DivideBlockInfo divideInfo = BTree.this.divideBlock(info.el());
                Block<T> nextnextEl = el.parent();
                if(nextnextEl.empty()){
                    BTree.this.root = new TreeBlock();
                    BTree.this.root.values()[0] = divideInfo.wert();
                    el = BTree.this.root.children()[0];
                    divideInfo.setEl(BTree.this.root.children()[1]);
                    el = BTree.this.root;
                }
                else{
                    el = nextnextEl;
                    BTree.this.insertBlock(el, divideInfo.wert(), divideInfo.el());
                }
            }
        }
        onEvent.accept(new StateEvent<T>(values()));
        return this;
    }

    /** Removes a value from this tree */
    public BTree<T> remove(T value) {
        onEvent.accept(new RemovalEvent<T>(value));
        Block<T> el = BTree.this.root;
            Returninformation info = BTree.this.searchBlock(BTree.this.root, value);
            el = info.el();
            if(!info.succes())System.out.println("Element does not exist");
            if(!el.children()[info.index()].empty()){
                Block<T> nextEl = searchNext(el,info.index);
            }
            return this;
        onEvent.accept(new StateEvent<T>(values()));
        return this;
    }

    public Returninformation searchBlock(Block<T> el, T value){
        int index = 1;
        Block<T> nextEl = el.children()[el.children().length];
        while(index < el.full() && comparator.compare(el.values()[index], value) <= 0){
            index = index + 1;
        }
        if(comparator.compare(el.values()[index], value) == 0) return new Returninformation(true, index, el);
        else if(comparator.compare(el.values()[index], value) > 0){
            if(el.children()[index].empty())return new Returninformation(false, index, el);
            else nextEl = el.children()[index];
        } 
        else if(index > el.full())nextEl = el.children()[el.children().length];
        return searchBlock(nextEl, value);
    }

    public void insertBlock(Block<T> el, T value, Block<T> tree){
        int i = el.full();
        while(i >= 1 && comparator.compare(el.values()[i], value) > 0){
            el.values()[i] = el.values()[i-1];
            el.children()[i] = el.children()[i-1];
        }
    }

    public DivideBlockInfo divideBlock(Block<T> el){
        Block<T> nextEl = new TreeBlock();
        for(int i = 0; i < this.m; i++){
            nextEl.values()[i] = el.values()[this.m+1+i];
            nextEl.children()[i] = el.children()[this.m+1+i];
        }
        nextEl.children()[this.m+1] = el.children()[2*this.m+2];
        el.setFull(m);
        nextEl.setFull(m);
        return new DivideBlockInfo(el.values()[m+1], nextEl);
    }

    

    /****************************
     *       Block Interface     *
     ****************************/

    private static interface Block<T> {
        T [] values();

        Block<T> [] children();

        int full();

        void setFull(int n);

        boolean empty();

        void setParent(Block<T> p);

        Block<T> parent();

        Optional<? extends Block<T>> find(T t);

        Optional<Integer> depth(T t);
    }


    /**********************
     * Return information *
     **********************/

     private class Returninformation{
         private Boolean success;
         private int index;
         private Block<T> el;

         public Returninformation(Boolean success, int index, Block<T> el){
             this.success = success;
             this.index = index;
             this.el = el;
         }

         public Boolean succes(){
             return this.success;
         }

         public int index(){
             return this.index;
         }

         public Block<T> el(){
             return this.el;
         }

     }

     private class DivideBlockInfo{
         private T wert;
         private Block<T> el;

         public DivideBlockInfo(T wert, Block<T> el){
             this.wert = wert;
             this.el = el;
         }

         public T wert(){
             return this.wert;
         }

         public Block<T> el(){
             return this.el;
         }

         public void setEl(Block<T> t){
             this.el = t;
         }
    }

    private class TreeBlock implements Block<T>{
        private T[] values;
        private Block<T>[] children;
        private int full = 0;
        private Block<T> parent;


        @Override
        public T[] values() {
            return this.values;
        }

        public void setParent(Block<T> p){
            this.parent = p;
        }

        public Block<T> parent(){
            return this.parent;
        }

        @Override
        public Block<T>[] children() {
            return this.children;
        }

        @Override
        public int full() {
            int check = 0;
            for(int i = 0; i < values.length; i++){
                if(values[i] != null) check++;
            }
            full = check;
            return full;
        }

        @Override
        public boolean empty() {
            if(this.full() == 0) return true;
            else return false;
        }

        @Override
        public Block<T> remove(T value) {
            
        }

        @Override
        public Optional<? extends Block<T>> find(T t) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Optional<Integer> depth(T t) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setFull(int n) {
            this.full = n;
            
        }
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
}
