package org.atpfivt.jsyntrax;


import org.atpfivt.jsyntrax.styles.Style;
import org.codehaus.groovy.control.CompilationFailedException;
import org.json.JSONException;
import org.json.JSONObject;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.groovy_parser.Parser;
import org.atpfivt.jsyntrax.units.Unit;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        // parse command line arguments
        InputArguments iArgs;
        try {
            iArgs = InputArguments.parseArgs(args);
        } catch (Exception e) {
            System.out.println("Failed CommandLine parse");
            System.out.println(e.getMessage());
            return;
        }

        // write help if need
        if (iArgs.isHelp()) {
            iArgs.writeHelp(new PrintWriter(System.out));
            return;
        }

        // check paths for correct file paths
        if (!checkPathsFromArgs(iArgs)) {
            return;
        }

        // parse style file
        Style style = new Style(iArgs.getScale(), iArgs.transparent());
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

        // find url map
        Map<String, String> urlMap = new HashMap<>();
        Matcher m = Pattern.compile("^*url_map[ ]*=*").matcher(scriptText);
        if (m.find()) {
            String urlMapText = scriptText.substring(m.end());
            scriptText = scriptText.substring(0, m.start());
            if (!evaluateUrlMap(urlMapText, urlMap)) {
                return;
            }
        }

        Unit root;
        try {
            // parse spec
            root = Parser.parse(scriptText);
            //System.out.println(root);
        } catch (CompilationFailedException e) {
            System.out.println("Something was wrong with input script " + e.getMessage());
            return;
        }

        // generate SVG
        SVGCanvas c = new SVGCanvasBuilder()
                .withStyle(style)
                .withUrlMap(urlMap)
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
        try {
            Path input = iArgs.getInput();
            Path output = iArgs.getOutput();
            Path style = iArgs.getStyle();

            // check input path
            if (!Files.isRegularFile(input)) {
                System.out.println("Got input path " + input + " is not a regular file");
                return false;
            }

            if (!Files.isReadable(input)) {
                System.out.println("There is no read access for file: " + input);
                return false;
            }

            // check output path
            if (!Files.exists(output)) {
                Files.createFile(output);
                System.out.println("Output file " + output + "was created");
            }

            if (!Files.isRegularFile(output)) {
                System.out.println("Got output path " + input + " is not a regular file");
                return false;
            }

            if (!Files.isWritable(output)) {
                System.out.println("There is no write access for file: " + output);
                return false;
            }

            // check style path
            if (style != null) {
                if (!Files.isRegularFile(style)) {
                    System.out.println("Got style path " + style + " is not a regular file");
                    return false;
                }

                if (!Files.isReadable(style)) {
                    System.out.println("There is no read access for file: " + style);
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    static boolean evaluateUrlMap(String text, Map<String, String> urlMap) {
        try {
            JSONObject map = new JSONObject(text);
            for (Iterator<String> it = map.keys(); it.hasNext(); ) {
                String key = it.next();
                urlMap.put(key, map.getString(key));
            }
        } catch (JSONException e) {
            System.out.println("Failed parse url map: " + e.getMessage());
            return false;
        }
        return true;
    }
}
