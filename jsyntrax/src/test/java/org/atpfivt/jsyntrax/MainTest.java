package org.atpfivt.jsyntrax;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class MainTest {
    @Test
    void testEnd2EndHappyPath() throws URISyntaxException, IOException {
        Path inputPath = Paths.get(MainTest.class.getResource("jsyntrax.spec").toURI());
        Path outPath = Files.createTempFile("jsyntrax-test-output", ".svg");
        try {
            Main.main("-o", outPath.toString(), inputPath.toString());
            Approvals.verify(Files.readString(outPath),
                    JSyntraxTestUtils.OPTIONS);
        } finally {
            Files.delete(outPath);
        }
    }
}