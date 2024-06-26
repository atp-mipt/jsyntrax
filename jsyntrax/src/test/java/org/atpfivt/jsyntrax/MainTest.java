package org.atpfivt.jsyntrax;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void testEnd2EndHappyPath() throws URISyntaxException, IOException {
        Path inputPath = Paths.get(MainTest.class.getResource("jsyntrax.spec").toURI());
        Path outPath = Files.createTempFile("jsyntrax-test-output", ".svg");
        try {
            Main.main("-o", outPath.toString(), inputPath.toString());
            String svg = Files.readString(outPath);
            validateSVG(svg);
        } finally {
            Files.delete(outPath);
        }
    }

    @Test
    void testWriteHelp() throws IOException {
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
            assertTrue(outputStreamCaptor.toString(StandardCharsets.UTF_8).startsWith("JSyntrax "));
        } finally {
            System.setOut(standardOut);
        }
    }

    @Test
    void getStyleTest() throws IOException, URISyntaxException {
        final Path resource = Path.of(
                Main.class.getResource("/" + Main.JSYNTRAX_INI).toURI());
        List<String> configExpected = Files.readAllLines(resource);
        Main.main("--get-style");
        Path expectedPath = Path.of(
                System.getProperty("user.dir"), Main.JSYNTRAX_INI);
        try {
            List<String> configActual = Files.readAllLines(expectedPath);
            assertEquals(configExpected, configActual);
        } finally {
            Files.delete(expectedPath);
        }
    }

    @ValueSource(strings = {".png", ".PNG", ".PnG"})
    @ParameterizedTest
    void testPngByOutFileExtention(String ext) throws IOException, URISyntaxException {
        Path inputPath = Paths.get(MainTest.class.getResource("jsyntrax.spec").toURI());
        Path outPath = Files.createTempFile("jsyntrax-test-output", ext);
        try {
            Main.main("-o", outPath.toString(), inputPath.toString());
            byte[] png = Files.readAllBytes(outPath);
            validatePng(png);
        } finally {
            Files.delete(outPath);
        }
    }

    @Test
    void testPngByOutFileType() throws IOException, URISyntaxException {
        Path inputPath = Paths.get(MainTest.class.getResource("jsyntrax.spec").toURI());
        Path outPath = Paths.get(inputPath.toString().substring(0, inputPath.toString().lastIndexOf('.')) + ".png");
        try {
            Main.main("-o", "png", inputPath.toString());
            byte[] png = Files.readAllBytes(outPath);
            validatePng(png);
        } finally {
            Files.delete(outPath);
        }
    }

    @Test
    void testSvgByOutFileType() throws IOException, URISyntaxException {
        Path inputPath = Paths.get(MainTest.class.getResource("jsyntrax.spec").toURI());
        Path outPath = Paths.get(inputPath.toString().substring(0, inputPath.toString().lastIndexOf('.')) + ".svg");
        try {
            Main.main("-o", "svg", inputPath.toString());
            String svg = Files.readString(outPath);
            validateSVG(svg);
        } finally {
            Files.delete(outPath);
        }
    }

    @Test
    void testPngByDefault() throws IOException, URISyntaxException {
        Path inputPath = Paths.get(MainTest.class.getResource("jsyntrax.spec").toURI());
        Path outPath = Paths.get(inputPath.toString().substring(0, inputPath.toString().lastIndexOf('.')) + ".png");
        try {
            Main.main(inputPath.toString());
            byte[] png = Files.readAllBytes(outPath);
            validatePng(png);
        } finally {
            Files.delete(outPath);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "sysexit.spec",
            "sysexitreflection.spec"})
    void testSysexitNotAllowed(String filename) throws URISyntaxException, IOException {
        Path inputPath = Paths.get(MainTest.class.getResource(filename).toURI());
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outContent));
            Main.main(inputPath.toString());
            assertTrue(outContent.toString().contains("Something is wrong with input script"));
        } finally {
            System.setOut(originalOut);
            System.out.println(outContent);
        }
    }


    private static void validatePng(byte[] png) {
        //check for png header
        assertEquals((byte) 0x89, png[0]);
        assertEquals((byte) 0x50, png[1]);
        assertEquals((byte) 0x4e, png[2]);
        assertEquals((byte) 0x47, png[3]);
    }

    private static void validateSVG(String svg) {
        assertTrue(svg.startsWith("<?xml"));
        assertTrue(svg.contains("JSYNTRAX"));
        assertTrue(svg.contains("diagram title"));
        assertTrue(svg.contains("href=\"https://github.com/atp-mipt/jsyntrax\""));
    }
}
