package Generators.FeedForward;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FibonacciNetworkGenerator {

    public static void main(String[] args) {
        int inputNodes = 10;
        List<Integer> hiddenLayerSizes = new ArrayList<>();
        hiddenLayerSizes.add(100);
        int outputNodes = 5;
        StringBuilder stringBuilder = new StringBuilder("testing/Fibonacci_" + inputNodes + "_");
        for (Integer i : hiddenLayerSizes) {
            stringBuilder.append(i).append("_");
        }
        stringBuilder.append(outputNodes).append(".dgf");

        String outputFile = stringBuilder.toString();

        try {
            generateFibonacciNetwork(inputNodes, hiddenLayerSizes, outputNodes, outputFile);
            System.out.println("Fibonacci network generated successfully in " + outputFile);
        } catch (IOException e) {
            System.err.println("Error generating Fibonacci network: " + e.getMessage());
        }
    }

    public static void generateFibonacciNetwork(int inputNodes, List<Integer> hiddenLayerSizes, int outputNodes, String outputFile) throws IOException {
        Set<String> edges = new HashSet<>();
        int currentLayerStart = 1;
        int nextLayerStart = inputNodes + 1;

        // Input Layer to First Hidden Layer
        for (int input = currentLayerStart; input < nextLayerStart; input++) {
            addFibonacciEdgesWithRotation(input, nextLayerStart, nextLayerStart + hiddenLayerSizes.get(0) - 1, edges);
        }


        currentLayerStart = nextLayerStart;
        nextLayerStart += hiddenLayerSizes.get(0);

        // Hidden Layers
        for (int layer = 1; layer < hiddenLayerSizes.size(); layer++) {
            for (int from = currentLayerStart; from < currentLayerStart + hiddenLayerSizes.get(layer - 1); from++) {
                addFibonacciEdgesWithRotation(from, nextLayerStart, nextLayerStart + hiddenLayerSizes.get(layer) - 1, edges);
            }


            currentLayerStart = nextLayerStart;
            nextLayerStart += hiddenLayerSizes.get(layer);
        }

        // Last Hidden Layer to Output Layer
        for (int hidden = currentLayerStart; hidden < currentLayerStart + hiddenLayerSizes.get(hiddenLayerSizes.size() - 1); hidden++) {
            addFibonacciEdgesWithRotation(hidden, nextLayerStart, nextLayerStart + outputNodes - 1, edges);
        }


        try (FileWriter writer = new FileWriter("graphs/" + outputFile)) {
            int totalNodes = inputNodes + hiddenLayerSizes.stream().mapToInt(Integer::intValue).sum() + outputNodes;
            writer.write("c Fibonacci network with input, hidden, and output layers with rotations\n");
            writer.write("p digraph " + totalNodes + " " + edges.size() + "\n");
            for (String edge : edges) {
                writer.write(edge + "\n");
            }
        }
    }

    private static List<Integer> rotate(List<Integer> originalList) {
        List<Integer> rotatedList = new ArrayList<>();
        Random r = new Random();
        int randomStart = r.nextInt(originalList.size());
        for (int i = randomStart; i < originalList.size(); i++) { //fill from random starting point
            rotatedList.add(originalList.get(i));
        }

        for (int i = 0; i < randomStart; i++) {
            rotatedList.add(originalList.get(i));
        }

        return rotatedList;
    }

    private static void addFibonacciEdgesWithRotation(int fromNode, int start, int end, Set<String> edges) {
        System.out.println("from: " + fromNode + "; start: " + start + "; end: " + end);
        int range = end - start + 1;
        List<Integer> fibonacciIndices = rotate(generateFibonacciIndices(range));
        System.out.println(fibonacciIndices);
        for (int i = 0; i < fibonacciIndices.size(); i++) {
            if (fibonacciIndices.get(i) == 1)
                edges.add(edgeKey(fromNode, start + i));
        }
    }

    private static List<Integer> generateFibonacciIndices(int range) {
        List<Integer> indices = new ArrayList<>(range);
        Integer[] indicesArr = new Integer[range];
        Arrays.fill(indicesArr, 0);

        int a = 0, b = 1;
        while (b + a <= range) {
            int temp = a + b;
            a = b;
            b = temp;
            indicesArr[b-1] = 1;
        }

        indices.addAll(Arrays.asList(indicesArr));

        return indices;
    }
    private static String edgeKey(int node1, int node2) {
        return "e " + node1 + " " + node2;
    }
}
