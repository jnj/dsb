package dsb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class Config {

    private final Properties props;
    private final Graph graph;

    public Config(Properties props) {
        this.props = props;
        this.graph = buildGraph();
    }

    public Graph getGraph() {
        return graph;
    }

    private Graph buildGraph() {
        final var graph = new Graph();
        final var defaultTarget = Target.forConfigValue(props.getProperty("defaultTarget"));
        graph.setDefault(defaultTarget.name);
        return graph;
    }

    static Config fromFile(String path) {
        final var props = new Properties();

        try (var fis = new FileInputStream(path)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Error loading config file: " + path, e);
        }

        return new Config(props);
    }

    static Config createDefault() {
        final var props = new Properties();
        final var cwd = System.getProperty("user.dir");
        final var defaultTgt = new File(cwd).getName();
        final var tgtPath = Path.of("target", defaultTgt + ".jar");
        props.setProperty("defaultTarget", tgtPath.toString());
        return new Config(props);
    }

    void report() {
        System.out.println("configuration loaded: " + graph);
    }

    static abstract class Target {
        public static Target forConfigValue(String value) {
            if (value.endsWith(".jar")) {
                return new JarTarget(value);
            }
            throw new UnsupportedOperationException("only jar targets supported currently");
        }

        public final String name;

        Target(String name) {
            this.name = name;
        }
    }

    static class JarTarget extends Target {
        public final String fullPath;

        JarTarget(String fullPath) {
            super(cleanName(fullPath));
            this.fullPath = fullPath;
        }

        static String cleanName(String fullPath) {
            //todo
            return fullPath;
        }
    }
}
