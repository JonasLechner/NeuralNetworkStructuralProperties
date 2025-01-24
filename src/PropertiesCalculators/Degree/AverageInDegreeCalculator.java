package PropertiesCalculators.Degree;

import PropertiesCalculators.Helper.Edge;
import PropertiesCalculators.Helper.Graph;

public class AverageInDegreeCalculator {
    public static void main(String[] args) {
        String graphName = "BMLP_5_2_3_1.dgf";
        String testFolder = "testing/";
        String thesisFolderFF = "thesis/feedForward/";
        String thesisFolderHopfield = "thesis/hopfield/";
        String thesisFolderRecurrent = "thesis/recurrent/";
        String graphPath = "graphs/" + thesisFolderFF + graphName;
        Graph graph = new Graph(graphPath);
        double totalDegree = 0;

        for (int i = 1; i <= graph.getMaxNode(); i++) {
            int inDegree = 0;
            for (Edge edge: graph.getEdges()) {
                if (edge.getNode2() == i)
                    ++inDegree;
            }
            totalDegree += inDegree;

        }

        System.out.println("Average inDegree of " + graphName + " = " + totalDegree/graph.getMaxNode());
    }
}
