package dsb;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
//        try (var props = Thread.currentThread().getContextClassLoader().getResourceAsStream("dsb.properties")) {
//            new Properties()
//        }

        String cwd = System.getProperty("user.dir");
        final Path outDir = createOutDir(cwd);
        final var sourceLocator = new SourceLocator(cwd);
        sourceLocator.locate();
        final var compiler = new Compiler(sourceLocator, outDir);
        compiler.compile();
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

class SourceLocator {

    private final File rootDir;
    private final List<File> mainSourceFiles = new ArrayList<>();
    private final List<String> mainSourcePrefixes = List.of(
            Path.of("src", "main", "java").toString(),
            Path.of("src", "java").toString()
    );

    SourceLocator(String rootDir) {
        this.rootDir = new File(rootDir);
    }

    void locate() {
        final var javaSources = new ArrayList<File>();
        locateSources(rootDir, javaSources);
        javaSources.stream()
                .filter(f -> mainSourcePrefixes.stream().anyMatch(p -> f.getPath().contains(p)))
                .forEach(mainSourceFiles::add);
    }

    List<File> getMainSources() {
        return mainSourceFiles;
    }

    void locateSources(File root, List<File> javaFilePaths) {
        final var contents = root.listFiles();
        if (contents != null && contents.length >= 1) {
            for (var f : contents) {
                if (f.isFile() && f.getName().endsWith(".java")) {
                    javaFilePaths.add(f);
                } else if (f.isDirectory()) {
                    locateSources(f, javaFilePaths);
                }
            }
        }
    }
}

class Compiler {

    private final JavaCompiler javaCompiler;
    private final SourceLocator locator;
    private final Path outDir;

    public Compiler(SourceLocator locator, Path outDir) {
        this.locator = locator;
        this.outDir = outDir;
        this.javaCompiler = ToolProvider.getSystemJavaCompiler();
        Objects.requireNonNull(javaCompiler, "No system java compiler found.");
    }

    void compile() {
        final var diagCollector = new DiagnosticCollector<JavaFileObject>();

        try (var fileManager = javaCompiler.getStandardFileManager(diagCollector, null, null)) {
            final var javaFiles = fileManager.getJavaFileObjectsFromFiles(locator.getMainSources());
            final var options = List.of(
                    "-d", outDir.toString()
            );
            final var task = javaCompiler.getTask(null, fileManager, diagCollector, options, null, javaFiles);
            final var success = task.call();
            if (!success) {
                diagCollector.getDiagnostics().forEach(d -> {
                    System.out.format("Error on line %d in %s%n", d.getLineNumber(), d.getSource().toUri());
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}