package dsb;

import java.util.HashSet;
import java.util.Set;

public class GraphExecutor {
    private final Graph graph;
    private final DefaultTargetExecutor targetExecutor;

    public GraphExecutor(Graph graph, DefaultTargetExecutor targetExecutor) {
        this.graph = graph;
        this.targetExecutor = targetExecutor;
    }

    public void execute(String targetName) {
        final var completed = new HashSet<Target>();
        execute(targetName, completed);
    }

    private void execute(String targetName, Set<Target> completed) {
        final var target = new Target(targetName);

        if (!completed.contains(target)) {
            final var deps = graph.getDependencies(target);
            deps.forEach(dep -> execute(dep.name, completed));

            if (completed.containsAll(deps) && targetExecutor.execute(target)) {
                completed.add(target);
            }
        }
    }
}
