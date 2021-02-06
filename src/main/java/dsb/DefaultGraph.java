package dsb;

import java.util.List;

public class DefaultGraph {

    private final Graph graph;

    public DefaultGraph() {
        graph = new Graph();
        graph.addDep(TargetName.CompileTestSources, TargetName.CompileMainSources);
        graph.addDeps(TargetName.AssembleJarFile, List.of(TargetName.CompileMainSources));
    }

    public Graph getGraph() {
        return graph;
    }
}
