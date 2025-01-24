

import java.io.*;

public class GraphPrinter {
    public static void main(String[] args) {
        String folder = "testing/";
        String graph = "KolmogorovNetwork_3.dgf";
        String dgfFilePath = "graphs/" + folder + graph;
        String dotFilePath = "graph.dot";
        String outputImagePath = "printedGraphs/" + graph.substring(0, graph.length() - 3) + "png";

        try (BufferedReader br = new BufferedReader(new FileReader(dgfFilePath))) {
            StringBuilder dotContent = new StringBuilder("digraph G {\n");
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("c")) {
                    continue;
                }

                if (line.startsWith("e")) {
                    String[] edge = line.substring(2).trim().split("\\s+");
                    if (edge.length == 2) {
                        dotContent.append("  ").append(edge[0]).append(" -> ").append(edge[1]).append(";\n"); 
                    }
                }
            }

            dotContent.append("}");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dotFilePath))) {
                writer.write(dotContent.toString());
            }

            ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", dotFilePath, "-o", outputImagePath);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor();
            File dotFile = new File(dotFilePath);
            dotFile.deleteOnExit();
            System.out.println("Directed graph visualization saved to: " + outputImagePath);

        } catch (IOException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
