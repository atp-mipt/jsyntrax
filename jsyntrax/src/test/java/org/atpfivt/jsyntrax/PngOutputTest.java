package org.atpfivt.jsyntrax;

import org.approvaltests.Approvals;
import org.approvaltests.namer.NamedEnvironment;
import org.approvaltests.namer.NamerFactory;
import org.approvaltests.writers.ApprovalBinaryFileWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class PngOutputTest {
    @Test
    public void transcodeTest() throws IllegalAccessException, NoSuchFieldException,
            IOException, URISyntaxException {
        // Given
        Path inputPath = Paths.get(MainTest.class.getResource("test_spec.txt").toURI());
        Path configPath = Paths.get(MainTest.class.getResource("test_style_config.ini").toURI());
        Path outPath = Files.createTempFile("jsyntrax-test-output", ".png");

        // When
        Main.main("-o", outPath.toString(), inputPath.toString(), "-s", configPath.toString());

        // Then
        try (NamedEnvironment ignored = NamerFactory.asOsSpecificTest();
            InputStream inputStream = Files.newInputStream(outPath)) {
            Approvals.verify(new ApprovalBinaryFileWriter(inputStream, "png"));
        } finally {
            Files.deleteIfExists(outPath);
        }
    }
}
