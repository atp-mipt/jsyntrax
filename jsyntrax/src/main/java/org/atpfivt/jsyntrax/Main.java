package org.atpfivt.jsyntrax;


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
            e.printStackTrace();
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
        SVGCanvas c = new SVGCanvasBuilder(iArgs).generateSVG(root, urlMap);
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
            if (!Files.isRegularFile(input)) {
                System.out.println("Got path " + input + " is not a regular file");
                return false;
            }

            if (!Files.isReadable(input)) {
                System.out.println("There is no read access for file: " + input);
                return false;
            }

            if (!Files.exists(output)) {
                Files.createFile(output);
            }

            if (!Files.isWritable(output)) {
                System.out.println("There is no write access for file: " + output);
                return false;
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
        } catch(JSONException e) {
            System.out.println("Failed parse url map: " + e.getMessage());
            return false;
        }
        return true;
    }
}
