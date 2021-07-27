# algorithm-analyzer

A tool to analyze various sorting algorithms, graphs or binary trees. The repository contains implementations for algorithms that where taught by Prof. Dr. rer. nat. Karsten Weicker in the Sommersemester 2021 at the Htwk Leipzig.

This is a community project with the aim to exchange experiences among students. It is also designed to better understand the algorithms and how they work.

## Contributing

Everyone is welcome to contribute, please read the [contribution guideline](./.github/CONTRIBUTING.md) if you want to know how this works.

## Table Of Contents

-   [Usage](#usage)
    -   [Setup](#setup)
    -   [Run](#run)
    -   [Build](#build)
    -   [Test](#test)
    -   [Formatting Code](#formatting-code)
-   [Sorting Algorithms](sorting)
    -   [Bubblesort](#bubblesort)
    -   [Insertionsort](#insertionsort)
    -   [Quicksort](#quicksort)
    -   [Shellsort](#shellsort)
    -   [Selectionsort](#selectionsort)
    -   [Heapsort](#heapsort)
    -   [Radixsort](#radixsort)
-   [Data Structures](#data-structures)
    -   [Binary Search](#binary-search)
    -   [AVL Tree](#avl-tree)
    -   [Skiplist](#skiplist)
-   [Graph Algorithms](graphs)
    -   [Deepsearch (Tiefensuche)](#deepsearch)
    -   [Breath-First Search (Breitensuche)](#breath-first-search)
    -   [Dijkstra](#dijkstra)
    -   [Floyd–Warshall Algorithm](#floyd-warhsall)
    -   [Traveling Salesman (Rundreise)](#tsm)
-   [Hash Tables](#hash-tables)
    -   [Closed Hash Table](#closed-hash-table)
    -   [Double Hash Table](#double-hash-table)
    -   [Brent Hash Table](#brent-hash-table)
    -   [Coalesced Hash Table](#coalesced-hash-table)
-   [Runtime](#runtime)

<a name="usage"></a>

## Usage

<a name="setup"></a>

### Requirements

-   JDK `16` or newer
-   Maven `3.8.x` or newer

### Setup

`mvn clean install`

<a name="run"></a>

### Run

`mvn exec:java`

<a name="build"></a>

### Build

Compile to run directly in the terminal:

`mvn compile`

Package compiled source code into an executable jar file:

`mvn package`

Outputs to `target/algorithm-analyzer-x.x.x.jar`

<a name="test"></a>

### Test

`mvn test`

<a name="formatting-code"></a>

### Formatting Code

`/bin/sh bin/format.sh`

Calls google-code-formatter locally.

<a name="sorting"></a>

## Sorting Algorithms

<a name="bubblesort"></a>

### Bubblesort

```java
final Integer[] array = {15, 48, 22, 34, 27, 35, 14};

final Algorithm<Event, BubbleSort.Data<Integer>> a = new BubbleSort<Integer>();
final EventConsumer<Event> ec = new GeneralEventConsumer();

a.run(ec, new BubbleSort.Data<>(array));

ec.visitEvents(new LogEventVisitor());
```

<a name="insertionsort"></a>

### Insertionsort

```java
final Integer[] array = {15, 48, 22, 34, 27, 35, 14};

final Algorithm<Event, InsertionSort.Data<Integer>> a = new InsertionSort<Integer>();
final EventConsumer<Event> ec = new GeneralEventConsumer();

a.run(ec, new InsertionSort.Data<>(array));

ec.visitEvents(new LogEventVisitor());
```

<a name="quicksort"></a>

### Quicksort

```java
final Integer[] array = {20, 54, 28, 31, 5, 24, 39, 14, 1, 15};

final Algorithm<Event, Quicksort.Data<Integer>> a = new Quicksort<Integer>();
final EventConsumer<Event> ec = new GeneralEventConsumer();

a.run(ec, new Quicksort.Data<>(array));

ec.visitEvents(new LogEventVisitor());
```

<a name="shellsort"></a>

### Shellsort

```java
final Integer[] array = {6, 5, 4, 3, 2, 1};

final Algorithm<Event, Shellsort.Data<Integer>> a = new Shellsort<Integer>();
final EventConsumer<Event> ec = new GeneralEventConsumer();

a.run(ec, new Shellsort.Data<>(array));

ec.visitEvents(new LogEventVisitor());
```

<a name="selectionsort"></a>

### Selectionsort

```java
final Integer[] array = {64, 25, 12, 22, 11};

final Algorithm<Event, Selectionsort.Data<Integer>> a = new Selectionsort<Integer>();
final EventConsumer<Event> ec = new GeneralEventConsumer();

a.run(ec, new Selectionsort.Data<>(array));

ec.visitEvents(new LogEventVisitor());
```

<a name="heapsort"></a>

### Heapsort

```java
final Integer[] array = {20, 54, 28, 31, 5, 24, 39, 14, 1, 15};

final Algorithm<Event, HeapSort.Data<Integer>> a = new HeapSort<Integer>();
final EventConsumer<Event> ec = new GeneralEventConsumer();

a.run(ec, new HeapSort.Data<>(array));

ec.visitEvents(new LogEventVisitor());

```

<a name="countingsort"></a>

### Countingsort

```java
final Integer[] array = {2, 4, 2, 1, 1, 4, 2, 1, 4, 2};

final Algorithm<Event, Countingsort.Data<Integer>> a = new Countingsort<Integer>();
final EventConsumer<Event> ec = new GeneralEventConsumer();

a.run(ec, new Countingsort.Data<>(array));

ec.visitEvents(new LogEventVisitor());
```

<a name="radixsort"></a>

### Radixsort

See: [Countingsort](#countingsort)

```java
// Todo...
```

<a name="data-structures"></a>

## Data Structures

<a name="binary-search"></a>

### Binary Search

```java
final Integer[] array = {21, 25, 32, 33, 26, 40, 52, 53, 57, 60, 65, 66, 67, 78};
final Integer searchedValue = 60;

final Algorithm<Event, BinarySearch.Data<Integer>> a = new BinarySearch<>();
final EventConsumer<Event> ec = new GeneralEventConsumer();

a.run(ec, new BinarySearch.Data<Integer>(array, searchedValue));

ec.visitEvents(new LogEventVisitor());
```

<a name="avl"></a>

### AVL Tree

```java
AVLTree<Integer> t = new AVLTree<Integer>(Comparator.naturalOrder());
EventConsumer<Event> ec = new GeneralEventConsumer();
t.onEvent(ec);

t.insert(4);
t.insert(2);
t.insert(5);
t.insert(1);
t.insert(3);
t.insert(6);
t.insert(10);
t.insert(15);
t.remove(2);
t.insert(13);
t.remove(3);

ec.visitEvents(new LogEventVisitor());
```

<a name="skiplist"></a>

### Skiplist

```java
// Todo ...
```

<a name="graphs"></a>

## Graph Algorithms

<a name="deepsearch"></a>

### Deepsearch (Tiefensuche)

```java
int size = 4;
String[] nodeNames = {"A", "B", "C", "D"};
WeightFreeGraph<Integer> graph = new AdjacentMatrixGraph(size);

graph.setEdge(1, 0);
graph.setEdge(0, 3);
graph.setEdge(0, 2);
graph.setEdge(2, 1);
graph.setEdge(2, 3);
graph.setEdge(3, 1);
graph.setEdge(3, 2);

final Algorithm<Event, Deepsearch.Data> a = new Deepsearch();
final EventConsumer<Event> ec = new GeneralEventConsumer();

a.run(ec, new Deepsearch.Data(graph, nodeNames));

ec.visitEvents(new LogEventVisitor());
```

<a name="breath-first-search"></a>

### Breath-First Search (Breitensuche)

```java
String start = "v1";
Collection<Edge<String, Integer>> edges = Set.of(
    Edge.of("v1", "v2", 7),
    Edge.of("v1", "v3", 4),
    Edge.of("v1", "v4", 2),
    Edge.of("v2", "v3", 3),
    Edge.of("v2", "v5", 3),
    Edge.of("v3", "v4", 1),
    Edge.of("v3", "v5", 5),
    Edge.of("v4", "v5", 8)
);
Graph<String, Integer> graph = new LinkedGraph<>(edges);
for (Edge<String, Integer> e : edges) {
    graph.setEdge(e.to(), e.from(), e.weight());
}

final Algorithm<Event, BreathFirst.Data> a = new BreathFirst();
final EventConsumer<Event> ec = new GeneralEventConsumer();

a.run(ec, new BreathFirst.Data<String>(graph, start));

ec.visitEvents(new LogEventVisitor());
```

<a name="dijkstra"></a>

### Dijkstra

```java
Graph<Character, Integer> costs = new LinkedGraph<>(Set.of(
    Edge.of('A', 'B', 100),
    Edge.of('A', 'C', 50),
    Edge.of('B', 'C', 100),
    Edge.of('B', 'D', 100),
    Edge.of('B', 'E', 250),
    Edge.of('C', 'E', 250),
    Edge.of('D', 'E', 50)
));

final Algorithm<Event, Dijkstra.Data<Character>> a = new Dijkstra<>();
final EventConsumer<Event> ec = new GeneralEventConsumer();

a.run(ec, new Dijkstra.Data<Character>(costs, 'A'));

ec.visitEvents(new LogEventVisitor());
```

<a name="floyd-warshall"></a>

### Floyd–Warshall Algorithm

```java
Graph<Character, Integer> costs = new LinkedGraph<>(Set.of(
    Edge.of('v', 'x', 2),
    Edge.of('w', 'v', 10),
    Edge.of('w', 'x', 5),
    Edge.of('x', 'v', 3),
    Edge.of('x', 'y', 2),
    Edge.of('y', 'v', 10),
    Edge.of('y', 'w', 1)
));

Algorithm<Event, FloydWarshall.Data<Character>> alg = new FloydWarshall<>();
final EventConsumer<Event> ec = new GeneralEventConsumer();
alg.run(ec, new FloydWarshall.Data<>(costs));

ec.visitEvents(new LogEventVisitor());
```

<a name="tsm"></a>

### Traveling Salesman (Rundreise)

```java
Collection<Edge<Character, Integer>> edges = Set.of(
    Edge.of('A', 'B', 7),
    Edge.of('A', 'C', 5),
    Edge.of('A', 'D', 8),
    Edge.of('A', 'E', 12),
    Edge.of('B', 'C', 5),
    Edge.of('B', 'D', 9),
    Edge.of('B', 'E', 8),
    Edge.of('C', 'D', 4),
    Edge.of('C', 'E', 7),
    Edge.of('D', 'E', 9)
);

Graph<Character, Integer> graph = new LinkedGraph<>(edges);
for (Edge<Character, Integer> e : edges) {
    graph.setEdge(e.to(), e.from(), e.weight());
}

final Algorithm<Event, TravelingSalesman.Data<Character>> a = new TravelingSalesman<>();
final EventConsumer<Event> ec = new GeneralEventConsumer();

a.run(ec, new TravelingSalesman.Data<Character>(graph));

ec.visitEvents(new LogEventVisitor());
```

<a name="hash-tables"></a>

## Hash Tables

<a name="closed-hash-table"></a>

### Closed Hash Table

```java
int startSize = 11;
EventConsumer<Event> ec = new GeneralEventConsumer();
ClosedHashTable.Hashing hashing = (int key, int p) -> {
    return key % p;
};
ClosedHashTable.Probing probing = (int key, int j, int p) -> {
    return (key % p) + j;
};
ClosedHashTable table = new ClosedHashTable(ec, startSize, hashing, probing);
table.resizeFactor = 3 / 2; // Resize by next prime to 3/2 of size.
table.resizeAtOccupation = 0.9; // Resize at 90% occupation.

table.insert(29);
table.insert(12);
table.insert(7);
table.insert(19);
table.insert(30);
table.insert(40);
table.insert(11);
table.remove(7);
table.insert(18);

ec.visitEvents(new LogEventVisitor());
```

<a name="double-hash-table"></a>

### Double Hash Table

```java
int size = 11;
EventConsumer<Event> ec = new GeneralEventConsumer();
DoubleHashTable.Hashing hashing = (int key) -> {
    return key % 11;
};
DoubleHashTable.Hashing doubleHashing = (int key) -> {
    return (1 + (key % (11 - 1)));
};
DoubleHashTable table = new DoubleHashTable(ec, size, hashing, doubleHashing);

table.insert(29);
table.insert(12);
table.insert(7);
table.insert(19);
table.insert(30);
table.insert(40);
table.insert(11);

ec.visitEvents(new LogEventVisitor());
```

<a name="brent-hash-table"></a>

### Brent Hash Table

```java
int size = 11;
EventConsumer<Event> ec = new GeneralEventConsumer();
BrentHashTable.Hashing hashing =(int key) -> {
    return key % 11;
};
BrentHashTable.Hashing doubleHashing = (int key) -> {
    return (1 + (key % (11 - 1)));
};
BrentHashTable table = new BrentHashTable(ec, size, hashing, doubleHashing);

table.insert(29);
table.insert(12);
table.insert(7);
table.insert(19);
table.insert(30);
table.insert(40);
table.insert(11);

ec.visitEvents(new LogEventVisitor());
```

<a name="coalesced-hash-table"></a>

### Coalesced Hash Table

```java
int mod = 10;
int reserved = 2;
EventConsumer<Event> ec = new GeneralEventConsumer();
CoalescedHashTable table = new CoalescedHashTable(ec, mod, reserved);

table.insert(29);
table.insert(12);
table.insert(7);
table.insert(19);
table.insert(30);
table.insert(40);
table.insert(2);
table.insert(39);
table.insert(8);

ec.visitEvents(new LogEventVisitor());
```

<a name="runtime"></a>

## Runtime

| Algorithm     | Best Case  | Worst Case | Expected Case           |
| ------------- | ---------- | ---------- | ----------------------- |
| Shellsort     | O(n log n) | O(n²)      | Depends on gap sequence |
| Selectionsort | O(n²)      | O(n²)      | O(n²)                   |
| dijkstra      | O(n²)      | O(n²)      | O(n²)                   |
