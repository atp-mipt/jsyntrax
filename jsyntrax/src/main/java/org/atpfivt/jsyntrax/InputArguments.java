package org.atpfivt.jsyntrax;

import groovyjarjarcommonscli.*;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InputArguments {

    InputArguments() {}
    public InputArguments(String i, String  o, boolean h, String t, boolean r, double s){
        this.input = Paths.get(i);
        this.output = Paths.get(o);
        this.help = h;
        this.title = t;
        this.transparent = r;
        this.scale = s;
    }

    public static InputArguments parseArgs(String[] args) throws ParseException, NumberFormatException {
        // TODO: add style
        InputArguments inputArguments = new InputArguments();

        // help
        Option helpOption = new Option("h", "help", true, "Help");
        helpOption.setArgs(0);

        // input option
        Option inputOption = new Option("i", "input", true, "Input file");
        inputOption.setArgs(1);

        // output option
        Option outputOption = new Option("o", "output", true, "Output file");
        outputOption.setArgs(1);

        // style option
        Option styleOption = new Option("s", "style", true, "Style .ini file");
        styleOption.setArgs(1);

        // title
        Option titleOption = new Option("t", "title", true, "SVG Title");
        titleOption.setArgs(1);

        // transparent background
        Option transparentOpt = new Option("r", "transparent", true,
                "If need transparent background");
        transparentOpt.setArgs(0);

        // scale
        Option scaleOption = new Option("c", "scale", true, "Scaling");
        scaleOption.setArgs(1);

        inputArguments.posixOptions = new Options()
                .addOption(helpOption)
                .addOption(inputOption)
                .addOption(outputOption)
                .addOption(styleOption)
                .addOption(titleOption)
                .addOption(transparentOpt)
                .addOption(scaleOption);

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(inputArguments.posixOptions, args);

        inputArguments.help = cmd.hasOption("help");
        if (inputArguments.help) {
            return inputArguments;
        }

        if (!cmd.hasOption('i')) {
            throw new IllegalArgumentException("Missing required i");
        }
        if (!cmd.hasOption('o')) {
            throw new IllegalArgumentException("Missing required o");
        }
        inputArguments.input = Paths.get(cmd.getOptionValue("input"));
        inputArguments.output = Paths.get(cmd.getOptionValue("output"));
        inputArguments.style = Paths.get(cmd.getOptionValue("style", ""));
        inputArguments.title = cmd.getOptionValue("title");
        inputArguments.transparent = cmd.hasOption("transparent");
        inputArguments.scale = Double.parseDouble(cmd.getOptionValue("scale", "1"));

        return inputArguments;
    }

    public void writeHelp(PrintWriter writer) {
        final String commandLineSyntax = "jsyntrax.jar";
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(writer, 80, commandLineSyntax, "Options",
                posixOptions, 3, 5, "-- HELP --", true);
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

    public boolean transparent() {
        return transparent;
    }

    public double getScale() {
        return scale;
    }

    Options posixOptions;
    boolean help;
    Path input;
    Path output;
    Path style;
    String title;
    boolean transparent;
    double scale;
}
