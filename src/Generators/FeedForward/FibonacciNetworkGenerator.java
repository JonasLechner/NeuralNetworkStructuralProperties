package Generators.FeedForward;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FibonacciNetworkGenerator {

    public static void main(String[] args) {
        int inputNodes = 20;
        List<Integer> hiddenLayerSizes = new ArrayList<>();
        hiddenLayerSizes.add(50);
        int outputNodes = 5;
        List<Integer> kList = new ArrayList<>(); // must contain number of layers - 1 elements
        kList.add(20);
        kList.add(2);
        StringBuilder stringBuilder = new StringBuilder("testing/Fibonacci_" + inputNodes + "_");
        for (Integer i : hiddenLayerSizes) {
            stringBuilder.append(i).append("_");
        }

        stringBuilder.append(outputNodes).append("_");
        stringBuilder.append("kValues");
        for (Integer i : kList) {
            stringBuilder.append("_").append(i);
        }
        stringBuilder.append(".dgf");

        String outputFile = stringBuilder.toString();

        try {
            generateFibonacciNetwork(inputNodes, hiddenLayerSizes, outputNodes, outputFile, kList);
            System.out.println("Fibonacci network generated successfully in " + outputFile);
        } catch (IOException e) {
            System.err.println("Error generating Fibonacci network: " + e.getMessage());
        }
    }

    private static List<Integer> generateKFibonacciNumber (int k) {
        List<Integer> kFibonacciNumbers = new ArrayList<>(k);
        int a = 0, b = 1;
        kFibonacciNumbers.add(1);
        for (int i = 0; i < k - 1; i++) {
            int temp = a + b;
            a = b;
            b = temp;
            kFibonacciNumbers.add(b);
        }

        return kFibonacciNumbers;
    }

    private static List<Integer> generateConnectionList (List<Integer> kFibonacciNumbers, int range) {
        List<Integer> connectionList = new ArrayList<>(range);
        Integer[] indicesArr = new Integer[range];
        Arrays.fill(indicesArr, 0);
        if (kFibonacciNumbers.get(kFibonacciNumbers.size() - 1) < range) { // last fibonacci number is smaller than range
            indicesArr[0] = 1;
            for (int i = 1; i < kFibonacciNumbers.size(); ++i) {
                indicesArr[kFibonacciNumbers.get(i)] = 1;
            }
        } else {
            double normalisationMultiplier = (double)range / kFibonacciNumbers.get(kFibonacciNumbers.size() - 1) - 0.0000000000000001;
            indicesArr[0] = 1;
            for (int i = 1; i < kFibonacciNumbers.size(); ++i) {
                int normalizedValue = (int) Math.floor( kFibonacciNumbers.get(i) * normalisationMultiplier);
                //System.out.println("normalizedValue: " + normalizedValue + "; kFibonacciNumbers.get(i)" +  kFibonacciNumbers.get(i) + "; normalisationMultiplier: " + normalisationMultiplier);
                while (indicesArr[normalizedValue] != 0)
                    normalizedValue += 1;
                indicesArr[normalizedValue] = 1;
            }
        }
        connectionList.addAll(Arrays.asList(indicesArr));
        //System.out.println("range: " + range +"; connectionlist: " + connectionList);

        return connectionList;
    }

    public static void generateFibonacciNetwork(int inputNodes, List<Integer> hiddenLayerSizes, int outputNodes, String outputFile, List<Integer> kList) throws IOException {
        Set<String> edges = new HashSet<>();
        int currentLayerStart = 1;
        int nextLayerStart = inputNodes + 1;
        int kCounter = 0;

        // Input Layer to First Hidden Layer
        for (int input = currentLayerStart; input < nextLayerStart; input++) {
            int end = nextLayerStart + hiddenLayerSizes.get(0) - 1;
            addFibonacciEdgesWithRotation(input, nextLayerStart, end, edges, input - currentLayerStart,
                    generateConnectionList(generateKFibonacciNumber(kList.get(kCounter)), end - nextLayerStart + 1));
        }
        ++kCounter;


        currentLayerStart = nextLayerStart;
        nextLayerStart += hiddenLayerSizes.get(0);


        // Hidden Layers
        for (int layer = 1; layer < hiddenLayerSizes.size(); layer++) {
            int end = nextLayerStart + hiddenLayerSizes.get(layer) - 1;
            for (int from = currentLayerStart; from < currentLayerStart + hiddenLayerSizes.get(layer - 1); from++) {
                addFibonacciEdgesWithRotation(from, nextLayerStart, end, edges, from - currentLayerStart,
                        generateConnectionList(generateKFibonacciNumber(kList.get(kCounter)), end - nextLayerStart + 1));
            }
            ++kCounter;
            currentLayerStart = nextLayerStart;
            nextLayerStart += hiddenLayerSizes.get(layer);
        }

        // Last Hidden Layer to Output Layer
        for (int hidden = currentLayerStart; hidden < currentLayerStart + hiddenLayerSizes.get(hiddenLayerSizes.size() - 1); hidden++) {
            int end = nextLayerStart + outputNodes - 1;
            addFibonacciEdgesWithRotation(hidden, nextLayerStart, end, edges, hidden - currentLayerStart,
                    generateConnectionList(generateKFibonacciNumber(kList.get(kCounter)), end - nextLayerStart + 1));
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

    private static List<Integer> rotate(List<Integer> originalList, int rotationIndex) {
        Integer[] indicesArr = new Integer[originalList.size()];

        for (int i = 0; i < originalList.size(); i++) {
            indicesArr[(i + rotationIndex) % originalList.size()] =  originalList.get(i);
        }

        List<Integer> rotatedList = new ArrayList<>(Arrays.asList(indicesArr));
        //System.out.println(" rotate by: " + rotationIndex % originalList.size() + " rotated: " + rotatedList);
        return rotatedList;
    }

    private static void addFibonacciEdgesWithRotation(int fromNode, int start, int end, Set<String> edges, int rotationIndex, List<Integer> connectionList) {

        List<Integer> fibonacciIndices = rotate(connectionList, rotationIndex);
        for (int i = 0; i < fibonacciIndices.size(); i++) {
            if (fibonacciIndices.get(i) == 1)
                edges.add(edgeKey(fromNode, start + i));
        }
    }

    private static String edgeKey(int node1, int node2) {
        return "e " + node1 + " " + node2;
    }
}
