package PropertiesCalculators;

import PropertiesCalculators.Helper.Graph;

public class DegreeCalculator {
    public static void main(String[] args) {
        String graphName = "IndRNN_3_100_100_100_100_1.dgf";

        String testFolder = "testing/";
        String thesisFolderFF = "thesis/feedForward/";
        String thesisFolderRecurrent = "thesis/recurrent/";
        //take the folder where the graph is stored
        String graphPath = "graphs/" + thesisFolderRecurrent + graphName;
        Graph graph = new Graph(graphPath);

        graph.calculateMaxInDegree();
        graph.calculateMaxOutDegree();
        graph.calculateMaxTotalDegree();
        graph.calculateAverageInOutDegree();
        graph.calculateAverageTotalDegree();
    }

}
