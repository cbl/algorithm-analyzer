package com.github.cbl.algorithm_analyzer.hashing;

import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.contracts.HashTable;
import com.github.cbl.algorithm_analyzer.util.TablePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class ClosedHashTable implements HashTable {
    EventConsumer events;
    Value[] table;
    int size;
    int mod;
    public double resizeFactor = 3 / 2;
    public double resizeAtOccupation = 0.9;
    int occupied = 0;

    Hashing hashing;
    Probing probing;

    public interface Hashing {
        int hash(int key, int p);
    }

    public interface Probing {
        int hash(int key, int j, int p);
    }

    public static int nextPrime(int i) {
        int counter;
        i++;
        while (true) {
            int l = (int) Math.sqrt(i);
            counter = 0;
            for (int j = 2; j <= l; j++) {
                if (i % j == 0) counter++;
            }
            if (counter == 0) return i;
            else i++;
        }
    }

    public static record PartialStateEvent<T>(
            String message,
            Value[] table,
            int size,
            int insertedKey,
            int hash,
            int finalHash,
            int j,
            List<Integer> collisions)
            implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            String[][] t = new String[2][];
            t[0] = new String[size + 1];
            t[1] = new String[size + 1];

            t[0][0] = "Index";
            t[1][0] = "Key";

            for (int i = 0; i < size; i++) {
                t[0][i + 1] = Integer.toString(i);
                if (table[i].state != State.Empty) {
                    t[1][i + 1] = table[i].key != -1 ? Integer.toString(table[i].key) : "";
                }
            }

            sj.add(message + ": " + insertedKey);
            sj.add("h(" + insertedKey + "): " + hash);
            sj.add("ĥ(" + insertedKey + ", " + j + "): " + finalHash);
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

    public ClosedHashTable(EventConsumer events, int size, Hashing hashing, Probing probing) {
        this.events = events;
        this.size = size;
        this.hashing = hashing;
        this.probing = probing;
        this.table = new Value[this.size];
        for (int i = 0; i < size; i++) this.table[i] = new Value();
    }

    public void remove(int key) {
        int j = 0;
        int i;
        do {
            i = probing.hash(key, j, this.size);
            j++;
        } while (table[i].key != key && table[i].state != State.Empty);

        if (table[i].state == State.Occupied) {
            table[i].key = -1;
            table[i].state = State.Removed;
        } else {
            System.out.println("Error: Missing Key");
        }

        this.events.accept(
                new PartialStateEvent(
                        "Removed",
                        deepClone(table),
                        size,
                        key,
                        this.hashing.hash(key, this.size),
                        0,
                        0,
                        new ArrayList<Integer>()));
    }

    public void insert(int key) {
        int j = 0;
        int i = probing.hash(key, j, this.size) % this.size;
        int index = i;
        List<Integer> collisions = new ArrayList<Integer>();
        while (table[i].key != key && table[i].state == State.Occupied) {
            collisions.add(i);
            j++;
            i = probing.hash(key, j, this.size) % this.size;
            if (table[i].state != State.Occupied && table[index].state == State.Occupied) {
                index = i;
            }
        }

        if (table[i].state == State.Occupied) {
            System.out.println("Error: Occupied");
            return;
        } else {
            if (table[i].state == State.Empty && occupied > resizeAtOccupation * this.size) {
                resize();
                insert(key);
            } else {
                if (table[index].state == State.Empty) occupied++;
                table[index].key = key;
                table[index].state = State.Occupied;
            }
        }

        this.events.accept(
                new PartialStateEvent(
                        "Inserted",
                        deepClone(table),
                        size,
                        key,
                        this.hashing.hash(key, this.size),
                        i,
                        j,
                        collisions));
    }

    protected void resize() {
        int sizeOld = this.size;
        this.size = nextPrime((int) (this.resizeFactor * (double) this.size));
        Value[] oldTable = deepClone(table);
        table = new Value[this.size];
        for (int i = 0; i < this.size; i++) this.table[i] = new Value();
        this.occupied = 0;
        for (int i = 0; i < sizeOld; i++) {
            if (oldTable[i].state == State.Occupied) {
                this.insert(oldTable[i].key);
            }
        }
    }

    protected Value[] deepClone(Value[] v) {
        Value[] copy = new Value[v.length];

        for (int i = 0; i < v.length; i++) {
            copy[i] = new Value(v[i]);
        }

        return copy;
    }
}
