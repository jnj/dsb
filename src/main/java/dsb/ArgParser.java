package dsb;

public class ArgParser {

    private String target = TargetName.AssembleJarFile.name();
    private String configFilePath;
    private String mainClass;
    private int argIndex;

    public void parse(String[] args) {
        for (argIndex = 0; argIndex < args.length; argIndex++) {
            var arg = args[argIndex];
            if ("-c".equals(arg)) {
                configFilePath = consumeNext(args);
                failIfNull(configFilePath);
            } else if ("-h".equals(arg)) {
                usage();
                System.exit(0);
            } else if ("-m".equals(arg)) {
                mainClass = consumeNext(args);
                failIfNull(mainClass);
            } else if ("-t".equals(arg)) {
                target = consumeNext(args);
                failIfNull(target);
            }
        }
    }

    private void failIfNull(String s) {
        if (s == null) {
            usage();
            System.exit(1);
        }
    }

    private String consumeNext(String[] s) {
        if (argIndex < s.length - 1) {
            var arg = s[argIndex + 1];
            arg = arg.trim();
            if (arg.isEmpty()) {
                arg = null;
            }
            argIndex++;
            return arg;
        }

        return null;
    }

    private void fail(String msg) {
        System.err.println(msg);
        usage();
        System.exit(1);
    }

    public String getMainClass() {
        return mainClass;
    }

    boolean hasConfigPath() {
        final var cfg = getConfigFilePath();
        return cfg != null && !cfg.isEmpty();
    }

    String getConfigFilePath() {
        return configFilePath;
    }

    String getTarget() {
        return target;
    }

    public void usage() {
        var help = "Command line options:\n" +
                   "---------------------\n" +
                   " -c <config file> pass in a config file to direct the build\n" +
                   " -h print usage\n" +
                   " -m <fully.qualified.Main> pass name of class with main() to get an executable jar\n" +
                   " -t target name (default is " + TargetName.AssembleJarFile + ")";
        System.out.println(help);
    }
}
