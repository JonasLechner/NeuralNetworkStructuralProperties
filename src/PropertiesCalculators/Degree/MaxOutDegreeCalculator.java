package PropertiesCalculators.Degree;

import PropertiesCalculators.Helper.Edge;
import PropertiesCalculators.Helper.Graph;

public class MaxOutDegreeCalculator {
    public static void main(String[] args) {
        String graphName = "RegularRotatingEdge10_20_1_60%.dgf";
        String testFolder = "testing/";
        String thesisFolderFF = "thesis/feedForward/";
        String thesisFolderHopfield = "thesis/hopfield/";
        String thesisFolderRecurrent = "thesis/recurrent/";
        String graphPath = "graphs/" + testFolder + graphName;
        Graph graph = new Graph(graphPath);
        int maxOutDegree = -1;
        int lastMaxNode = -1;

        for (int i = 1; i <= graph.getMaxNode(); i++) {
            int outDegree = 0;
            for (Edge edge: graph.getEdges()) {
                if (edge.getNode1() == i)
                    ++outDegree;
            }
            if (outDegree > maxOutDegree) {
                maxOutDegree = outDegree;
                lastMaxNode = i;
            }

        }

        System.out.println("Max outDegree of " + graphName + " = " + maxOutDegree + ", at Node " + lastMaxNode);


    }
}
