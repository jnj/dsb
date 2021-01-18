package dsb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class RecursiveFileLocator {

    public static List<File> locate(String rootDir, String fileSuffix) {
        final var results = new ArrayList<File>();
        locate(rootDir, fileSuffix, results);
        return results;
    }

    public static void locate(String rootDir, String fileSuffix, List<File> results) {
        final var rootFile = new File(rootDir);
        if (rootFile.exists()) {
            final var contents = rootFile.listFiles();
            if (contents != null) {
                for (var f : contents) {
                    if (f.isFile() && f.getName().endsWith(fileSuffix)) {
                        results.add(f);
                    } else if (f.isDirectory()) {
                        locate(f.getPath(), fileSuffix, results);
                    }
                }
            }
        }
    }
}
