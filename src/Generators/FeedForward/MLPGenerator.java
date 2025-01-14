package Generators.FeedForward;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MLPGenerator {
    public static void main(String[] args) {
        int inputNodes = 2;
        List<Integer> hiddenLayerSizes = new ArrayList<>();
        hiddenLayerSizes.add(10);
        hiddenLayerSizes.add(5);
        int outputNodes = 1;
        StringBuilder stringBuilder = new StringBuilder("testing/MLP_" + inputNodes + "_");
        for (Integer i : hiddenLayerSizes) {
            stringBuilder.append(i).append("_");
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

        // Input Layer to First Hidden Layer
        for (int input = 1; input <= inputNodes; input++) {
            for (int hidden = 1; hidden <= hiddenLayerSizes.get(0); hidden++) {
                edges.add(edgeKey("I_" + input, "H1_" + hidden));
            }
        }

        // Hidden Layers
        for (int layer = 1; layer < hiddenLayerSizes.size(); layer++) {
            for (int from = 1; from <= hiddenLayerSizes.get(layer - 1); from++) {
                for (int to = 1; to <= hiddenLayerSizes.get(layer); to++) {
                    edges.add(edgeKey("H" + layer + "_" + from, "H" + (layer + 1) + "_" + to));
                }
            }
        }

        // Last Hidden Layer to Output Layer
        for (int hidden = 1; hidden <= hiddenLayerSizes.get(hiddenLayerSizes.size() - 1); hidden++) {
            for (int output = 1; output <= outputNodes; output++) {
                edges.add(edgeKey("H" + hiddenLayerSizes.size() + "_" + hidden, "O_" + output));
            }
        }

        try (FileWriter writer = new FileWriter("graphs/" + outputFile)) {
            int totalNodes = inputNodes + hiddenLayerSizes.stream().mapToInt(Integer::intValue).sum() + outputNodes;
            writer.write("c MLP network with input, hidden, and output layers\n");
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
