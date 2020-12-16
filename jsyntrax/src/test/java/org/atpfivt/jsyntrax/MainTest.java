package org.atpfivt.jsyntrax;

import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.groovy_parser.Parser;
import org.atpfivt.jsyntrax.styles.MatchException;
import org.atpfivt.jsyntrax.styles.NodeStyle;
import org.atpfivt.jsyntrax.styles.Style;
import org.atpfivt.jsyntrax.units.Unit;
import org.codehaus.groovy.control.CompilationFailedException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
class MainTest {
    @Test
    void testEnd2EndHappyPath() throws URISyntaxException, IOException {
        Path inputPath = Paths.get(MainTest.class.getResource("jsyntrax.spec").toURI());
        Path outPath = Files.createTempFile("jsyntrax-test-output", ".svg");
        try {
            Main.main("-o", outPath.toString(), inputPath.toString());
            assertTrue(Files.readString(outPath).contains("JSYNTRAX"));
        } finally {
            Files.delete(outPath);
        }
    }
    @Test
    void testFormatByNamedGroup() throws MatchException {
        NodeStyle nodeStyle = new NodeStyle();
        nodeStyle.setCapture_group("x");
        nodeStyle.pattern = Pattern.compile("(.*)(?<x>abc)(.*)");
        String extractedText = nodeStyle.extractCaptureGroupText("dfgabcdfs");
        assertEquals("abc", extractedText);
    }
    @Test
    void testFormatByDefaultNamedGroup() throws MatchException {
        NodeStyle nodeStyle = new NodeStyle();
        nodeStyle.pattern = Pattern.compile("(.*)(?<x>abc)(.*)");
        String txt = "dfgabcdfs";
        String extractedText = nodeStyle.extractCaptureGroupText(txt);
        assertEquals(txt, extractedText);
    }
}
