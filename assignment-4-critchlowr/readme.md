[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=10368959&assignment_repo_type=AssignmentRepo)
% CSCI 241 - A4
% Caroline Hardin
% Winter 2023

## Overview

In A4, you will implement Dijkstra's Single-Source Shortest Paths algorithm using a Graph class provided to you. You will write methods in the ShortestPaths class to implement Dijkstra's algorithm, reconstruct shortest paths after the algorithm has executed, and complete a command-line program that computes shortest paths on a graph read from a text file.

As usual, please keep track of the hours you spend on this assignment, as you will be asked to report it in A4 Survey. Hours spent will not affect your grade.

## Getting Started

The Github Classroom invitation link for this assignment is in Assignment 4 on Canvas. Begin by accepting the invitation and cloning a local working copy of your repository as usual.

### `import heap.Heap`

This assignment makes use of the priority queue you implemented in the Heap class in A3, which in turn depends on your HashTable and AList classes. 

Follow the [instructions from Lab 7](../lab7/#data-structure-use) for importing the Heap class as a dependency in your A4 project.

If you are not confident in the correctness of your A3 solution, you may download my [heap.jar](../lab7/heap.jar) and use that instead. Also if you're encountering strange bugs in your shortest paths algorithm, try plugging in the correct `heap.jar` to make sure the problem isn't originating in your A3 code.

## Testing A4

Your grade will be partly based on unit tests, but this time around you have not been provided with these tests. **It is your responsibility to test your implementation and be certain that the algorithm is implemented correctly.** ShortestPathsTest.java contains a placeholder test case to get you started writing your own tests. You will lose a few points if you do not write at least a few unit tests in ShortestPathsTest.java, although we will not be grading your test cases for correctness or comprehensiveness.

Some testing advice:

-   You should test your code both using JUnit test cases and the command line program implemented in the main method.

-   Two simple graphs (`Simple1.txt` and `Simple2.txt`) are provided in `app/src/test/resources/`. Run the algorithm by hand to determine the correct answers for these graphs and verify that your implementation arrives at the correct paths and path lengths.

-   **The sample graphs given are not sufficient to test your algorithm's correctness.** It's your responsibility to write tests that cover all possible cases that the algorithm could encounter.

-   You can write unit tests either by constructing Graph objects from scratch in code, or by parsing graphs from files in the `src/test/resources` directory. Opening these files at test time requires a little bit of gradle trickery, but we've included some example code and a helper method in `ShortestPathsTest.java` that finds the path for a given filename and uses `ShortestPaths.parseGraph` to read a graph from a file located in `src/test/resources`.

-   Make sure your algorithm handles edge cases correctly, including behaving as specified when the destination node is unreachable. Test this using the simplest possible test cases (for example, this edge case could be tested using a two-node graph with only an edge from destination to origin).

-   The BasicParser class parses a simple edge list from a text file, such as `Simple1.txt`, `Simple2.txt`, and `FakeCanada.txt`. Feel free to write and test using additional graph files in this format.

-   See the information in `data/db1b_info.txt` for how to download a larger, more real-world dataset of flight information and run your algorithm on it.

## Implementing The Algorithm

The abstract version of Dijkstra's algorithm presented in lecture is as follows:

    /** compute shortest paths to all ndoes from
      * origin node v */
    shortest_paths(v):
      S = { };
      F = {v};
      v.d = 0;
      v.bp = null;
      while  (F != {})  {
        f = node in F with min d value;
        Remove f from F, add it to S;
        for each neighbor w of f {
          if (w not in S or F) {
            w.d =  f.d + weight(f, w);
            w.bp = f;
            add w to F;
          } else if (f.d + weight(f,w) < w.d) {
            w.d = f.d + weight(f,w);
            w.bp = f;
          }
        }
      }

Your implementation should follow this abstract algorithm as closely as possible, but the graph representation won't match the pseudocode exactly because we have to deal with practical implementation considerations. The following design decisions have been made for you:

-   So that we can efficiently find the node in F with minimum d value, the Frontier set is stored in a min-heap, using d-values as priorities. Because a Node's d-value may change, you will need to use the Heap's changePriority method to keep the priorities updated.

-   Instead of the Node class having a field for `d` and `bp`, we store these things separately. The ShortestPaths class maintains a Map that associates each Node with a PathData object, which stores the distance and backpointer for a node.

-   The Settled set does not need to be explicitly stored. If a Node has a PathData object associated with it, it is in either the Settled (not in the heap) or Frontier set (in the heap); otherwise it is in the Unexplored set.

## Main Method Behavior

The main method behavior is specified in the descriptions below and in comments associated with each TODO item. In brief, the program takes 3 or 4 command-line arguments. The first two specify the file type (basic or db1b) and the filename where the graph data is stored. The third is an origin node id, from which shortest paths should be computed. If no fourth argument is supplied, the program should print all reachable nodes and their shortest path distances. If the fourth argument is supplied, it gives a destination node, and the program should print in order the nodes along the path from the origin to the destination, followed by the length of the path.

Two sample invocations of the program are given below:

```
    $ gradle run --args "basic src/test/resources/Simple0.txt A"
    Graph has:
      3 nodes.
      3 edges.
      Average degree 1.0
    Shortest paths from A:
    B: 1.0
    C: 2.0
    A: 0.0
    
    $ gradle run --args "basic src/test/resources/Simple0.txt A C"
    Graph has:
    3 nodes.
    3 edges.
    Average degree 1.0
    A C 2.0
```
## Your Tasks

You will be implementing Dijkstra's algorithm, writing helper methods to reconstruct paths and fetch path lengths, and finishing the main method, all in ShortestPaths.java. As in A3, there are not very many lines of code to write, but you cannot write them without first understanding the algorithm and the codebase you're working in.

0. Begin by looking over the slides from Lecture 18C, where the implementation details are covered. Carefully read and understand the `/** Javadoc comments */` in Graph.java, Node.java, and ShortestPaths.java. Read over the code in these files as well. When you're done, you should be able to answer questions such as:

   1.  What is the purpose of each of the following HashMaps?

       -   `Graph`'s `nodes` field
       -   `Node`'s `neighbors` field
       -   `ShortestPath`'s `paths` field
   2.  Where is the Graph's adjacency list stored, how would you iterate over all edges leaving a given Node, and how would you get the weight of each edge?
   3.  What are types of the Values (V) and Priorities (P) in the min-heap storing F?
   4.  For a given Node object, where are `n.d` and `n.bp` stored, and how would you access them?
   
   If you can't answer these questions, you're unlikely to make much progress on the code.

1. Implement the `compute` method in the ShortestPaths class according to its specification.
2. Implement the `shortestPathLength` method. Notice that this method's precondition states that `compute` has been called with the desired origin node, so the `paths` field should already be filled in with the final shortest paths data.
3. Implement the `shortestPath` method. Once again, `compute` has already been called with the desired origin so you only need to use the data stored in `paths` to reconstruct the path.


   4.  In the `main` method, create and use an instance of ShortestPaths to compute shortest-paths data from the origin node specified in the command line arguments.

   5.  If a destination node was **not** specified on the command line, print a table of reachable nodes and their shortest path lengths.

   6.  If a destination node **was** specified on the command line, print the nodes in the shortest path from the origin to the destination, followed by the length of the path.

## Extra Credit

You'll have noticed based on ShortestPaths' main method that the codebase is able to read graphs from two types of text files, denoted basic and db1b. When a db1b file is given, the DB1BParser class parses a Graph from a csv file with data showing actual flights, their origin and destination, and the number of miles flown. The DB1BCoupon dataset I used is available at <https://www.transtats.bts.gov/Fields.asp?gnoyr_VQ=FLM>.

Back when traveling was a thing, and I was making plans to see my folks in Vermont for winter break, I wondered about the shortest path from Bellingham to Burlington, Vermont. I downloaded flight data from the Bureau of Transporation Statistics for the first quarter of 2018 and ran the following command:

    % gradle run --args "db1b data/DB1BCoupon_2018_1.csv BLI BTV"
    BLI GEG MSP BUF BTV 2454.0

It turns out the shortest route to Vermont, in terms of miles, involves flying from Bellingham to Spokane to Minneapolis to Buffalo to Burlington, a total of 4 flights! Surely that's not the best way to fly there.

In air travel, it's rarely the case that more flights get you there faster than fewer flights---you usually want to get there with the fewest hops possible. For up to 3 points of extra credit, create an `enhancements` branch and extend your ShortestPaths class to also compute the route between a given origin and destination with the fewest flights, regardless of the total number of miles flown. For full credit, compute both the path itself and the number of edges in the path.

How you design your solution is up to you, but you should keep your modifications in ShortestPaths.java and make sure they're in a separate `enhancements` branch. Write a detailed comment at the top of ShortestPaths.java explaining

1.  How to use your program (try to keep it similar to the behavior of the base assignment).

2.  How you solved the problem and any design decisions you made, including any relation your solution has to algorithms we've discussed in class.

## Game Plan

To complete this assignment in a low-stress manner, here's a suggested timeline:

-   By the end of Friday, 3/3: You have read and understood the codebase and documentation. You can answer the questions in Section 6, Task 0.

-   By the end of Sunday, 3/5: You have completed an implementation of the `compute` method and written thorough test cases.

-   By the end of Tuesaday 3/7: You have implemented `shortestPathLength`, `shortestPath`, and implemented the main method functionality.

-   By 10pm Wed 3/8 Any enhancements completed, code pushed, and A4 Survey filled out.

## How and What to Submit

Check the following things before you submit:

1.  Your code follows the style guidelines set out in the rubric and the syllabus.

2.  Your submission compiles and runs on the command line in Linux without modification.

3.  All code, including unit tests, is committed and pushed to your A4 GitHub repository.

Submit the assignment by pushing your final changes to GitHub before the deadline, then submitting the A4 Survey on Canvas. If you completed any enhancements, be sure to push your enhancements branch as well.

## Rubric

You can earn points for the correctness and efficiency of your program, and points can be deducted for errors in commenting, style, clarity, and following assignment instructions. Correctness will be judged on both unit tests on your ShortestPaths class as well as the correct behavior exhibited by the program implemented in the `main` method.

**Code : Correctness**

* (24 points) Unit tests of methods in ShortestPaths
* (5 points) Correct behavior of the ShortestPaths main method program with no destination specified
* (5 points) Correct behavior of the ShortestPaths main method program with a destination specifie

**Code : Unit Tests**

* (5 points) At least a few test test are written in ShortestPathsTest.java

**Code : Efficiency**

* (5 points) `ShortestPaths.compute` uses a Heap to find the node to move from F to S in O(log $n$).
* (5 points) The body of the `for` loop in `ShortestPaths.compute` runs in O(log $n$) expected time.

**Clarity deductions** (up to 2 points each)

* Include author, date and purpose in a comment comment at the top of each file you write any code in
* Methods you introduce should be accompanied by a precise specification
* Non-obvious code sections should be explained in comments
* Indentation should be consistent
* Method should be written as concisely and clearly as possible
* Methods should not be too long - use private helper methods
* Code should not be cryptic and terse
* Variable and function names should be informative
