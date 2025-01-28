package PropertiesCalculators.Helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Graph {
    private int maxNode;
    private int numEdges;
    private List<Edge> edges = new ArrayList<>();
    private String graphName;

    public Graph(String input) {
        parseName(input);
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("p")) {
                    parseMetaInformation(line);
                } else
                // Ignore comment lines
                if (line.startsWith("c")) {
                    continue;
                } else
                //edge
                if (line.startsWith("e")) {
                    parseEdge(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void parseName(String path) {
        String normalizedPath = path.replace("\\", "/");
        graphName = normalizedPath.substring(normalizedPath.lastIndexOf("/") + 1);
    }

    private void parseMetaInformation(String line) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(line);
        int count = 0;

        while (matcher.find()) {
            if (count == 0)
                maxNode = Integer.parseInt(matcher.group());
            else if (count == 1)
                numEdges = Integer.parseInt(matcher.group());
            ++count;
        }


        if (count != 2)
            throw new RuntimeException("to many or to little matches for numEdges");

    }

    private void parseEdge(String line){
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(line);
        int count = 0;
        int node1 = -1;
        int node2 = -1;

        while (matcher.find()) {
            if (count == 0)
                node1 = Integer.parseInt(matcher.group());
            else if (count == 1)
                node2 = Integer.parseInt(matcher.group());
            ++count;
        }
        edges.add(new Edge(node1, node2));
        if (count != 2)
            throw new RuntimeException("to many or to little matches for numEdges");
    }

    public Graph() {
        throw new RuntimeException("can't use empty constructor");
    }

    public int getNumEdges() {
        return numEdges;
    }

    public void setNumEdges(int numEdges) {
        this.numEdges = numEdges;
    }

    public int getMaxNode() {
        return maxNode;
    }

    public void setMaxNode(int maxNode) {
        this.maxNode = maxNode;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public void calculateMaxInDegree () {
        int maxInDegree = -1;
        int lastMaxNode = -1;

        for (int i = 1; i <= maxNode; i++) {
            int inDegree = 0;
            for (Edge edge: edges) {
                if (edge.getNode2() == i)
                    ++inDegree;
            }
            if (inDegree > maxInDegree) {
                maxInDegree = inDegree;
                lastMaxNode = i;
            }
        }
        System.out.println("Max in-degree of " + graphName + " = " + maxInDegree + ", at Node " + lastMaxNode);

    }

    public void calculateMaxOutDegree () {
        int maxOutDegree = -1;
        int lastMaxNode = -1;

        for (int i = 1; i <= maxNode; i++) {
            int outDegree = 0;
            for (Edge edge: edges) {
                if (edge.getNode1() == i)
                    ++outDegree;
            }
            if (outDegree > maxOutDegree) {
                maxOutDegree = outDegree;
                lastMaxNode = i;
            }
        }

        System.out.println("Max out-degree of " + graphName + " = " + maxOutDegree + ", at Node " + lastMaxNode);
    }

    public void calculateMaxTotalDegree () {
        int maxTotalDegree = -1;
        int lastMaxNode = -1;

        for (int i = 1; i <= maxNode; i++) {
            int totalDegree = 0;
            for (Edge edge: edges) {
                if (edge.getNode1() == i || edge.getNode2() == i)
                    ++totalDegree;
            }
            if (totalDegree > maxTotalDegree) {
                maxTotalDegree = totalDegree;
                lastMaxNode = i;
            }
        }
        System.out.println("Max total degree of " + graphName + " = " + maxTotalDegree + ", at Node " + lastMaxNode);
    }

    public void calculateAverageInOutDegree () {
        double totalDegreeGraph = 0;

        for (int i = 1; i <= maxNode; i++) {
            int inDegree = 0;
            for (Edge edge: edges) {
                if (edge.getNode2() == i) //doesn't matter if we take node 1 or node 2 result for average in- and out-degree is the same
                    ++inDegree;
            }
            totalDegreeGraph += inDegree;
        }
        System.out.println("Average in- and out-degree of " + graphName + " = " + totalDegreeGraph/maxNode);
    }

    public void calculateAverageTotalDegree () {
        double totalDegreeGraph = 0;

        for (int i = 1; i <= maxNode; i++) {
            int inDegree = 0;
            for (Edge edge: edges) {
                if (edge.getNode1() == i || edge.getNode2() == i)
                    ++inDegree;
            }
            totalDegreeGraph += inDegree;

        }

        System.out.println("Average total degree of " + graphName + " = " + totalDegreeGraph/maxNode);
    }

}
