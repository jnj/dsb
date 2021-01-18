package dsb;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

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
        final var jar = new JarCreator(new File(cwd).getName(), outDir);
        jar.create();
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

