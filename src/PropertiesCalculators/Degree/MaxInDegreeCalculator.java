package PropertiesCalculators.Degree;

import PropertiesCalculators.Helper.Edge;
import PropertiesCalculators.Helper.Graph;

// just works if the nodes are sequentially from 1-n
// fix to work generally would be to use a set and store all unique node names
public class MaxInDegreeCalculator {
    public static void main(String[] args) {
        String graphName = "RegularRotatingEdge10_20_1_60%.dgf";
        String testFolder = "testing/";
        String thesisFolderFF = "thesis/feedForward/";
        String thesisFolderRecurrent = "thesis/recurrent/";
        String graphPath = "graphs/" + testFolder + graphName;
        Graph graph = new Graph(graphPath);
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
}
