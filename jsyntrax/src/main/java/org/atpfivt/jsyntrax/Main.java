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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public final class Main {
    public static final String JSYNTRAX_INI = "jsyntrax.ini";

    private Main() {
    }

    public static void main(String... args) throws IOException, NoSuchFieldException, IllegalAccessException {
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
            Path destPath = Path.of(System.getProperty("user.dir"), JSYNTRAX_INI);
            if (Files.exists(destPath)) {
                System.out.printf("Ini file \"%s\" exists%n", JSYNTRAX_INI);
            } else {
                System.out.printf("Creating ini with default styles in \"%s\"%n", JSYNTRAX_INI);
                Files.writeString(destPath, config);
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
        if (iArgs.getStyle() != null) {
            try {
                style = new StyleConfig(
                        iArgs.getScale(),
                        iArgs.isTransparent(),
                        iArgs.getStyle());
            } catch (Exception e) {
                System.out.println("Failed parsing style file.");
                System.out.println("Error: " + e.getMessage());
                return;
            }
        } else {
            style = new StyleConfig(
                    iArgs.getScale(),
                    iArgs.isTransparent());
        }

        // read script
        String scriptText;
        try {
            scriptText = new String(Files.readAllBytes(iArgs.getInput()));
        } catch (IOException e) {
            System.out.println("Failed to read: " + e.getMessage());
            return;
        }

        // parse script
        Unit root;
        String titleInSpecFile;
        try {
            // parse spec
            root = Parser.parse(scriptText);
            titleInSpecFile = Parser.getTitle();
        } catch (CompilationFailedException e) {
            System.out.println("Something was wrong with input script:");
            System.out.println("\t" + e.getMessage());
            return;
        }

        // generate SVG
        SVGCanvas c = new SVGCanvasBuilder()
                .withStyle(style)
                .withTitle(iArgs.getTitle() != null ? iArgs.getTitle() : titleInSpecFile)
                .generateSVG(root);
        String result = c.generateSVG();

        // write result to file
        try {
            Path output = iArgs.getOutput();
            if (output.toString().toLowerCase().endsWith(".png")) {
                Files.write(output, SVGTranscoder.svg2Png(result));
            } else {
                Files.writeString(output, result);
            }
        } catch (IOException e) {
            System.out.println("Failed to write: " + e.getMessage());
            return;
        } catch (TranscoderException e) {
            System.out.println("Failed to transcode .svg image: " + e.getMessage());
        }
        System.out.println("Done!");
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
