package PropertiesCalculators.TreeWidth;

import nl.uu.cs.treewidth.algorithm.GreedyDegree;
import nl.uu.cs.treewidth.algorithm.LexBFS;
import nl.uu.cs.treewidth.algorithm.LowerBound;
import nl.uu.cs.treewidth.algorithm.MinDegree;
import nl.uu.cs.treewidth.algorithm.Permutation;
import nl.uu.cs.treewidth.algorithm.PermutationToTreeDecomposition;
import nl.uu.cs.treewidth.algorithm.QuickBB;
import nl.uu.cs.treewidth.algorithm.TreewidthDP;
import nl.uu.cs.treewidth.algorithm.UpperBound;
import nl.uu.cs.treewidth.input.DgfReader;
import nl.uu.cs.treewidth.input.GraphInput;
import nl.uu.cs.treewidth.input.InputException;
import nl.uu.cs.treewidth.ngraph.NGraph;
import nl.uu.cs.treewidth.timing.JavaNanoTime;
import nl.uu.cs.treewidth.timing.Stopwatch;

public class TreeWidthCalculator {
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
        Permutation<GraphInput.InputData> p = new LexBFS();
        PermutationToTreeDecomposition<GraphInput.InputData> pttd = new PermutationToTreeDecomposition(p);
        stopwatch.reset();
        stopwatch.start();
        pttd.setInput(g);
        pttd.run();
        stopwatch.stop();
        System.out.println("Permutation algorithm : " + p.getName());
        System.out.println("Upperbound            : " + pttd.getUpperBound());
        System.out.println("Time needed           : " + stopwatch.getTime() + " ms");
        System.out.println("");
        UpperBound<GraphInput.InputData> ub = new GreedyDegree();
        stopwatch.reset();
        stopwatch.start();
        ub.setInput(g);
        ub.run();
        stopwatch.stop();
        System.out.println("Upperbound algorithm  : " + ub.getName());
        System.out.println("Upperbound            : " + ub.getUpperBound());
        System.out.println("Time needed           : " + stopwatch.getTime() + " ms");
        System.out.println("");
        LowerBound<GraphInput.InputData> lb = new MinDegree();
        stopwatch.reset();
        stopwatch.start();
        lb.setInput(g);
        lb.run();
        stopwatch.stop();
        System.out.println("Lowerbound algorithm  : " + lb.getName());
        System.out.println("Lowerbound            : " + lb.getLowerBound());
        System.out.println("Time needed           : " + stopwatch.getTime() + " ms");
        System.out.println("");
        QuickBB<GraphInput.InputData> qbb = new QuickBB();
        stopwatch.reset();
        stopwatch.start();
        qbb.setInput(g);
        qbb.run();
        stopwatch.stop();
        System.out.println("Exact algorithm : " + qbb.getName());
        System.out.println("Treewidth       : " + qbb.getUpperBound());
        System.out.println("Time needed     : " + stopwatch.getTime() + " ms");
        System.out.println("");
        TreewidthDP<GraphInput.InputData> twdp = new TreewidthDP(ub.getUpperBound());
        stopwatch.reset();
        stopwatch.start();
        twdp.setInput(g);
        twdp.run();
        stopwatch.stop();
        System.out.println("Exact algorithm : " + twdp.getName());
        System.out.println("Treewidth       : " + twdp.getTreewidth());
        System.out.println("Time needed     : " + stopwatch.getTime() + " ms");
        System.out.println("");
        System.out.println("Done.");



    }
}