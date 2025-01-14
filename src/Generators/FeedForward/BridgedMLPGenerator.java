package Generators.FeedForward;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BridgedMLPGenerator {

    public static void main(String[] args) {
        int inputNodes = 5;
        List<Integer> hiddenLayerSizes = new ArrayList<>();
        hiddenLayerSizes.add(2);
        hiddenLayerSizes.add(3);
        int outputNodes = 1;

        StringBuilder stringBuilder = new StringBuilder("testing/BMLP_" + inputNodes + "_");
        for (Integer size : hiddenLayerSizes) {
            stringBuilder.append(size).append("_");
        }
        stringBuilder.append(outputNodes).append(".dgf");

        String outputFile = stringBuilder.toString();

        try {
            generateMLP(inputNodes, hiddenLayerSizes, outputNodes, outputFile);
            System.out.println("MLP network generated successfully in " + outputFile);
        } catch (IOException e) {
            System.err.println("Error generating MLP: " + e.getMessage());
        }
    }

    public static void generateMLP(int inputNodes, List<Integer> hiddenLayerSizes, int outputNodes, String outputFile) throws IOException {
        Set<String> edges = new HashSet<>();
        int totalNodes = inputNodes + hiddenLayerSizes.stream().mapToInt(Integer::intValue).sum() + outputNodes;

        // Calculate the starting index for each layer
        int[] layerStartIndices = new int[hiddenLayerSizes.size() + 2];
        layerStartIndices[0] = 1; // Input layer starts at 1
        layerStartIndices[1] = inputNodes + 1; // First hidden layer starts after input nodes

        for (int i = 2; i < layerStartIndices.length; i++) {
            layerStartIndices[i] = layerStartIndices[i - 1] + hiddenLayerSizes.get(i - 2);
        }

        // Generate edges from each layer to all subsequent layers
        for (int i = 0; i < layerStartIndices.length - 1; i++) {
            int currentLayerStart = layerStartIndices[i];
            int currentLayerEnd = (i == 0) ? inputNodes : currentLayerStart + hiddenLayerSizes.get(i - 1) - 1;

            for (int j = i + 1; j < layerStartIndices.length; j++) {
                int nextLayerStart = layerStartIndices[j];
                int nextLayerEnd = (j == hiddenLayerSizes.size() + 1) ? totalNodes : nextLayerStart + hiddenLayerSizes.get(j - 1) - 1;

                for (int from = currentLayerStart; from <= currentLayerEnd; from++) {
                    for (int to = nextLayerStart; to <= nextLayerEnd; to++) {
                        edges.add(edgeKey(from, to));
                    }
                }
            }
        }

        try (FileWriter writer = new FileWriter("graphs/" + outputFile)) {
            writer.write("c MLP network with input, hidden, and output layers\n");
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
