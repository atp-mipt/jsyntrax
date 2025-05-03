package org.atpfivt.jsyntrax;

import org.approvaltests.Approvals;
import org.approvaltests.namer.NamedEnvironment;
import org.approvaltests.namer.NamerFactory;
import org.approvaltests.writers.ApprovalBinaryFileWriter;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class PngOutputTest {
    private static void setupPNGFonts() throws IOException, FontFormatException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try (InputStream fontStream = Main.class.getResourceAsStream("pt-sans_regular.ttf")) {
            Font f = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            ge.registerFont(f);
        }
    }

    @Test
    public void transcodeTest() throws
            IOException, URISyntaxException, FontFormatException {
        setupPNGFonts();
        // Given
        Path inputPath = Paths.get(MainTest.class.getResource("test_spec.txt").toURI());
        Path configPath = Paths.get(MainTest.class.getResource("test_style_config.ini").toURI());
        Path outPath = Files.createTempFile("jsyntrax-test-output", ".png");

        // When
        Main.main("-o", outPath.toString(), inputPath.toString(), "-s", configPath.toString());

        // Then
        try (InputStream inputStream = Files.newInputStream(outPath)) {
            Approvals.verify(new ApprovalBinaryFileWriter(inputStream, "png"));
        } finally {
            Files.deleteIfExists(outPath);
        }
    }
}
