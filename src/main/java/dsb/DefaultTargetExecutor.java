package dsb;

public class DefaultTargetExecutor implements TargetExecutor {

    private final ArgParser args;

    DefaultTargetExecutor(ArgParser args) {
        this.args = args;
    }

    @Override
    public boolean execute(Target target) {
        System.out.println("executing " + target);

        if (TargetName.CompileMainSources.name().equals(target.name)) {
            final var subExec = new CompileMainExecutor();
            return subExec.execute(target);
        } else if (TargetName.AssembleJarFile.name().equals(target.name)) {
            final var subExec = new JarAssemblyExecutor(args);
            return subExec.execute(target);
        }

        return true;
    }
}
