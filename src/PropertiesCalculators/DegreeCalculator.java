package PropertiesCalculators;

import PropertiesCalculators.Helper.Edge;
import PropertiesCalculators.Helper.Graph;

public class DegreeCalculator {
    public static void maxInDegree(Graph graph, String graphName) {
        int maxInDegree = -1;
        int lastMaxNode = -1;

        for (int i = 1; i <= graph.getMaxNode(); i++) {
            int inDegree = 0;
            for (Edge edge: graph.getEdges()) {
                if (edge.getNode2() == i)
                    ++inDegree;
            }
            if (inDegree > maxInDegree) {
                maxInDegree = inDegree;
                lastMaxNode = i;
            }

        }

        System.out.println("Max inDegree of " + graphName + " = " + maxInDegree + ", at Node " + lastMaxNode);

    }

    public static void maxOutDegree(Graph graph, String graphName) {
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

    public static void averageInAndOutDegree(Graph graph, String graphName) {


        System.out.println("Average out- or inDegree of " + graphName + " = " + ((double)graph.getNumEdges())/graph.getMaxNode());

    }

    public static void main(String[] args) {
        String graphName = "BMLP_5_2_3_1.dgf";
        String testFolder = "testing/";
        String thesisFolderFF = "thesis/feedForward/";
        String thesisFolderHopfield = "thesis/hopfield/";
        String thesisFolderRecurrent = "thesis/recurrent/";
        String graphPath = "graphs/" + thesisFolderFF + graphName;
        Graph graph = new Graph(graphPath);

        maxInDegree(graph, graphName);
        maxOutDegree(graph, graphName);
        averageInAndOutDegree(graph, graphName);

    }

}
