package PropertiesCalculators;

import PropertiesCalculators.Helper.Edge;
import PropertiesCalculators.Helper.Graph;

import java.util.*;

public class VertexCoverCalculator {

    public static Set<Integer> findVertexCover(Graph graph) {
        Set<Integer> vertexCover = new HashSet<>();
        Set<Edge> edges = new HashSet<>(graph.getEdges());

        while (!edges.isEmpty()) {
            Edge edge = edges.iterator().next();
            int node1 = edge.getNode1(); // Start node
            int node2 = edge.getNode2(); // End node

            // Add the start node (node1) to the vertex cover
            vertexCover.add(node1);

            // Remove all edges starting from node1
            edges.removeIf(e -> e.getNode1() == node1);
        }

        return vertexCover;
    }

    public static Set<Integer> findMinVertexCover(Graph graph) {
        Set<Integer> vertexCover = new HashSet<>();
        Set<Edge> edges = new HashSet<>(graph.getEdges()); // Copy of all edges

        while (!edges.isEmpty()) {
            // Find the node with the highest degree (most outgoing edges)
            Map<Integer, Integer> degreeMap = new HashMap<>();

            for (Edge edge : edges) {
                degreeMap.put(edge.getNode1(), degreeMap.getOrDefault(edge.getNode1(), 0) + 1);
                degreeMap.put(edge.getNode2(), degreeMap.getOrDefault(edge.getNode2(), 0));
            }

            // Get the node with the maximum degree
            int maxDegreeNode = Collections.max(degreeMap.entrySet(), Map.Entry.comparingByValue()).getKey();

            // Add the node to the vertex cover
            vertexCover.add(maxDegreeNode);

            // Remove all edges incident to the chosen node
            edges.removeIf(e -> e.getNode1() == maxDegreeNode || e.getNode2() == maxDegreeNode);
        }

        return vertexCover;
    }


    public static void main(String[] args) {

        String graphName = "KolmogorovNetwork_2.dgf";
        String testFolder = "testing/";
        String thesisFolderFF = "thesis/feedForward/";
        String thesisFolderHopfield = "thesis/hopfield/";
        String thesisFolderRecurrent = "thesis/recurrent/";
        String graphPath = "graphs/" + testFolder + graphName;
        Graph graph = new Graph(graphPath);

        Set<Integer> vertexCover = findMinVertexCover(graph);

        System.out.println("Vertex Cover: " + vertexCover);
        System.out.println("Size of Vertex Cover: " + vertexCover.size());
    }
}
