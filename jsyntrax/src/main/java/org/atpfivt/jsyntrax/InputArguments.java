package org.atpfivt.jsyntrax;

import groovyjarjarcommonscli.*;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

public class InputArguments {

    private static final Options options;
    private static final Map<Option, BiConsumer<InputArguments, String>> optionsMap;
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
        options = new Options();
        optionsMap = new LinkedHashMap<>();
        optionsMap.put(new Option("h", "help", false, "Show this help message and exit"),
                (o, s) -> o.help = true);
        optionsMap.put(new Option("i", "input", true, "Diagram spec file"),
                (o, s) -> o.input = Paths.get(s));
        optionsMap.put(new Option("o", "output", true, "Output file"),
                (o, s) -> o.output = Paths.get(s));
        optionsMap.put(new Option("s", "style", true, "Style config .ini file"),
                (o, s) -> o.style = Paths.get(s));
        optionsMap.put(new Option(null, "title", true, "Diagram title"),
                (o, s) -> o.title = s);
        optionsMap.put(new Option("t", "transparent", false, "Transparent background"),
                (o, s) -> o.transparent = true);
        optionsMap.put(new Option(null, "scale", true, "Scale image"),
                (o, s) -> o.scale = Double.parseDouble(s));
        optionsMap.put(new Option("v", "version", false, "JSyntrax version"),
                (o, s) -> o.version = "JSyntrax " + Main.class
                        .getPackage()
                        .getImplementationVersion());
        optionsMap.put(new Option(null, "get-style", false, "Create default style .ini"),
                (o, s) -> o.getDefaultStyle = true);
    }

    InputArguments(String... args) throws ParseException {
        for (Option o : optionsMap.keySet()) {
            options.addOption(o);
        }
        CommandLine commandLine = new PosixParser().parse(options, args);

        for (Option o : commandLine.getOptions()) {
            optionsMap.getOrDefault(o, (x, s) -> {
            }).accept(this, o.getValue());
        }
        //Any argument not associated with a flag is assumed to be the input file name
        if (commandLine.getArgList().size() > 0) {
            this.input = Paths.get(commandLine.getArgList().get(0).toString());
        }
    }

    public static void writeHelp(PrintWriter writer) {
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(writer, 80, "syntrax",
                "Railroad diagram generator.\n\nOptions",
                options, 3, 5, null, true);
        writer.flush();
    }

    public boolean isHelp() {
        return help;
    }

    public Path getInput() {
        return input;
    }

    public Path getOutput() {
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
