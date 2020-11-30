package org.atpfivt.jsyntrax;

import groovyjarjarcommonscli.*;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InputArguments {

    InputArguments() {}

    public static InputArguments parseArgs(String[] args) throws ParseException, NumberFormatException {
        // TODO: add style
        InputArguments inputArguments = new InputArguments();

        // input option
        Option inputOption = new Option("i", "input", true, "Input file");
        inputOption.setArgs(1);
        inputOption.setRequired(true);

        // output option
        Option outputOption = new Option("o", "output", true, "Output file");
        outputOption.setArgs(1);
        outputOption.setRequired(true);

        // help
        Option helpOption = new Option("h", "help", true, "Help");
        helpOption.setArgs(0);
        helpOption.setRequired(false);

        // title
        Option titleOption = new Option("t", "title", true, "SVG Title");
        titleOption.setArgs(1);
        titleOption.setRequired(false);

        // transparent background
        Option transparentOpt = new Option("r", "transparent", true,
                "If need transparent background");
        transparentOpt.setArgs(0);
        transparentOpt.setRequired(false);

        // scale
        Option scaleOption = new Option("s", "scale", true, "Scaling");
        scaleOption.setArgs(1);
        scaleOption.setRequired(false);

        inputArguments.posixOptions = new Options()
                .addOption(inputOption)
                .addOption(outputOption)
                .addOption(helpOption)
                .addOption(titleOption)
                .addOption(transparentOpt)
                .addOption(scaleOption);

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(inputArguments.posixOptions, args);

        inputArguments.input = Paths.get(cmd.getOptionValue('i'));
        inputArguments.output = Paths.get(cmd.getOptionValue('o'));
        inputArguments.help = cmd.hasOption('h');
        inputArguments.title = cmd.getOptionValue('t');
        inputArguments.transparent = cmd.hasOption('r');
        inputArguments.scale = Double.parseDouble(cmd.getOptionValue('s', "1"));

        return inputArguments;
    }

    public void writeHelp(PrintWriter writer) {
        // TODO: rename to jar
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
    String title;
    boolean transparent;
    double scale;
}
