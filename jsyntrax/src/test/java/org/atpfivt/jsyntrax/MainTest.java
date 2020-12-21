package org.atpfivt.jsyntrax;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    void testWriteHelp() {
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
}
