package PropertiesCalculators.TreeWidth;

import nl.uu.cs.treewidth.algorithm.*;
import nl.uu.cs.treewidth.input.DgfReader;
import nl.uu.cs.treewidth.input.GraphInput;
import nl.uu.cs.treewidth.input.InputException;
import nl.uu.cs.treewidth.ngraph.NGraph;
import nl.uu.cs.treewidth.timing.JavaNanoTime;
import nl.uu.cs.treewidth.timing.Stopwatch;

public class LowerBoundCalculator {
    public static void main(String[] args) {
        System.out.println("LibTW V1.0\r\n");
        System.out.println("This library is free software; you can redistribute it and/or ");
        System.out.println("modify it under the terms of the GNU Lesser General Public");
        System.out.println("License as published by the Free Software Foundation\r\n");
        String graph = "Fibonacci_20_50_5.dgf";
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
        //
        lb = new AllStartMaximumCardinalitySearch();
        stopwatch.reset();
        stopwatch.start();
        lb.setInput(g);
        lb.run();
        stopwatch.stop();
        System.out.println("Lowerbound algorithm  : " + lb.getName());
        System.out.println("Lowerbound            : " + lb.getLowerBound());
        System.out.println("Time needed           : " + stopwatch.getTime() + " ms");
        System.out.println("");
        //
        lb = new AllStartMaximumMinimumDegree();
        stopwatch.reset();
        stopwatch.start();
        lb.setInput(g);
        lb.run();
        stopwatch.stop();
        System.out.println("Lowerbound algorithm  : " + lb.getName());
        System.out.println("Lowerbound            : " + lb.getLowerBound());
        System.out.println("Time needed           : " + stopwatch.getTime() + " ms");
        System.out.println("");
        //
        lb = new AllStartMaximumMinimumDegreePlusLeastC();
        stopwatch.reset();
        stopwatch.start();
        lb.setInput(g);
        lb.run();
        stopwatch.stop();
        System.out.println("Lowerbound algorithm  : " + lb.getName());
        System.out.println("Lowerbound            : " + lb.getLowerBound());
        System.out.println("Time needed           : " + stopwatch.getTime() + " ms");
        System.out.println("");
        //
        lb = new AllStartMinorMinWidth();
        stopwatch.reset();
        stopwatch.start();
        lb.setInput(g);
        lb.run();
        stopwatch.stop();
        System.out.println("Lowerbound algorithm  : " + lb.getName());
        System.out.println("Lowerbound            : " + lb.getLowerBound());
        System.out.println("Time needed           : " + stopwatch.getTime() + " ms");
        System.out.println("");
        //
        lb = new MaximumCardinalitySearch();
        stopwatch.reset();
        stopwatch.start();
        lb.setInput(g);
        lb.run();
        stopwatch.stop();
        System.out.println("Lowerbound algorithm  : " + lb.getName());
        System.out.println("Lowerbound            : " + lb.getLowerBound());
        System.out.println("Time needed           : " + stopwatch.getTime() + " ms");
        System.out.println("");
        //
        lb = new MaximumMinimumDegree();
        stopwatch.reset();
        stopwatch.start();
        lb.setInput(g);
        lb.run();
        stopwatch.stop();
        System.out.println("Lowerbound algorithm  : " + lb.getName());
        System.out.println("Lowerbound            : " + lb.getLowerBound());
        System.out.println("Time needed           : " + stopwatch.getTime() + " ms");
        System.out.println("");
        //
        lb = new MaximumMinimumDegreePlusLeastC();
        stopwatch.reset();
        stopwatch.start();
        lb.setInput(g);
        lb.run();
        stopwatch.stop();
        System.out.println("Lowerbound algorithm  : " + lb.getName());
        System.out.println("Lowerbound            : " + lb.getLowerBound());
        System.out.println("Time needed           : " + stopwatch.getTime() + " ms");
        System.out.println("");
        //
        lb = new MaximumMinimumDegreePlusMaxD();
        stopwatch.reset();
        stopwatch.start();
        lb.setInput(g);
        lb.run();
        stopwatch.stop();
        System.out.println("Lowerbound algorithm  : " + lb.getName());
        System.out.println("Lowerbound            : " + lb.getLowerBound());
        System.out.println("Time needed           : " + stopwatch.getTime() + " ms");
        System.out.println("");
        //
        lb = new MaximumMinimumDegreePlusMinD();
        stopwatch.reset();
        stopwatch.start();
        lb.setInput(g);
        lb.run();
        stopwatch.stop();
        System.out.println("Lowerbound algorithm  : " + lb.getName());
        System.out.println("Lowerbound            : " + lb.getLowerBound());
        System.out.println("Time needed           : " + stopwatch.getTime() + " ms");
        System.out.println("");
        //
        lb = new MinorMinWidth();
        stopwatch.reset();
        stopwatch.start();
        lb.setInput(g);
        lb.run();
        stopwatch.stop();
        System.out.println("Lowerbound algorithm  : " + lb.getName());
        System.out.println("Lowerbound            : " + lb.getLowerBound());
        System.out.println("Time needed           : " + stopwatch.getTime() + " ms");
        System.out.println("");
        //
        lb = new Ramachandramurthi();
        stopwatch.reset();
        stopwatch.start();
        lb.setInput(g);
        lb.run();
        stopwatch.stop();
        System.out.println("Lowerbound algorithm  : " + lb.getName());
        System.out.println("Lowerbound            : " + lb.getLowerBound());
        System.out.println("Time needed           : " + stopwatch.getTime() + " ms");
        System.out.println("");


    }
}
