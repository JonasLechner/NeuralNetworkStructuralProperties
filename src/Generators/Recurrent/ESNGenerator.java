package Generators.Recurrent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ESNGenerator {
    public static void main(String[] args) {
        int inputNodes = 5;
        int reservoirNodes = 100;
        int outputNodes = 1;
        double sparsity = 0.01;
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
        int startReservoir = inputNodes + 1;
        int endReservoir = reservoirNodes + inputNodes;
        int endOutputNodes = reservoirNodes + inputNodes + outputNodes;

        // Input Layer to Reservoir
        for (int input = 1; input <= inputNodes; input++) {
            for (int reservoir = startReservoir; reservoir <= endReservoir; reservoir++) {
                edges.add(edgeKey(input, reservoir));
            }
        }


        // Reservoir to Reservoir
        for (int from = startReservoir; from <= endReservoir; from++) {
            for (int to = startReservoir; to <= endReservoir; to++) {
                if (from != to && random.nextDouble() < reservoirConnectivity) {
                    edges.add(edgeKey(from, to));
                }
            }
        }

        // Reservoir to Output Layer
        for (int reservoir = startReservoir; reservoir <= endReservoir; reservoir++) {
            for (int output = endReservoir + 1 ; output <= endOutputNodes; output++) {
                edges.add(edgeKey(reservoir, output));
            }
        }

        // Output to Reservoir Layer
        for (int reservoir = startReservoir; reservoir <= endReservoir; reservoir++) {
            for (int output = endReservoir + 1 ; output <= endOutputNodes; output++) {
                if (random.nextDouble() < reservoirConnectivity)
                    edges.add(edgeKey(output, reservoir));
            }
        }

        // self loops
        for (int node = startReservoir; node <= endReservoir; node++) {
            if (random.nextDouble() < reservoirConnectivity) {
                edges.add(edgeKey(node, node));
                System.out.println("self loop added for node: " + node);
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

    private static String edgeKey(int node1, int node2) {
        return "e " + node1 + " " + node2;
    }
}
