package com.github.cbl.algorithm_analyzer.hashing;

import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.contracts.HashTable;
import com.github.cbl.algorithm_analyzer.util.TablePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class BrentHashTable implements HashTable {
    EventConsumer events;
    Value[] table;
    int size;
    int mod;

    Hashing hashing;
    Hashing doubleHashing;

    public interface Hashing {
        int hash(int key);
    }

    public static record PartialStateEvent<T>(
            Value[] table,
            int size,
            int insertedKey,
            int hash,
            int doubleHash,
            Integer[] removed,
            List<Integer> collisions)
            implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            String[][] t = new String[3][];
            t[0] = new String[size + 1];
            t[1] = new String[size + 1];
            t[2] = new String[size + 1];

            t[0][0] = "Index";
            t[1][0] = "Key";
            t[2][0] = "Removed";

            for (int i = 0; i < size; i++) {
                t[0][i + 1] = Integer.toString(i);
                if (table[i].state != State.Empty) {
                    t[1][i + 1] = Integer.toString(table[i].key);
                    t[2][i + 1] = removed[i] != null ? removed[i].toString() : "";
                }
            }

            sj.add("Inserted: " + insertedKey);
            sj.add("h(" + insertedKey + "): " + hash);
            sj.add("h'(" + insertedKey + "): " + doubleHash);
            sj.add("Collisions: " + collisions.toString());
            sj.add(TablePrinter.toString(t));

            return sj.toString();
        }
    }

    public enum State {
        Occupied,
        Empty,
        Removed
    }

    public class Value {
        int key;
        State state = State.Empty;
        Integer nextIndex;

        Value() {}

        Value(Value v) {
            this.key = v.key;
            this.state = v.state;
            this.nextIndex = v.nextIndex;
        }
    }

    public BrentHashTable(EventConsumer events, int size, Hashing hashing, Hashing doubleHashing) {
        this.events = events;
        this.size = size;
        this.hashing = hashing;
        this.doubleHashing = doubleHashing;
        this.table = new Value[this.size];
        for (int i = 0; i < size; i++) this.table[i] = new Value();
    }

    public void insert(int key) {
        int i = this.hashing.hash(key);
        Integer[] removed = new Integer[this.size];
        List<Integer> collisions = new ArrayList<Integer>();
        while (table[i].state == State.Occupied) {
            collisions.add(i);
            int newfollows = (i + this.doubleHashing.hash(key)) % this.size;
            int oldfollows = (i + this.doubleHashing.hash(table[i].key)) % this.size;

            if (table[newfollows].state == State.Empty
                    || table[oldfollows].state == State.Occupied) {
                i = newfollows;
            } else {
                removed[i] = table[i].key;
                table[oldfollows].key = table[i].key;
                table[oldfollows].state = State.Occupied;
                table[i].state = State.Removed;
            }
        }
        table[i].key = key;
        table[i].state = State.Occupied;

        this.events.accept(
                new PartialStateEvent(
                        deepClone(table),
                        size,
                        key,
                        this.hashing.hash(key),
                        this.doubleHashing.hash(key),
                        removed,
                        collisions));
    }

    protected int linearProbing(int key) {
        return key % this.mod;
    }

    protected Value[] deepClone(Value[] v) {
        Value[] copy = new Value[v.length];

        for (int i = 0; i < v.length; i++) {
            copy[i] = new Value(v[i]);
        }

        return copy;
    }
}
