package PropertiesCalculators.Helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Graph {
    int maxNode;
    int numEdges;
    List<Edge> edges = new ArrayList<>();

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

    public Graph(String input) {
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
}
