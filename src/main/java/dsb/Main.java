package dsb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        final var argParser = new ArgParser();
        argParser.parse(args);

        final var cfg = argParser.hasConfigPath() ?
                Config.fromFile(argParser.getConfigFilePath()) :
                Config.createDefault();

        cfg.report();

        final var cwd = System.getProperty("user.dir");
        final Path outDir = createOutDir(cwd);
        final var sourceLocator = new SourceLocator(cwd);

        sourceLocator.locateOr(() -> {
            System.err.println("No sources found.");
            System.exit(0);
        });

        final var compiler = new Compiler(sourceLocator.getMainSources(), outDir);
        compiler.compile();
        final var projectName = new File(cwd).getName();
        final var jar = new JarCreator(projectName, outDir);
        jar.create(argParser.getMainClass());
    }

    private static Path createOutDir(String cwd) {
        final var cwdFile = new File(cwd);
        final var outDir = Path.of(cwdFile.getPath(), "target");
        final var outDirFile = outDir.toFile();
        if (outDirFile.exists()) {
            outDirFile.mkdir();
        }
        return outDir;
    }
}
