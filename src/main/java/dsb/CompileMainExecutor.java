package dsb;

import java.nio.file.Path;

import static dsb.FileSystem.createOutDir;

public class CompileMainExecutor implements TargetExecutor {

    CompileMainExecutor() {
    }

    @Override
    public boolean execute(Target t) {
        final var cwd = System.getProperty("user.dir");
        final Path outDir = createOutDir(cwd);
        final var sourceLocator = new SourceLocator(cwd);

        final var b = new boolean[1];
        b[0] = true;

        sourceLocator.locateOr(() -> {
            System.err.println("No sources found.");
            b[0] = false;
        });

        if (b[0]) {
            final var compiler = new Compiler(sourceLocator.getMainSources(), outDir);
            compiler.compile();
        }

        return b[0];
    }
}
