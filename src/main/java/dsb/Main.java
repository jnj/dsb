package dsb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
//        try (var props = Thread.currentThread().getContextClassLoader().getResourceAsStream("dsb.properties")) {
//            new Properties()
//        }

        String cwd = System.getProperty("user.dir");
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
        jar.create("dsb.Main");
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
