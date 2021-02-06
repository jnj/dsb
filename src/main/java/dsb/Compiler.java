package dsb;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

class Compiler {

    private final JavaCompiler javaCompiler;
    private final Iterable<File> sources;
    private final Path outDir;

    public Compiler(Iterable<File> sources, Path outDir) {
        this.sources = sources;
        this.outDir = outDir;
        this.javaCompiler = ToolProvider.getSystemJavaCompiler();
        Objects.requireNonNull(javaCompiler, "No system java compiler found.");
    }

    void compile() {
        System.out.println("Compiling " + sources);
        final var diagCollector = new DiagnosticCollector<JavaFileObject>();

        try (var fileManager = javaCompiler.getStandardFileManager(diagCollector, null, null)) {
            final var javaFiles = fileManager.getJavaFileObjectsFromFiles(sources);
            final var options = List.of(
                    "-d", outDir.toString(),
                    "-g"
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
