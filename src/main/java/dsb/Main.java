package dsb;

public class Main {
    public static void main(String[] args) {
        final var argParser = new ArgParser();
        argParser.parse(args);

        final var cfg = argParser.hasConfigPath() ?
                Config.fromFile(argParser.getConfigFilePath()) :
                Config.createDefault();

        cfg.report();

        final var graph = new DefaultGraph();
        final var targetExecutor = new DefaultTargetExecutor(argParser);
        final var graphExecutor = new GraphExecutor(graph.getGraph(), targetExecutor);

        graphExecutor.execute(argParser.getTarget());
    }
}
