package org.atpfivt.jsyntrax;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class InputArguments {

    private static final Options OPTIONS;
    private static final Map<Option, BiConsumer<InputArguments, String>> OPTIONS_MAP;
    private boolean help;
    private Path input;
    private Path output;
    private Path style;
    private String title;
    private String version;
    private boolean transparent;
    private double scale = 1.0;
    private boolean getDefaultStyle = false;

    static {
        OPTIONS = new Options();
        OPTIONS_MAP = new LinkedHashMap<>();
        OPTIONS_MAP.put(new Option("h", "help", false, "Show this help message and exit"),
                (o, s) -> o.help = true);
        OPTIONS_MAP.put(new Option("i", "input", true, "Diagram spec file"),
                (o, s) -> o.input = Paths.get(s));
        OPTIONS_MAP.put(new Option("o", "output", true, "Output file"),
                (o, s) -> o.output = Paths.get(s));
        OPTIONS_MAP.put(new Option("s", "style", true, "Style config .ini file"),
                (o, s) -> o.style = Paths.get(s));
        OPTIONS_MAP.put(new Option(null, "title", true, "Diagram title"),
                (o, s) -> o.title = s);
        OPTIONS_MAP.put(new Option("t", "transparent", false, "Transparent background"),
                (o, s) -> o.transparent = true);
        OPTIONS_MAP.put(new Option(null, "scale", true, "Scale image"),
                (o, s) -> o.scale = Double.parseDouble(s));
        OPTIONS_MAP.put(new Option("v", "version", false, "Release version"),
                (o, s) -> {
                    o.version = "JSyntrax " + Main.class
                            .getPackage()
                            .getImplementationVersion();
                }
        );
        OPTIONS_MAP.put(new Option(null, "get-style", false, "Create default style .ini"),
                (o, s) -> o.getDefaultStyle = true);
    }

    public InputArguments(String... args) throws ParseException {
        for (Option o : OPTIONS_MAP.keySet()) {
            OPTIONS.addOption(o);
        }
        CommandLine commandLine = new DefaultParser().parse(OPTIONS, args);

        for (Option o : commandLine.getOptions()) {
            OPTIONS_MAP.getOrDefault(o, (x, s) -> {
            }).accept(this, o.getValue());
        }
        //Any argument not associated with a flag is assumed to be the input file name
        if (commandLine.getArgList().size() > 0) {
            this.input = Paths.get(commandLine.getArgList().get(0));
        }
    }

    public static void writeHelp(PrintWriter writer) {
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(writer, 80, "syntrax",
                "Railroad diagram generator.\n\nOptions",
                OPTIONS, 3, 5, null, true);
        writer.flush();
    }

    public boolean isHelp() {
        return help;
    }

    public Path getInput() {
        return input;
    }

    static Path changeExtension(Path p, String ext) {
        String path = p.toString();
        int i = path.lastIndexOf('.');
        if (i >= 0) {
            return Paths.get(path.substring(0, i)
                    + "." + ext);
        } else {
            return Paths.get(path
                    + "." + ext);
        }
    }

    public Path getOutput() {
        if (output == null) {
            output = changeExtension(input, "png");
        } else if ("png".equalsIgnoreCase(output.toString())
                || "svg".equalsIgnoreCase(output.toString())) {
            output = changeExtension(input, output.toString());
        }
        return output;
    }

    public Path getStyle() {
        return style;
    }

    public String getTitle() {
        return title;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public double getScale() {
        return scale;
    }

    public String getVersion() {
        return version;
    }

    public boolean getDefaultStyleProperty() {
        return getDefaultStyle;
    }
}
