package com.github.cbl.algorithm_analyzer.hashing;

import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.contracts.HashTable;

import java.util.Map;

public class CoalescedHashTable<K, V> implements HashTable<K, V>
{
    EventConsumer events;
    Value[] table;
    int size;
    HashAlgorithm<K, V> hash;

    public enum State
    {
        Occupied,
        Empty
    }

    public class Value {
        K key;
        V value;
        State state = State.Empty;
        int nextIndex;

        public Vaule(K key, V value, int nextIndex)
        {
            this.key = key;
            this.value = value;
            this.nextIndex = nextIndex;
        }
    }

    public interface HashAlgorithm<K> {
        int hash(K key);
    }

    public CoalescedHashTable(EventConsumer events, int size) 
    {
        this.events = events;
        this.table = new Value[size];
        this.size = size;
        this.algorithm = (key) -> {
            return 0;
        };
    }

    public void hashUsing(HashAlgorithm<K> hash) {
        this.hash = hash;
    }

    public void insert(K key, V value)
    {
        int i = this.hash(key);
        int nextIndex = getNextIndex(i);
        if(this.table[i].state == State.Empty) {
            table[i].key = key;
            table[i].value = value;
            table[i].state = State.Occupied;
            table[i].nextIndex = nextIndex;
        } else {
            while(table[i].key != key && table[i].nextIndex != nextIndex) {
                i = table[i].nextIndex;
                if(table[i].state == State.Occupied) {
                    // TODO: Element bereits enthalten.
                    return;
                } else {
                    for(int last = table.length-1;last>=0;last--) {
                        if(table[last].state == State.Empty) break;
                        table[i].nextIndex = k;
                        table[k].key = key;
                        table[k].value = value;
                        table[k].state = Satte.Occupied;
                        table[k].nextIndex = nextIndex;
                    }
                }
            }
        }
    }

    protected int getNextIndex(int i) {
        for(;i<table.length;++i) {
            if(table[i].state == State.Empty) {
                return i;
            }
        }
    }
}