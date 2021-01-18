package dsb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

class JarCreator {
    private static final String CLASS_EXT = ".class";
    private static final String JAR_EXT = ".jar";

    private final String name;
    private final Path outputDir;

    JarCreator(String name, Path outputDir) {
        this.name = name;
        this.outputDir = outputDir;
    }

    void create(String mainClass) throws IOException {
        final var manifest = new Manifest();
        final var classFiles = RecursiveFileLocator.locate(outputDir.toString(), CLASS_EXT);
        final var attributes = manifest.getMainAttributes();

        attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        attributes.put(new Attributes.Name("Created-By"), "dsb");

        if (mainClass != null) {
            attributes.put(Attributes.Name.MAIN_CLASS, mainClass);
        }

        try (var fos = new FileOutputStream(outputDir.toString() + File.separator + name + JAR_EXT);
             var jar = new JarOutputStream(fos, manifest)) {

            classFiles.forEach(classFile -> {
                var relPath = classFile.getPath();
                var index = relPath.indexOf(outputDir.toString());
                relPath = relPath.substring(index + outputDir.toString().length() + 1);
                final var entry = new JarEntry(relPath);
                entry.setTime(classFile.lastModified());
                try {
                    jar.putNextEntry(entry);
                    Files.copy(classFile.toPath(), jar);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
