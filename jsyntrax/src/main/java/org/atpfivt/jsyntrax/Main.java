package org.atpfivt.jsyntrax;


import org.apache.batik.transcoder.TranscoderException;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.groovy_parser.Parser;
import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.util.SVGTranscoder;
import org.codehaus.groovy.control.CompilationFailedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public final class Main {
    public static final String JSYNTRAX_INI = "jsyntrax.ini";

    private Main() {
    }

    public static void main(String... args) throws IOException {
        // parse command line arguments
        InputArguments iArgs;
        try {
            iArgs = new InputArguments(args);
        } catch (Exception e) {
            System.out.println("Got exception when parsing input arguments:");
            System.out.println("\t" + e.getMessage());
            InputArguments.writeHelp(new PrintWriter(System.out));
            return;
        }

        // print version if specified
        String version = iArgs.getVersion();
        if (version != null) {
            System.out.println(iArgs.getVersion());
            return;
        }

        // get-style
        if (iArgs.getDefaultStyleProperty()) {
            String config = new BufferedReader(new InputStreamReader(
                    Main.class.getResourceAsStream("/" + JSYNTRAX_INI)))
                    .lines()
                    .collect(Collectors.joining("\n"))
                    + "\n";
            Path destPath = Paths.get(System.getProperty("user.dir"), JSYNTRAX_INI);
            if (Files.exists(destPath)) {
                System.out.printf("Ini file \"%s\" exists%n", JSYNTRAX_INI);
            } else {
                System.out.printf("Creating ini with default styles in \"%s\"%n", JSYNTRAX_INI);
                Files.write(destPath, config.getBytes(StandardCharsets.UTF_8));
            }
            return;
        }

        // write help if needed
        if (iArgs.isHelp()) {
            InputArguments.writeHelp(new PrintWriter(System.out));
            return;
        }

        // check paths for correct file paths
        if (!checkPathsFromArgs(iArgs)) {
            return;
        }

        // parse style file
        StyleConfig style;
        try {
            style = getStyleConfig(iArgs);
        } catch (IOException e) {
            System.out.println("Failed parsing style file.");
            System.out.println("Error: " + e.getMessage());
            return;
        }

        // read script
        String scriptText;
        try {
            scriptText = new String(Files.readAllBytes(iArgs.getInput()));
        } catch (IOException e) {
            System.out.println("Failed to read: " + e.getMessage());
            return;
        }

        String result = generateSVG(iArgs.getTitle(), style, scriptText);
        if (result == null) return;

        // write result to file
        try {
            Path output = iArgs.getOutput();
            if (output.toString().toLowerCase().endsWith(".png")) {
                Files.write(output, SVGTranscoder.svg2Png(result));
            } else {
                Files.write(output, result.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            System.out.println("Failed to write: " + e.getMessage());
            return;
        } catch (TranscoderException e) {
            System.out.println("Failed to transcode .svg image: " + e.getMessage());
        }
        System.out.println("Done!");
    }

    /** Generates StyleConfig based on provided command line arguments.
     *
     * @param args Command line arguments. Can be created from an array of string arguments
     * @throws IOException style file is inaccessible or invalid
     */
    public static StyleConfig getStyleConfig(InputArguments args) throws IOException {
        if (args.getStyle() != null) {
                return new StyleConfig(
                        args.getScale(),
                        args.isTransparent(),
                        args.getStyle());
        } else {
            return new StyleConfig(
                    args.getScale(),
                    args.isTransparent());
        }
    }

    /**
     * Generates SVG for the diagram.
     * @param title Diagram title
     * @param style Diagram style
     * @param scriptText Text of the diagram script
     *
     */
    public static String generateSVG(String title, StyleConfig style, String scriptText) throws IOException {
        // parse script
        Unit root;
        try {
            // parse spec
            root = Parser.parse(scriptText);
        } catch (CompilationFailedException e) {
            System.out.println("Something is wrong with input script:");
            System.out.println("\t" + e.getMessage());
            return null;
        }

        // generate SVG
        SVGCanvas c = new SVGCanvasBuilder()
                .withStyle(style)
                .withTitle(title)
                .generateSVG(root);
        return c.generateSVG();
    }

    static boolean checkPathsFromArgs(InputArguments iArgs) {
        Path input = iArgs.getInput();
        Path output = iArgs.getOutput();
        Path style = iArgs.getStyle();
        // check input path
        if (!(input != null && Files.isRegularFile(input) && Files.isReadable(input))) {
            System.out.printf("Cannot read input file %s%n", input);
            return false;
        }
        // check output path
        if (output == null) {
            System.out.println("No output file set");
            return false;
        }

        if (!Files.exists(output)) {
            try {
                Files.createFile(output);
            } catch (IOException e) {
                System.out.printf("Can't create file %s%n", output);
                return false;
            }
            System.out.printf("Output file %s was created%n", output);
        }
        if (!(Files.isRegularFile(output) && Files.isWritable(output))) {
            System.out.printf("Cannot write to %s%n", output);
            return false;
        }
        // check style path
        if (style != null && !(Files.isRegularFile(style) && Files.isReadable(style))) {
            System.out.printf("Cannot read style file %s%n", style);
            return false;
        }
        return true;
    }
}
