package dsb;

import java.io.File;
import java.nio.file.Path;

public class FileSystem {
    public static Path createOutDir(String cwd) {
        final var cwdFile = new File(cwd);
        final var outDir = Path.of(cwdFile.getPath(), "target");
        final var outDirFile = outDir.toFile();

        if (outDirFile.exists()) {
            outDirFile.mkdir();
        }

        return outDir;
    }
}
