package PropertiesCalculators.TreeWidth;

import nl.uu.cs.treewidth.algorithm.QuickBB;
import nl.uu.cs.treewidth.input.DgfReader;
import nl.uu.cs.treewidth.input.GraphInput;
import nl.uu.cs.treewidth.input.InputException;
import nl.uu.cs.treewidth.ngraph.NGraph;
import nl.uu.cs.treewidth.timing.JavaNanoTime;
import nl.uu.cs.treewidth.timing.Stopwatch;

public class QuickBBCalculator {
    public static void main(String[] args) {
        System.out.println("LibTW V1.0\r\n");
        System.out.println("This library is free software; you can redistribute it and/or ");
        System.out.println("modify it under the terms of the GNU Lesser General Public");
        System.out.println("License as published by the Free Software Foundation\r\n");
        String graph = "Hopfield_100_10%.dgf";
        String folder = "testing/";
        GraphInput in = new DgfReader("graphs/" + folder + graph);

        NGraph g;
        try {
            g = in.get();
        } catch (InputException var11) {
            System.out.println("There was an error opening this file.");
            var11.printStackTrace();
            return;
        }

        System.out.println("  Graph               : " + graph);
        System.out.println("# Vertices in graph   : " + g.getNumberOfVertices());
        System.out.println("# Edges in graph      : " + g.getNumberOfEdges());
        System.out.println("");
        Stopwatch stopwatch = new Stopwatch(new JavaNanoTime());
        QuickBB<GraphInput.InputData> qbb = new QuickBB();
        stopwatch.reset();
        stopwatch.start();
        qbb.setInput(g);
        qbb.run();
        stopwatch.stop();
        System.out.println("Exact algorithm : " + qbb.getName());
        System.out.println("Treewidth       : " + qbb.getUpperBound());
        System.out.println("Time needed     : " + stopwatch.getTime() + " ms");
        System.out.println();
        System.out.println("Done.");

    }
}
