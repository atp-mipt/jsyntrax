package org.atpfivt.jsyntrax;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {
    @Test
    void testEnd2EndHappyPath() throws URISyntaxException, IOException {
        Path inputPath = Paths.get(MainTest.class.getResource("jsyntrax.spec").toURI());
        Path outPath = Files.createTempFile("jsyntrax-test-output", ".svg");
        try {
            Main.main("-o", outPath.toString(), inputPath.toString());
            String svg = Files.readString(outPath);
            assertAll(
                    () -> assertTrue(svg.contains("JSYNTRAX")),
                    () -> assertTrue(svg.contains("href=\"https://github.com/atp-mipt/jsyntrax\"")));
        } finally {
            Files.delete(outPath);
        }
    }

    @Test
    void testWriteHelp() throws IOException, URISyntaxException {
        final PrintStream standardOut = System.out;
        final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        try {
            Main.main("-h");
            assertTrue(outputStreamCaptor.toString(StandardCharsets.UTF_8).contains(
                    "Railroad diagram generator."
            ));
        } finally {
            System.setOut(standardOut);
        }
    }

    @Test
    void printVersionTest() throws IOException {
        final PrintStream standardOut = System.out;
        final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        try {
            Main.main("-v");
            // There is a problem, that version retrieving method
            // relies on .jar manifest file, which is unavailable when maven runs tests
            // Should I run .jar with shell invocation to test, or it is fine?
            assertTrue(outputStreamCaptor.toString(StandardCharsets.UTF_8).contains(
                    "JSyntrax version is not available. "
                            + "Make sure you're running valid .jar distribution."
            ));
        } finally {
            System.setOut(standardOut);
        }
    }

    @Test
    void getStyleTest() throws IOException {
        Main.main("--get-style");
        Path expectedPath = Path.of(
                System.getProperty("user.dir"),
                "default_style_config.ini"
        );
        byte[] configExpected  = Main
                .class
                .getResourceAsStream("/default_style_config.ini")
                .readAllBytes();
        try {
            byte[] configActual = Files.readAllBytes(expectedPath);
            assertTrue(Arrays.equals(configExpected, configActual));
        } finally {
            Files.delete(expectedPath);
        }
    }
}
