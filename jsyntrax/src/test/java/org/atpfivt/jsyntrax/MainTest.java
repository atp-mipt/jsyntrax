package org.atpfivt.jsyntrax;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
}
