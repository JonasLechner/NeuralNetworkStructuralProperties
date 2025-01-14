package Generators.Recurrent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BiRNNGenerator {

    public static void main(String[] args) {
        int inputNodes = 300;
        int numLayers = 2;
        int nodesPerLayer = 128;
        int outputNodes = 1;

        StringBuilder outputFile = new StringBuilder("testing/BRNN_" + inputNodes + "_");
        outputFile.append((nodesPerLayer + "_").repeat(numLayers));
        outputFile.append(outputNodes).append(".dgf");

        try {
            generateBRNN(inputNodes, nodesPerLayer, outputNodes, numLayers, outputFile.toString());
            System.out.println("BRNN network generated successfully in " + outputFile);
        } catch (IOException e) {
            System.err.println("Error generating BRNN: " + e.getMessage());
        }
    }

    public static void generateBRNN(int inputNodes, int hiddenNodes, int outputNodes, int layers, String outputFile) throws IOException {
        Set<String> edges = new HashSet<>();
        int nodeCounter = 1;
        int[] layerStart = new int[layers + 2]; // input, hidden layers, output

        // Input layer
        layerStart[0] = nodeCounter;
        nodeCounter += inputNodes;

        // Hidden layers
        for (int i = 1; i <= layers; i++) {
            layerStart[i] = nodeCounter;
            nodeCounter += hiddenNodes;
        }

        // Output layer
        layerStart[layers + 1] = nodeCounter;
        nodeCounter += outputNodes;

        // Forward connections
        for (int i = 0; i <= layers; i++) {
            for (int from = layerStart[i]; from < layerStart[i] + ((i == 0) ? inputNodes : hiddenNodes); from++) {
                for (int to = layerStart[i + 1]; to < layerStart[i + 1] + ((i == layers) ? outputNodes : hiddenNodes); to++) {
                    edges.add(edgeKey(from, to));
                }
            }
        }

        // Backward connections
        for (int i = layers; i > 0; i--) {
            for (int from = layerStart[i]; from < layerStart[i] + hiddenNodes; from++) {
                for (int to = layerStart[i - 1]; to < layerStart[i - 1] + ((i - 1 == 0) ? inputNodes : hiddenNodes); to++) {
                    edges.add(edgeKey(from, to));
                }
            }
        }

        try (FileWriter writer = new FileWriter("graphs/" + outputFile)) {
            int totalNodes = nodeCounter - 1;
            writer.write("c BRNN network with input, hidden, and output layers\n");
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
