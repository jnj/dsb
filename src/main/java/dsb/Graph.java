package dsb;

import java.util.*;

public class Graph {
    private String defaultName;
    private final Map<Target, List<Target>> deps = new HashMap<>();
    private final Map<String, Target> byName = new HashMap<>();

    public void setDefault(String name) {
        defaultName = name;
        findOrAddTarget(defaultName);
    }

    public void addDeps(TargetName tgt, Collection<TargetName> deps) {
        deps.forEach(d -> addDep(tgt, d));
    }

    public void addDep(TargetName name, TargetName dep) {
        addDep(name.name(), dep.name());
    }

    public void addDep(String targetName, String dep) {
        final var t = findOrAddTarget(targetName);
        final var list = deps.computeIfAbsent(t, k -> new ArrayList<>());
        list.add(findOrAddTarget(dep));
    }

    /**
     * Returns this target's dependencies, i.e. targets that need
     * to be satisfied first.
     */
    public Collection<Target> getDependencies(Target target) {
        return new ArrayList<>(deps.computeIfAbsent(target, t -> new ArrayList<>()));
    }

    private Target findOrAddTarget(String name) {
        final var target = byName.computeIfAbsent(name, Target::new);
        deps.computeIfAbsent(target, k -> new ArrayList<>());
        return target;
    }

    @Override
    public String toString() {
        return "Graph{" + deps + "}";
    }

}
