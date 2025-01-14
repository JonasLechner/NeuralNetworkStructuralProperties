package Generators.Recurrent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ESNGenerator {
    public static void main(String[] args) {
        int inputNodes = 10;
        int reservoirNodes = 100;
        int outputNodes = 1;
        double sparsity = 0.1;
        String outputFile = "testing/ESN_RNN_" + inputNodes + "_" + reservoirNodes + "_" + outputNodes + ".dgf";

        try {
            generateESN(inputNodes, reservoirNodes, outputNodes, sparsity, outputFile);
            System.out.println("ESN network generated successfully in " + outputFile);
        } catch (IOException e) {
            System.err.println("Error generating ESN network: " + e.getMessage());
        }
    }

    public static void generateESN(int inputNodes, int reservoirNodes, int outputNodes, double reservoirConnectivity, String outputFile) throws IOException {
        Set<String> edges = new HashSet<>();
        Random random = new Random();

        // Input Layer to Reservoir
        for (int input = 1; input <= inputNodes; input++) {
            for (int reservoir = 1; reservoir <= reservoirNodes; reservoir++) {
                edges.add(edgeKey("I_" + input, "R_" + reservoir));
            }
        }

        // Reservoir to Reservoir
        for (int from = 1; from <= reservoirNodes; from++) {
            for (int to = 1; to <= reservoirNodes; to++) {
                if (from != to && random.nextDouble() < reservoirConnectivity) { // No self-loops, sparse connectivity
                    edges.add(edgeKey("R_" + from, "R_" + to));
                }
            }
        }

        // Reservoir to Output Layer
        for (int reservoir = 1; reservoir <= reservoirNodes; reservoir++) {
            for (int output = 1; output <= outputNodes; output++) {
                edges.add(edgeKey("R_" + reservoir, "O_" + output));
            }
        }

        try (FileWriter writer = new FileWriter("graphs/" + outputFile)) {
            int totalNodes = inputNodes + reservoirNodes + outputNodes;
            writer.write("c ESN with input, reservoir, and output layers\n");
            writer.write("c Reservoir Connectivity: " + reservoirConnectivity + "\n");
            writer.write("p digraph " + totalNodes + " " + edges.size() + "\n");
            for (String edge : edges) {
                writer.write(edge + "\n");
            }
        }
    }

    private static String edgeKey(String node1, String node2) {
        return "e " + node1 + " " + node2;
    }
}
