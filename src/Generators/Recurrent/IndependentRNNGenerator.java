package Generators.Recurrent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class IndependentRNNGenerator {

    public static void main(String[] args) {
        int inputNodes = 3;
        List<Integer> hiddenLayerSizes = new ArrayList<>();
        hiddenLayerSizes.add(100);
        hiddenLayerSizes.add(100);
        hiddenLayerSizes.add(100);
        hiddenLayerSizes.add(100);
        int outputNodes = 1;

        StringBuilder stringBuilder = new StringBuilder("testing/IndRNN_" + inputNodes +"_");
        for (Integer i: hiddenLayerSizes) {
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
        int currentLayerStart = 1;
        int nextLayerStart = inputNodes + 1;


        // Input Layer to First Hidden Layer
        for (int input = currentLayerStart; input < nextLayerStart; input++) {
            for (int hidden = nextLayerStart; hidden < nextLayerStart + hiddenLayerSizes.get(0); hidden++) {
                edges.add(edgeKey(input, hidden));
            }
        }

        currentLayerStart = nextLayerStart;
        nextLayerStart += hiddenLayerSizes.get(0);

        // Hidden Layers
        for (int layer = 1; layer < hiddenLayerSizes.size(); layer++) {
            for (int from = currentLayerStart; from < currentLayerStart + hiddenLayerSizes.get(layer - 1); from++) {
                for (int to = nextLayerStart; to < nextLayerStart + hiddenLayerSizes.get(layer); to++) {
                    edges.add(edgeKey(from, to));
                }
            }
            currentLayerStart = nextLayerStart;
            nextLayerStart += hiddenLayerSizes.get(layer);
        }

        // Last Hidden Layer to Output Layer
        for (int hidden = currentLayerStart; hidden < currentLayerStart + hiddenLayerSizes.get(hiddenLayerSizes.size() - 1); hidden++) {
            for (int output = nextLayerStart; output < nextLayerStart + outputNodes; output++) {
                edges.add(edgeKey(hidden, output));
            }
        }



        //add self loops
        int totalNodesExceptOutput = inputNodes;
        for (Integer size: hiddenLayerSizes)
            totalNodesExceptOutput += size;

        for (int node = 1 + inputNodes; node <= totalNodesExceptOutput; node++)
            edges.add(edgeKey(node, node));



        try (FileWriter writer = new FileWriter("graphs/" + outputFile)) {
            int totalNodes = inputNodes + hiddenLayerSizes.stream().mapToInt(Integer::intValue).sum() + outputNodes;
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
