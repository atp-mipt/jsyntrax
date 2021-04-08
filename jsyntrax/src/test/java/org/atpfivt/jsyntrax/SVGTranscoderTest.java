package org.atpfivt.jsyntrax;

import org.approvaltests.awt.AwtApprovals;
import org.approvaltests.namer.NamerFactory;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class SVGTranscoderTest {
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
        try {
            NamerFactory.asOsSpecificTest();
            AwtApprovals.verify(ImageIO.read(outPath.toFile()));
        } finally {
            Files.deleteIfExists(outPath);
        }
    }
}
