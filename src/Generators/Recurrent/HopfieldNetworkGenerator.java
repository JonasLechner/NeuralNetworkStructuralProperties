package Generators.Recurrent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class HopfieldNetworkGenerator {

    public static void main(String[] args) {
        int numNeurons = 100;
        double connectionProbability = 0.1;
        String outputFile = "testing/Hopfield_" + numNeurons + "_" + (int) (connectionProbability * 100) + "%.dgf";

        try {
            generateHopfieldNetwork(numNeurons, connectionProbability, outputFile);
            System.out.println("Hopfield network generated successfully in " + outputFile);
        } catch (IOException e) {
            System.err.println("Error generating Hopfield network: " + e.getMessage());
        }
    }

    public static void generateHopfieldNetwork(int numNeurons, double connectionProbability, String outputFile) throws IOException {
        Set<String> edges = new HashSet<>();
        Random random = new Random();

        for (int from = 1; from <= numNeurons; from++) {
            for (int to = from + 1; to <= numNeurons; to++) {
                if (random.nextDouble() < connectionProbability) {
                    edges.add(edgeKey(from, to));
                    edges.add(edgeKey(to, from));
                }
            }
        }

        try (FileWriter writer = new FileWriter("graphs/" + outputFile)) {
            writer.write("c Hopfield Network with " + numNeurons + " neurons and connection probability " + connectionProbability + "\n");
            writer.write("p digraph " + numNeurons + " " + edges.size() + "\n");
            for (String edge : edges) {
                writer.write(edge + "\n");
            }
        }
    }

    private static String edgeKey(int node1, int node2) {
        return "e " + node1 + " " + node2;
    }
}
