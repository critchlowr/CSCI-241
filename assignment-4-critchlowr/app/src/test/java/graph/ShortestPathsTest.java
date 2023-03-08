package graph;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.LinkedList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShortestPathsTest {

    /* Performs the necessary gradle-related incantation to get the
       filename of a graph text file in the src/test/resources directory at
       test time.*/
    private String getGraphResource(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return resource.getPath();
    }

    /* Returns the Graph loaded from the file with filename fn located in
     * src/test/resources at test time. */
    private Graph loadBasicGraph(String fn) {
        Graph result = null;
        String filePath = getGraphResource(fn);
        try {
            result = ShortestPaths.parseGraph("basic", filePath);
        } catch (FileNotFoundException e) {
            fail("Could not find graph " + fn);
        }
        return result;
    }

    /**
     * Dummy test case demonstrating syntax to create a graph from scratch.
     * Write your own tests below.
     */
    @Test
    public void test00Nothing() {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        g.addEdge(a, b, 1);

        // sample assertion statements:
        assertTrue(true);
        assertEquals(2 + 2, 4);
    }

    /**
     * Minimal test case to check the path from A to B in Simple0.txt
     */
    @Test
    public void test01Simple0() {
        Graph g = loadBasicGraph("Simple0.txt");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        sp.compute(a);
        Node b = g.getNode("B");
        LinkedList<Node> abPath = sp.shortestPath(b);
        assertEquals(abPath.size(), 2);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(), b);
        assertEquals(sp.shortestPathLength(b), 1.0, 1e-6);
    }

    /**
     * Minimal test case to check the path from D to B in Simple1.txt
     */
    @Test
    public void test02Simple1() {
        Graph g = loadBasicGraph("Simple1.txt");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("D");
        sp.compute(a);
        Node b = g.getNode("B");
        LinkedList<Node> abPath = sp.shortestPath(b);
        assertEquals(abPath.size(), 2);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(), b);
        assertEquals(sp.shortestPathLength(b), 7.0, 1e-6);
    }

    /**
     * Minimal test case to check the path from D to A in Simple2.txt
     */
    @Test
    public void test03Simple2() {
        Graph g = loadBasicGraph("Simple2.txt");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("D");
        sp.compute(a);
        Node b = g.getNode("J");
        LinkedList<Node> abPath = sp.shortestPath(b);
        assertEquals(abPath.size(), 6);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(), b);
        assertEquals(sp.shortestPathLength(b), 11.0, 1e-6);
    }

    /**
     * Minimal test case to check the path from YUL to YYC in FakeCanada.txt
     */
    @Test
    public void test04FakeCanada() {
        Graph g = loadBasicGraph("FakeCanada.txt");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("YUL");
        sp.compute(a);
        Node b = g.getNode("YYC");
        LinkedList<Node> abPath = sp.shortestPath(b);
        assertEquals(abPath.size(), 4);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(), b);
        assertEquals(sp.shortestPathLength(b), 1995.0, 1e-6);
    }

    /* Pro tip: unless you include @Test on the line above your method header,
     * gradle test will not run it! This gets me every time. */
}
