package dsb;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class SourceLocator {
    private static final String JAVA_EXT = ".java";

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
        RecursiveFileLocator.locate(rootDir.getPath(), JAVA_EXT)
                .stream()
                .filter(f -> mainSourcePrefixes.stream().anyMatch(p -> f.getPath().contains(p)))
                .forEach(mainSourceFiles::add);
    }

    List<File> getMainSources() {
        return mainSourceFiles;
    }
}
