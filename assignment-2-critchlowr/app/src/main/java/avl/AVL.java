/*
Ryan Critchlow
Feb 12th - 2023

AVL class is designed to perform insertions into BST and AVL trees.
The left and right rotation methods are utilized by rebalance, a method used by avlInsert after each new insert.
There are three private helper methods I created called getHeight, getBalance, and updHeight.
    getBalance uses getGeight to calculate the balance factor of nodes, which is used by rebalance to determine rotations.
    updHeight is used within leftRotate and rightRotate to adjust the height of nodes x and y after rotations.
    updHeight is also used to update the height in avlInsert to ensure proper parent pointer height.
 */
package avl;

public class AVL {

    public Node root;

    private int size;

    public int getSize() {
        return size;
    }
    /**
     * find w in the tree. return the node containing w or
     * null if not found
     */
    public Node search(String w) {
        return search(root, w);
    }

    private Node search(Node n, String w) {
        if (n == null) {
            return null;
        }
        if (w.equals(n.word)) {
            return n;
        } else if (w.compareTo(n.word) < 0) {
            return search(n.left, w);
        } else {
            return search(n.right, w);
        }
    }

    /**
     * insert w into the tree as a standard BST, ignoring balance
     */
    public void bstInsert(String w) {
        if (root == null) {
            root = new Node(w);
            size = 1;
            return;
        }
        bstInsert(root, w);
    }

    /* insert w into the tree rooted at n, ignoring balance
     * pre: n is not null */
    private void bstInsert(Node n, String w) {
        // insert left if w has fewer characters than n.left
        // increment size when an element is added
        // base case: insert w into node IF n.r/l is null, otherwise recurse
        if (w.compareTo(n.word) < 0) {
            if (n.left == null) {
                //create new node giving it a word and a parent
                n.left = new Node(w, n);
                size++;
            } else {
                bstInsert(n.left, w);
            }
            // insert right if w has more characters than n.right
        } else if (w.compareTo(n.word) > 0) {
            if (n.right == null) {
                //create new node giving it a word and a parent
                n.right = new Node(w, n);
                size++;
            } else {
                bstInsert(n.right, w);
            }
            // if equal do nothing of importance
        }
    }

    /**
     * insert w into the tree, maintaining AVL balance
     * precondition: the tree is AVL balanced and any prior insertions have been
     * performed by this method.
     */
    public void avlInsert(String w) {
        if (root == null) {
            root = new Node(w);
            size = 1;
            return;
        }
        avlInsert(root, w);
    }

    /* insert w into the tree, maintaining AVL balance
     *  precondition: the tree is AVL balanced and n is not null */
    private void avlInsert(Node n, String w) {
        // insert left if w has fewer characters than n.left
        // increment size when an element is added
        // base case: insert w into node IF n.r/l is null, otherwise recurse
        if (w.compareTo(n.word) < 0) {
            if (n.left == null) {
                // create new node giving it a word and a parent
                n.left = new Node(w, n);
                size++;
            } else {
                avlInsert(n.left, w);
            }
            // insert right if w has more characters than n.right
        } else if (w.compareTo(n.word) > 0) {
            if (n.right == null) {
                // create new node giving it a word and a parent
                n.right = new Node(w, n);
                size++;
            } else {
                avlInsert(n.right, w);
            }
        }
        // else initiate rebalance and then update heights
        rebalance(n);
        updHeight(n);
    }

    /**
     * do a left rotation: rotate on the edge from x to its right child.
     * precondition: x has a non-null right child
     */
    public void leftRotate(Node x) {
        // finished
        Node y = x.right;  // set y
        x.right = y.left;  // turn y's left subtree into x's right subtree
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent; // link x's parent to y
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x; // put x on y's left
        x.parent = y;
        //update the heights of modified nodes
        updHeight(x);
        updHeight(y);
    }

    /**
     * do a right rotation: rotate on the edge from x to its left child.
     * precondition: y has a non-null left child
     */
    public void rightRotate(Node y) {
        // finished
        Node x = y.left;  // set x
        y.left = x.right;  // turn x's right subtree into y's left subtree
        if (x.right != null) {
            x.right.parent = y;
        }
        x.parent = y.parent; // link y's parent to x
        if (y.parent == null) {
            root = x; //if y is the root, then make x the root
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }
        x.right = y; // put y on x's right
        y.parent = x;
        //update the heights of modified nodes
        updHeight(y);
        updHeight(x);
    }

    /**
     * rebalance a node N after a potentially AVL-violoting insertion.
     * precondition: none of n's descendants violates the AVL property
     */
    public void rebalance(Node n) {
        // get balance of node n
        int balNodeN = getBalance(n);
        // left heavy tree
        if (balNodeN < -1) {
            // case 1 - balNodeN < -1 and getBalance(n.left) ≤ 0
            // Right rotation around N
            if (getBalance(n.left) <= 0) {
                // Rotate node right
                rightRotate(n);
            }
            // case 2 - balNodeN < -1 and getBalance(n.left) > 0
            // Left rotation around L followed by right rotation around N
            else {
                leftRotate(n.left);
                rightRotate(n);
            }
        }
        // right heavy tree
        if (balNodeN > 1) {
            // case 3 - balNodeN > 1 and getBalance(n.right) ≥ 0
            // Left rotation around N
            if (getBalance(n.right) >= 0) {
                leftRotate(n);
            }
            // case 4 balNodeN > 1 and getBalance(n.right) < 0
            // Right rotation around R followed by left rotation around N
            else {
                rightRotate(n.right);
                leftRotate(n);
            }
        }
    }

    // returns the height of subtree stored in n.height OR -1 when subtree is empty.
    private int getHeight(Node n) {
        return n != null ? n.height : -1;
    }

    // calculate the n's balance factor
    private int getBalance(Node n) {
        return getHeight(n.right) - getHeight(n.left);
    }

    // sets n.height to the max height of the children plus 1.
    private void updHeight(Node n) {
        n.height = 1 + Math.max(getHeight(n.left), getHeight(n.right));
    }


    /**
     * remove the word w from the tree
     */
    public void remove(String w) {
        remove(root, w);
    }

    /* remove w from the tree rooted at n */
    private void remove(Node n, String w) {
        removeWork(n, w);
        updHeight(n);
        rebalance(n);
    }
    /* this is designed to recursively remove in all three cases discussed in lecture
    * this allows remove() to simply call this, update the height, and then recursively the tree*/
    private void removeWork(Node n, String w) {
        // if null get out of there, guy
        if (n != null) {
            //if we found the node, enter removal logic
            if (w.compareTo(n.word) < 0) {
                removeWork(n.left, w);
            } else if (w.compareTo(n.word) > 0) {
                removeWork(n.right, w);
            }
            // Case 1: if both children are null, delete n
            else if (n.left == null && n.right == null) {
                n = null;
            }
            // Case 2 (left): n's left child is null, replace n with its only child
            else if (n.left == null) {
                n = n.right;
            }
            // Case 2 (right): n's right child is null, replace n with its only child
            else if (n.right == null) {
                n = n.left;
            }
            // Case 3: n has two children
            else {
                //let k = min node in right subtree
                Node k = minValue(n.right);
                //replace n's value with k's value
                n.word = k.word;
                //remove k from n's right subtree
                removeWork(n.right, k.word);
            }
        }
    }
    //helper method to find the min left most leaf
    private Node minValue(Node n) {
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }

    /**
     * print a sideways representation of the tree - root at left,
     * right is up, left is down.
     */
    public void printTree() {
        printSubtree(root, 0);
    }

    private void printSubtree(Node n, int level) {
        if (n == null) {
            return;
        }
        printSubtree(n.right, level + 1);
        for (int i = 0; i < level; i++) {
            System.out.print("        ");
        }
        System.out.println(n);
        printSubtree(n.left, level + 1);
    }

    /**
     * inner class representing a node in the tree.
     */
    public class Node {
        public String word;
        public Node parent;
        public Node left;
        public Node right;
        public int height;

        public String toString() {
            return word + "(" + height + ")";
        }

        /**
         * constructor: gives default values to all fields
         */
        public Node() {
        }

        /**
         * constructor: sets only word
         */
        public Node(String w) {
            word = w;
        }

        /**
         * constructor: sets word and parent fields
         */
        public Node(String w, Node p) {
            word = w;
            parent = p;
        }

        /**
         * constructor: sets all fields
         */
        public Node(String w, Node p, Node l, Node r) {
            word = w;
            parent = p;
            left = l;
            right = r;
        }
    }
}
