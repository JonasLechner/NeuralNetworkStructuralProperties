package Generators.FeedForward;

import java.io.FileWriter;
import java.io.IOException;

public class KolmogorovNetworkGenerator {

    public static void main(String[] args) {
        int d = 2; //Number of input nodes
        String outputFile = "testing/KolmogorovNetwork_" + d + ".dgf";

        try {
            generateKolmogorovNetwork(d, outputFile);
            System.out.println("Kolmogorov Neural Network generated successfully in " + outputFile);
        } catch (IOException e) {
            System.err.println("Error generating Kolmogorov Neural Network: " + e.getMessage());
        }
    }

    public static void generateKolmogorovNetwork(int d, String outputFile) throws IOException {
        int numInputNeurons = d;
        int numFirstHiddenNeurons = d * (2 * d + 1);
        int numSecondHiddenNeurons = 2 * d + 1;
        int numOutputNeurons = 1;

        try (FileWriter writer = new FileWriter("graphs/" + outputFile)) {
            writer.write("c Kolmogorov Neural Network with input dimension " + d + "\n");
            writer.write("p digraph " + (numInputNeurons + numFirstHiddenNeurons + numSecondHiddenNeurons + numOutputNeurons) + " " + calculateTotalEdges(d) + "\n");

            // Input to First Hidden Layer connections
            int neuronId = 1;
            for (int input = 1; input <= numInputNeurons; input++) {
                for (int hidden1 = 1; hidden1 <= numFirstHiddenNeurons / numInputNeurons; hidden1++) {
                    writer.write(edgeKey(input, numInputNeurons + neuronId) + "\n");
                    neuronId++;
                }
            }

            // First Hidden Layer to Second Hidden Layer connections
            neuronId = 1;
            for (int hidden1 = 1; hidden1 <= numFirstHiddenNeurons; hidden1++) {
                int targetNeuron = numInputNeurons + numFirstHiddenNeurons + (neuronId % numSecondHiddenNeurons) + 1;
                writer.write(edgeKey(numInputNeurons + hidden1, targetNeuron) + "\n");
                neuronId++;
            }

            // Second Hidden Layer to Output Layer connections
            int outputNeuronId = numInputNeurons + numFirstHiddenNeurons + numSecondHiddenNeurons + 1;
            for (int hidden2 = 1; hidden2 <= numSecondHiddenNeurons; hidden2++) {
                writer.write(edgeKey(numInputNeurons + numFirstHiddenNeurons + hidden2, outputNeuronId) + "\n");
            }
        }
    }

    private static String edgeKey(int from, int to) {
        return "e " + from + " " + to;
    }

    private static int calculateTotalEdges(int d) {
        int numInputNeurons = d;
        int numFirstHiddenNeurons = d * (2 * d + 1);
        int numSecondHiddenNeurons = 2 * d + 1;
        int edgesInputToHidden1 = numInputNeurons * (numFirstHiddenNeurons / numInputNeurons);
        int edgesHidden1ToHidden2 = numFirstHiddenNeurons;
        int edgesHidden2ToOutput = numSecondHiddenNeurons;
        return edgesInputToHidden1 + edgesHidden1ToHidden2 + edgesHidden2ToOutput;
    }
}
