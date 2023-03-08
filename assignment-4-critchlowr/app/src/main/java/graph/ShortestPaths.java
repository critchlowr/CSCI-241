package graph;

import heap.HashTable;
import heap.Heap;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import heap.Heap;

/**
 * Provides an implementation of Dijkstra's single-source shortest paths
 * algorithm.
 * Sample usage:
 * Graph g = // create your graph
 * ShortestPaths sp = new ShortestPaths();
 * Node a = g.getNode("A");
 * sp.compute(a);
 * Node b = g.getNode("B");
 * LinkedList<Node> abPath = sp.getShortestPath(b);
 * double abPathLength = sp.getShortestPathLength(b);
 */
public class ShortestPaths {
    // stores auxiliary data associated with each node for the shortest
    // paths computation:
    private HashMap<Node, PathData> paths;

    /**
     * Compute the shortest path to all nodes from origin using Dijkstra's
     * algorithm. Fill in the paths field, which associates each Node with its
     * PathData record, storing total distance from the source, and the
     * backpointer to the previous node on the shortest path.
     * Precondition: origin is a node in the Graph.
     */
    public void compute(Node origin) {
        // initialize variables
        paths = new HashMap<Node, PathData>();
        Heap<Node, Double> frontier = new Heap<>();
        frontier.add(origin, 0.0);
        Node f;
        double pathLength;
        // add origin into frontier and paths to start search
        paths.put(origin, new PathData(0, null));
        // the work
        while (frontier.size() != 0) {
            // grab minimum priority node from frontier
            f = frontier.poll();
            HashMap<Node, Double> fNeighbors = f.getNeighbors();
            // for all neighbors of f
            for (Node w : fNeighbors.keySet()) {
                double fd = paths.get(f).distance;
                double edgeWeight = fNeighbors.get(w);
                if (!frontier.contains(w) && !paths.containsKey(w)) {
                    pathLength = fd + edgeWeight;
                    paths.put(w, new PathData(pathLength, f));
                    frontier.add(w, pathLength);
                }
                // determine if already discovered node has a shorter path than prev route
                else if (fd + fNeighbors.get(w) < paths.get(w).distance) {
                    edgeWeight += fd;
                    frontier.changePriority(w, edgeWeight);
                    paths.put(w, new PathData(edgeWeight, f));
                }
            }
        }
    }

    /**
     * Returns the length of the shortest path from the origin to destination.
     * If no path exists, return Double.POSITIVE_INFINITY.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called.
     */
    public double shortestPathLength(Node destination) {
        if (paths.containsKey(destination)) {
            return paths.get(destination).distance;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    /**
     * Returns a LinkedList of the nodes along the shortest path from origin
     * to destination. This path includes the origin and destination. If origin
     * and destination are the same node, it is included only once.
     * If no path to it exists, return null.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called.
     */
    public LinkedList<Node> shortestPath(Node destination) {
        LinkedList<Node> n = new LinkedList<>();
        if (!paths.containsKey(destination)) {
            return null;
        } else {
            // add first element only
            n.add(destination);
            Node prev;
            // loop through all previous nodes touching our destination node
            for (prev = paths.get(destination).previous; prev != null; prev = paths.get(prev).previous) {
                n.addFirst(prev);
            }
            return n;
        }
    }

    /**
     * Inner class representing data used by Dijkstra's algorithm in the
     * process of computing the shortest paths from a given source node.
     */
    class PathData {
        double distance; // distance of the shortest path from source
        Node previous; // previous node in the path from the source

        /**
         * constructor: initialize distance and previous node
         */
        public PathData(double dist, Node prev) {
            distance = dist;
            previous = prev;
        }
    }

    /**
     * Static helper method to open and parse a file containing graph
     * information. Can parse either a basic file or a DB1B CSV file with
     * flight data. See GraphParser, BasicParser, and DB1BParser for more.
     */
    protected static Graph parseGraph(String fileType, String fileName) throws
            FileNotFoundException {
        // create an appropriate parser for the given file type
        GraphParser parser;
        if (fileType.equals("basic")) {
            parser = new BasicParser();
        } else if (fileType.equals("db1b")) {
            parser = new DB1BParser();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported file type: " + fileType);
        }

        // open the given file
        parser.open(new File(fileName));

        // parse the file and return the graph
        return parser.parse();
    }

    public static void main(String[] args) {
        // read command line args
        String fileType = args[0];
        String fileName = args[1];
        String origCode = args[2];

        String destCode = null;
        if (args.length == 4) {
            destCode = args[3];
        }

        // parse a graph with the given type and filename
        Graph graph;
        try {
            graph = parseGraph(fileType, fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open file " + fileName);
            return;
        }
        graph.report();

        ShortestPaths sp = new ShortestPaths();
        Node start = graph.getNode(origCode);
        sp.compute(start);
        if (destCode == null) {
            System.out.println("Shortest path from: " + origCode);
            HashMap<Node, Double> fNeighbors = start.getNeighbors();
            for (Node n : fNeighbors.keySet()) {
                System.out.println(n + ":" + " " + fNeighbors.get(n));
            }
            System.out.println(start + ":" + " 0.0");
        } else {
            Node destination = graph.getNode(destCode);
            System.out.println("The shortest path from start to destination is: " + sp.shortestPath(destination));
            double abPathLength = sp.shortestPathLength(destination);
            System.out.println("The shortest path from " + origCode + " to " + destCode + " is " + abPathLength);
        }
    }
}
