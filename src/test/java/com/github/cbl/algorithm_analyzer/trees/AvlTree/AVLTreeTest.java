package com.github.cbl.algorithm_analyzer.trees.AvlTree;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AVLTreeTest {

    @Test
    @DisplayName("Empty tree is empty")
    public void emptyTreeIsEmpty() {
        var t = AVLTree.newIntTree();
        assertEquals(List.of(), t.values());
    }

    @Test
    @DisplayName("Inserts nodes without rotation")
    public void insertNodesWithoutRotation() {
        var t = AVLTree.newIntTree();
        t.insert(5);
        t.insert(2);
        t.insert(10);
        assertEquals(Arrays.asList(5, 2, 10), t.values());
    }

    @Test
    @DisplayName("Deletes nodes without rotation")
    public void insertNode() {
        var t = AVLTree.newIntTree();
        t.insert(5);
        t.insert(2);
        t.insert(10);
        assertEquals(Arrays.asList(5, 2, 10), t.values());

        t.remove(10);
        assertEquals(Arrays.asList(5, 2, null), t.values());

        t.remove(5);
        assertEquals(Arrays.asList(2), t.values());
    }

    @Test
    @DisplayName("Inserts nodes with rotation")
    public void insertNodesWithRotation() {
        var t = AVLTree.newIntTree();
        t.insert(5);
        t.insert(2);
        t.insert(10);
        t.insert(8);
        t.insert(12);
        t.insert(9);
        assertEquals(Arrays.asList(8, 5, 10, 2, null, 9, 12), t.values());

        t = AVLTree.newIntTree();
        t.insert(10);
        t.insert(5);
        t.insert(15);
        t.insert(3);
        t.insert(7);
        t.insert(6);
        assertEquals(Arrays.asList(7, 5, 10, 3, 6, null, 15), t.values());
    }

    @Test
    @DisplayName("Deletes nodes with rotation")
    public void deleteNodesWithRotation() {
        var t = AVLTree.newIntTree();
        t.insert(5);
        t.insert(2);
        t.insert(10);
        t.insert(8);
        t.remove(2);
        assertEquals(Arrays.asList(8, 5, 10), t.values());

        t = AVLTree.newIntTree();
        t.insert(10);
        t.insert(5);
        t.insert(15);
        t.insert(7);
        t.remove(15);
        assertEquals(Arrays.asList(7, 5, 10), t.values());
    }

    @Test
    @DisplayName("Calculates height correctly")
    public void calculatesHeightCorrectly() {
        var t = AVLTree.newIntTree();
        assertEquals(0, t.height());

        t.insert(10);
        assertEquals(1, t.height());

        t.insert(15);
        assertEquals(2, t.height());

        t.insert(5).insert(20);
        assertEquals(3, t.height());
    }

    @Test
    @DisplayName("Calculates depth correctly")
    public void calculatesDepthCorrectly() {
        var t = AVLTree.newIntTree();
        t.insert(4);
        t.insert(2);
        t.insert(5);
        t.insert(1);
        t.insert(3);
        t.insert(6);
        t.insert(10);
        t.insert(15);

        assertEquals(Optional.of(1), t.depth(4));
        assertEquals(Optional.of(2), t.depth(2));
        assertEquals(Optional.of(3), t.depth(10));
        assertEquals(Optional.of(4), t.depth(15));
        assertEquals(Optional.empty(), t.depth(20));
    }
}
