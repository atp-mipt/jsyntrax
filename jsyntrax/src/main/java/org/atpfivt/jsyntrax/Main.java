package org.atpfivt.jsyntrax;


import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.groovy_parser.Parser;
import org.atpfivt.jsyntrax.styles.Style;
import org.atpfivt.jsyntrax.units.Unit;
import org.codehaus.groovy.control.CompilationFailedException;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String... args) throws IOException {
        // parse command line arguments
        InputArguments iArgs;
        try {
            iArgs = new InputArguments(args);
        } catch (Exception e) {
            System.out.println("Cannot parse command line");
            InputArguments.writeHelp(new PrintWriter(System.out));
            return;
        }

        // print version if specified
        if (iArgs.getVersion() != null) {
            System.out.println(iArgs.getVersion());
            return;
        }

        // get-style
        if (iArgs.getDefaultStyleProperty()) {
            var defaultConfigReader = new BufferedReader(
                    new InputStreamReader(
                            Main.class.getResourceAsStream("/default_style_config.ini")
                    ));
            String config = defaultConfigReader
                    .lines()
                    .collect(Collectors.joining("\n"))
                    + "\n";
            Path destPath = Path.of(System.getProperty("user.dir") + "/default_config.ini");
            Files.writeString(destPath, config);
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
        Style style = new Style(iArgs.getScale(), iArgs.isTransparent());
        if (iArgs.getStyle() != null) {
            if (!style.updateByFile(iArgs.getStyle())) {
                System.out.println("Failed parsing style file");
                return;
            }
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
        try {
            // parse spec
            root = Parser.parse(scriptText);
        } catch (CompilationFailedException e) {
            System.out.println("Something was wrong with input script " + e.getMessage());
            return;
        }

        // generate SVG
        SVGCanvas c = new SVGCanvasBuilder()
                .withStyle(style)
                .withTitle(iArgs.getTitle())
                .generateSVG(root);
        String result = c.generateSVG();

        // write result to file
        try {
            Files.writeString(iArgs.getOutput(), result);
        } catch (IOException e) {
            System.out.println("Failed to write: " + e.getMessage());
            return;
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
