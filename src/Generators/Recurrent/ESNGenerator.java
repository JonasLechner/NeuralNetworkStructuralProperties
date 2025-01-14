package Generators.Recurrent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ESNGenerator {

    public static void main(String[] args) {
        int numNodes = 30; // Number of neurons in the reservoir
        int numInputNodes = 1; // Number of input nodes
        int numOutputNodes = 1; // Number of output nodes
        double sparsity = 0.1; // Fraction of possible connections to use
        String outputFile = "testing/ESN_RNN_" + numInputNodes + "_" + numNodes + "_" + numOutputNodes + ".dgf";

        try {
            generateESN(numInputNodes, numNodes, numOutputNodes, sparsity, outputFile);
            System.out.println("ESN network with input and output layers generated successfully in " + outputFile);
        } catch (IOException e) {
            System.err.println("Error generating ESN: " + e.getMessage());
        }
    }

    public static void generateESN(int numInputNodes, int numNodes, int numOutputNodes, double sparsity, String outputFile) throws IOException {
        Random random = new Random();
        Set<String> edges = new HashSet<>();

        // Input to reservoir connections
        for (int i = 1; i <= numInputNodes; i++) {
            int connections = random.nextInt(5) + 1; // Each input connects to 1-5 reservoir nodes
            for (int j = 0; j < connections; j++) {
                int target = random.nextInt(numNodes) + 1 + numInputNodes; // Target is in reservoir layer
                edges.add(edgeKey(i, target));
            }
        }

        // Ensure all reservoir nodes are connected to at least one other node
        for (int i = 1 + numInputNodes; i <= numNodes + numInputNodes; i++) {
            int target;
            do {
                target = random.nextInt(numNodes) + 1 + numInputNodes;
            } while (target == i || edges.contains(edgeKey(i, target)));
            edges.add(edgeKey(i, target));
        }

        // Add cycles to create recurrent connections in the reservoir
        for (int i = 1 + numInputNodes; i <= numNodes + numInputNodes; i++) {
            int target;
            do {
                target = random.nextInt(numNodes) + 1 + numInputNodes;
            } while (target == i || edges.contains(edgeKey(i, target)));
            edges.add(edgeKey(i, target));
        }

        // Add additional edges based on sparsity
        int maxEdges = numNodes * (numNodes - 1) / 2;
        int numEdgesToAdd = (int) (sparsity * maxEdges) - edges.size();

        while (edges.size() < numEdgesToAdd + numNodes) {
            int source = random.nextInt(numNodes) + 1 + numInputNodes;
            int target = random.nextInt(numNodes) + 1 + numInputNodes;
            if (source != target && !edges.contains(edgeKey(source, target))) {
                edges.add(edgeKey(source, target));
            }
        }

        // Reservoir to output connections
        for (int i = 1; i <= numOutputNodes; i++) {
            int connections = random.nextInt(5) + 1; // Each output connects from 1-5 reservoir nodes
            for (int j = 0; j < connections; j++) {
                int source = random.nextInt(numNodes) + 1 + numInputNodes; // Source is in reservoir layer
                edges.add(edgeKey(source, numNodes + numInputNodes + i));
            }
        }

        // Add random self-loops (10% of reservoir nodes)
        /*int selfLoopsToAdd = (int) (0.1 * numNodes);
        for (int i = 0; i < selfLoopsToAdd; i++) {
            int node = random.nextInt(numNodes) + 1 + numInputNodes;
            edges.add(edgeKey(node, node));
        }*/

        // Write to .dgf file
        try (FileWriter writer = new FileWriter("graphs/" + outputFile)) {
            int totalNodes = numInputNodes + numNodes + numOutputNodes;
            writer.write("c Echo State Network with input, reservoir, and output layers (no self loops)\n");
            writer.write("p digraph " + totalNodes + " " + edges.size() + "\n");
            for (String edge : edges) {
                writer.write(edge + "\n");
            }
        }
    }

    private static String edgeKey(int node1, int node2) {
        if (node1 < node2) {
            return "e " + node1 + " " + node2;
        } else {
            return "e " + node2 + " " + node1;
        }
    }
}
