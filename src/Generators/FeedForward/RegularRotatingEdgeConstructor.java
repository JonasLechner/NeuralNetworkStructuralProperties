package Generators.FeedForward;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RegularRotatingEdgeConstructor {

    public static void main(String[] args) {
        int inputNodes = 10;
        List<Integer> hiddenLayerSizes = new ArrayList<>();
        hiddenLayerSizes.add(20);
        int outputNodes = 1;
        double sparsity = 0.6;
        StringBuilder stringBuilder = new StringBuilder("testing/RegularRotatingEdge" + inputNodes + "_");
        for (Integer i : hiddenLayerSizes) {
            stringBuilder.append(i).append("_");
        }
        stringBuilder.append(outputNodes).append("_").append((int)(sparsity*100)).append("%.dgf");

        String outputFile = stringBuilder.toString();

        try {
            generateNetwork(inputNodes, hiddenLayerSizes, outputNodes, sparsity, outputFile);
            System.out.println("RegularRotatingEdge network generated successfully in " + outputFile);
        } catch (IOException e) {
            System.err.println("Error generating RegularRotatingEdge network: " + e.getMessage());
        }
    }

    public static void generateNetwork(int inputNodes, List<Integer> hiddenLayerSizes, int outputNodes, double sparsity, String outputFile) throws IOException {
        Set<String> edges = new HashSet<>();
        int currentLayerStart = 1;
        int nextLayerStart = inputNodes + 1;

        int range = (nextLayerStart + hiddenLayerSizes.get(0) - 1) - nextLayerStart + 1;
        System.out.println("range1: " + range);
        List<Integer> indices = generateIndices(range, sparsity);
        // Input Layer to First Hidden Layer
        for (int input = currentLayerStart; input < nextLayerStart; input++) {
            addEdgesWithRotation(input, nextLayerStart, nextLayerStart + hiddenLayerSizes.get(0) - 1, edges, indices, input - currentLayerStart);
        }


        currentLayerStart = nextLayerStart;
        nextLayerStart += hiddenLayerSizes.get(0);

        // Hidden Layers
        for (int layer = 1; layer < hiddenLayerSizes.size(); layer++) {
            range = (nextLayerStart + hiddenLayerSizes.get(layer) - 1) - nextLayerStart + 1;
            System.out.println("range2: " + range);
            indices = generateIndices(range, sparsity);

            for (int from = currentLayerStart; from < currentLayerStart + hiddenLayerSizes.get(layer - 1); from++) {
                addEdgesWithRotation(from, nextLayerStart, nextLayerStart + hiddenLayerSizes.get(layer) - 1, edges, indices, from - currentLayerStart);
            }


            currentLayerStart = nextLayerStart;
            nextLayerStart += hiddenLayerSizes.get(layer);
        }

        range = (nextLayerStart + outputNodes - 1) - nextLayerStart + 1;
        System.out.println("range3: " + range);
        indices = generateIndices(range, sparsity);

        // Last Hidden Layer to Output Layer
        for (int hidden = currentLayerStart; hidden < currentLayerStart + hiddenLayerSizes.get(hiddenLayerSizes.size() - 1); hidden++) {
            addEdgesWithRotation(hidden, nextLayerStart, nextLayerStart + outputNodes - 1, edges, indices, hidden - currentLayerStart);
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
        System.out.println(" rotate by: " + rotationIndex % originalList.size() + " rotated: " + rotatedList);
        return rotatedList;
    }

    private static void addEdgesWithRotation(int fromNode, int start, int end, Set<String> edges, List<Integer> indices, int rotationIndex) {
        indices = rotate(indices, rotationIndex);
        //System.out.println(indices);
        for (int i = 0; i < indices.size(); i++) {
            if (indices.get(i) == 1)
                edges.add(edgeKey(fromNode, start + i));
        }
    }

    private static List<Integer> generateIndices(int range, double sparsity) {
        List<Integer> indices = new ArrayList<>(range);
        Integer[] indicesArr = new Integer[range];
        Arrays.fill(indicesArr, 0);
        int numFilled = (int) Math.ceil(range*sparsity);
        for (int i = 0; i < numFilled; i++) {
            indicesArr[i] = 1;
        }

        indices.addAll(Arrays.asList(indicesArr));
        System.out.println("indicesArr: " + Arrays.toString(indicesArr));
        return indices;
    }
    private static String edgeKey(int node1, int node2) {
        return "e " + node1 + " " + node2;
    }
}
