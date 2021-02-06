package dsb;

import java.io.File;
import java.io.IOException;

import static dsb.FileSystem.createOutDir;

public class JarAssemblyExecutor implements TargetExecutor {

    private final ArgParser argParser;

    public JarAssemblyExecutor(ArgParser argParser) {
        this.argParser = argParser;
    }

    @Override
    public boolean execute(Target t) {
        final var cwd = System.getProperty("user.dir");
        final var outDir = createOutDir(cwd);
        final var projectName = new File(cwd).getName();
        final var jar = new JarCreator(projectName, outDir);
        try {
            jar.create(argParser.getMainClass());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
