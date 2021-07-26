package com.github.cbl.algorithm_analyzer.hashing;

import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.contracts.HashTable;
import com.github.cbl.algorithm_analyzer.util.TablePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CoalescedHashTable implements HashTable {
    EventConsumer events;
    Value[] table;
    int reserved;
    int size;
    int mod;

    public static record PartialStateEvent<T>(
            Value[] table, int size, int insertedKey, List<Integer> collision) implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            String[][] t = new String[3][];
            t[0] = new String[size + 1];
            t[1] = new String[size + 1];
            t[2] = new String[size + 1];

            t[0][0] = "Index";
            t[1][0] = "Key";
            t[2][0] = "Next Index";

            for (int i = 0; i < size; i++) {
                t[0][i + 1] = Integer.toString(i);
                if (table[i].state != State.Empty) {
                    t[1][i + 1] = Integer.toString(table[i].key);
                    t[2][i + 1] =
                            table[i].nextIndex != null ? Integer.toString(table[i].nextIndex) : "";
                }
            }

            sj.add("Inserted: " + insertedKey);
            sj.add("Collisions: " + collision.toString());
            sj.add(TablePrinter.toString(t));

            return sj.toString();
        }
    }

    public enum State {
        Occupied,
        Empty
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

    public CoalescedHashTable(EventConsumer events, int mod, int reserved) {
        this.events = events;
        this.size = reserved + mod;
        this.mod = mod;
        this.reserved = reserved;
        this.table = new Value[this.size];
        for (int i = 0; i < size; i++) this.table[i] = new Value();
    }

    public void insert(int key) {
        int i = key % this.mod;
        int nextIndex = getNextIndex();
        List<Integer> collisions = new ArrayList<Integer>();
        if (this.table[i].state == State.Empty) {
            table[i].key = key;
            table[i].state = State.Occupied;
            table[i].nextIndex = null;
        } else {
            collisions.add(i);
            while (table[i].key != key && table[i].nextIndex != null) {
                i = table[i].nextIndex;
                collisions.add(i);
            }
            for (int k = table.length - 1; k >= 0; k--) {
                if (table[k].state == State.Empty) {
                    table[i].nextIndex = k;
                    table[k].key = key;
                    table[k].state = State.Occupied;
                    table[k].nextIndex = null;
                    break;
                }
            }
        }

        this.events.accept(new PartialStateEvent(deepClone(table), size, key, collisions));
    }

    protected int getNextIndex() {
        for (int k = table.length - 1; k >= 0; k--) {
            if (table[k].state == State.Empty) return k;
        }

        return -1;
    }

    protected Value[] deepClone(Value[] v) {
        Value[] copy = new Value[v.length];

        for (int i = 0; i < v.length; i++) {
            copy[i] = new Value(v[i]);
        }

        return copy;
    }
}
