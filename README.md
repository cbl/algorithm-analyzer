# algorithm-analyzer

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
-   [Runtime](#runtime)

<a name="usage"></a>

## Usage

<a name="setup"></a>

### Setup

`mvn clean install`

<a name="run"></a>

### Run

`mvn exec:java`

<a name="build"></a>

### Build

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

![](./images/bubblesort.png)

<a name="insertionsort"></a>

### Insertionsort

![](./images/insertionsort.png)

<a name="quicksort"></a>

### Quicksort

![](./images/quicksort.png)
![](./images/quicksort_partitioning.png)

<a name="shellsort"></a>

### Shellsort

![](./images/shellsort.png)

<a name="selectionsort"></a>

### Selectionsort

![](./images/selectionsort.png)

<a name="heapsort"></a>

### Heapsort

![](./images/heapsort.png)
![](./images/heapsort_build.png)
![](./images/heapsort_sink.png)
![](./images/heapsort_bottom_up_sink.png)

<a name="countingsort"></a>

### Countingsort

![](./images/countingsort.png)

<a name="radixsort"></a>

### Radixsort

![](./images/radixsort.png)

See: [Countingsort](#countingsort)

<a name="data-structures"></a>

## Data Structures

<a name="binary-search"></a>

### Binary Search

![](./images/binary-search.png)

<a name="avl"></a>

### AVL Tree

![](./images/avl_rotate.png)
![](./images/avl_insert.png)
![](./images/avl_insert_recursive.png)
![](./images/avl_rotate_right.png)
![](./images/avl_rotate_left.png)
![](./images/avl_balance.png)
![](./images/avl_check_right.png)
![](./images/avl_check_left.png)
![](./images/avl_delete_1.png)
![](./images/avl_delete_2.png)
![](./images/avl_successor.png)
![](./images/avl_successor_recursive.png)
![](./images/avl_sort.png)

<a name="skiplist"></a>

### Skiplist

![](./images/skiplist-insert.png)
![](./images/skiplist-previous.png)
![](./images/skiplist-search.png)

<a name="graphs"></a>

## Graph Algorithms

<a name="deepsearch"></a>

### Deepsearch (Tiefensuche)

![](./images/deepsearch.png)
![](./images/deepsearch_expand.png)

<a name="breath-first-search"></a>

### Breath-First Search (Breitensuche)

![](./images/breath-first_search.png)

<a name="dijkstra">

### Dijkstra

![](./images/dijkstra.png)
![](./images/dijkstra_vertice_exists.png)
![](./images/dijkstra_next.png)

<a name="floyd-warshall">

### Floyd–Warshall Algorithm

![](./images/floyd-warshall.png)

<a name="runtime"></a>

## Runtime

| Algorithm     | Best Case  | Worst Case | Expected Case           |
| ------------- | ---------- | ---------- | ----------------------- |
| Shellsort     | O(n log n) | O(n²)      | Depends on gap sequence |
| Selectionsort | O(n²)      | O(n²)      | O(n²)                   |
| dijkstra      | O(n²)      | O(n²)      | O(n²)                   |
