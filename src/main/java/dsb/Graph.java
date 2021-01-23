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

    public void addDep(String targetName, String dep) {
        final var t = findOrAddTarget(targetName);
        final var list = deps.computeIfAbsent(t, k -> new ArrayList<>());
        list.add(findOrAddTarget(dep));
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

    private static class Target {
        public final String name;

        Target(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final var target = (Target) o;
            return name.equals(target.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
