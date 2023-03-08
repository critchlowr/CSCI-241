[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=10009498&assignment_repo_type=AssignmentRepo)
% CSCI 241 - A2
% Caroline Hardin
% Winter 2022

## Overview

You are provided with a partial implementation of a Binary Search tree in `AVL.java`. Your task will be to complete this implementation and turn the BST into an AVL tree by maintaining the AVL balance property on insertion. You will use this AVL tree to efficiently count the number of unique lines in a text document.

Putting elements into a set is one way to find how many unique items are in the set. Though we aren't using the name, you'll notice that our AVL tree is implementing the Set ADT here, storing a set of Strings with no duplicates allowed.

## Getting Started

The Github Classroom invitation link for this assignment is in Assignment 2 on Canvas. Begin by accepting the invitation and cloning a local working copy of your repository as you did in Assignment 1. Make sure to clone it somewhere outside the local working copies for other assignments and labs (e.g., clone to `~/csci241/a2`) to avoid nesting local repositories.

## Main Program

The main program is found in `Unique.java`. This program takes two command line arguments: a mode and the name of a text file. The mode is either `naive` or `avl` (it defaults to `avl` if anything other than those two is given). The program reads lines from the text file one by one and prints the number of unique lines. A naive $O(n^2)$ solution is already implemented for you in `naiveUnique`. Your job is to implement the `avlUnique` method such that it counts the number of unique lines in the file in $O(n \log n)$. To do this, you'll complete the AVL tree implementation in `AVL.java`.

Two sample text files are provided to demo the difference between the naive and efficient solutions. `prefixes.txt` contains the first 5 characters of each word in a large list of English words (you can find a similar one on the lab systems at `/usr/share/dict/american-english`). `prefixes_small.txt` contains the first 50,000 lines of that file. On my laptop, the naive implementation takes around 26 seconds on the complete file and about 3 seconds for the small file. The efficient implementation takes a second or less on either file, thanks to its $O(n \log n)$ runtime.

    $ gradle run --args "avl prefixes.txt"
    > Task :compileJava UP-TO-DATE
    > Task :processResources NO-SOURCE
    > Task :classes UP-TO-DATE
    
    > Task :run
    Finding unique lines in prefixes.txt
    prefixes.txt
    AVL:
    65137
    
    BUILD SUCCESSFUL in 1s
    2 actionable tasks: 1 executed, 1 up-to-date
    
    $ gradle run --args "naive prefixes.txt"
    > Task :compileJava UP-TO-DATE
    > Task :processResources NO-SOURCE
    > Task :classes UP-TO-DATE
    
    > Task :run
    Finding unique lines in prefixes.txt
    Naive:
    65137
    
    BUILD SUCCESSFUL in 26s
    2 actionable tasks: 1 executed, 1 up-to-date
    $

## Your Tasks

Skeleton code is provided in your repository. The AVL class in `app/src/main/java/avl/AVL.java` currently implements the `search` functionality for a BST.

1.  Implement standard BST (*not* AVL) insert functionality in the provided `bstInsert` method stub. As with `search`, the AVL class has a public `bstInsert(String w)` method that calls a private `bstInsert(Node n, String w)` method that recursively inserts on nodes. Notice that AVL class has a `size` field that should be kept up to date as words are inserted. *Note: `bstInsert` does not need to keep heights up-to-date; this is only necessary in `avlInsert`, and you should assume that `bstInsert` calls are not mixed with avlInsert calls.*

2.  Implement `leftRotate` and `rightRotate` helper methods to perform a rotation on a given node. Use the lecture slides as a reference.

3.  Implement `rebalance` to fix a violation of the AVL property caused by an insertion. In the process, you'll need to correctly maintain the `height` field of each node. Remember that height needs to be updated any time the tree's structure changes.

4.  Implement `avlInsert` to maintain AVL balance in the tree after insertions using the `rebalance` method.

5.  Use your completed AVL tree class to implement the `avlUnique` method in `Unique.java` such that it runs in $O(n \log n)$ time.

6.  For up to 5 points of extra credit, you may complete some or all of the enhancements described below.

7.  Submit the A2 Survey quiz, including the estimated total number of hours you spent on this assignment.

### Implementation notes

-   **Public method specifications and signatures should not be changed**: if method names, call signatures, or return values change, your code will not compile with the testing system and you'll receive no credit for the correctness portion of your grade.
-   You may write and use as many `private` helper methods as you need. You are especially encouraged to use helper methods for things like calculating balance factors, updating heights, etc., in order to keep the code for intricate procedures like `rebalance` easy to read.
-   The skeleton code implements a try/catch block to handle nonexistent files in `Unique.java`. Error catching beyond this is not required - you may assume well-formed user input and that method preconditions will not be violated.
-   Be careful with parent pointers. A recursive tree traversal such as the reverse-in-order traversal used in `printTree` never follows parent pointers; this means parent pointers can be misplaced and `printTree` will still look normal.
-   Keep in mind that the `height` method from Lab 3 is $O(n)$,  which means it's not suitable for our purposes. To maintain efficiency, you'll need to update the height of each node along the insertion path from the bottom up.
-   You are provided with a test suite in `app/src/test/java/avl/AVLTest.java`. Use `gradle test` often and pass tests for each task before moving onto the next.

## Enhancements

**Enhancements and git** The base project will be graded based on the master branch of your repository. Before you change your code in the process of completing enhancements, create a new branch in your repository (e.g., `git checkout -b enhancements`). Keep all changes related to enhancements on this branch---this way you can add functionality, without affecting your score on the base project. Make sure you've pushed both master and enhancements branches to GitHub before the submission deadline.

You can earn up to 3 points of extra credit for completing the following:

1.  (1 point) Implement `remove`, maintaining AVL balance.

2.  (1 point) The base assignment implements a Set of Strings. In your enhancements branch, modify your code to instead implement a Map from strings to integers. This will allow it to behave something like a `HashMap<String, Integer>` would. In the context of lines of a document, this means you'll keep track of the number of occurrences of each line you've seen. Modify your main program to use this to calculate the most frequently-occurring line in addition to the number of unique lines.

3.  (1 point) Modify your tree to fully support the following semantics for removal: removing an element either decrements its count (if count was greater than 1) or removes it from the tree entirely (if the count was 1).

If you complete any of the above, explain what you did, how you did it, and instructions for testing your enhancements in a comment at the top of the corresponding java file.

## Game Plan

Start small, test incrementally, and git commit often. Please keep track of the number of hours you spend on this assignment, as you will be asked to report it in A2 Survey. Hours spent will not affect your grade.

The tasks are best completed in the order presented. Make sure you pass the tests for the current task before moving on to the next. Rotations and rebalancing are the trickiest part. Visit the mentors, come to office hours, or post on Piazza if you are stuck. A suggested timeline for completing the assignment in a stress-free manner is given below:

-   By 2/4: BST insertion completed and tested.
-   By 2/7: rotations implemented and tested.
-   By 2/10: rebalance implemented and tested.
-   By 2/13: AVL insertion and main program behavior implemented and tested.
-   By 2/14: Any enhancements completed.
-   By 10pm on 2/15: Final changes pushed to github, A2 Survey submitted.

## How and What to Submit

Submit the assignment by pushing your final changes to GitHub before the deadline, then submitting A2 Survey on Canvas. If you completed any enhancements, be sure to push your enhancements branch as well.

## Rubric

You can earn points for the correctness and efficiency of your program, and points can be deducted for errors in commenting, style, clarity, and following assignment instructions. A2 is out of a total of 50 points.

**Submission**

* (1 point) Code is pushed to github and hours are reported in A2 Survey.

**Code : Correctness**

* (33 points) Unit tests (1.5 points per test)
* (6 points) Main program prints the correct number of unique lines

**Code : Efficiency**

* (5 points) `avlInsert` maintains $O(\log n)$ performance by keeping track of node heights and updating them as necessary
* (5 points) `Unique` processes a document with $n$ words in  $O(n \log n)$ time

**Clarity deductions**  (up to 2 points each)

* Include author, date and purpose in a comment comment at the top of each file you write any code in
* Methods you introduce should be accompanied by a precise specification
* Non-obvious code sections should be explained in comments
* Indentation should be consistent
* Methods should be written as concisely and clearly as possible
* Methods should not be too long - use private helper methods
* Code should not be cryptic and terse
* Variable and function names should be informative
