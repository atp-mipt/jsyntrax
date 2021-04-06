package org.atpfivt.jsyntrax;

import org.apache.batik.transcoder.TranscoderException;
import org.approvaltests.Approvals;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.groovy_parser.Parser;
import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.util.SVGTranscoder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SVGTranscoderTest {
    @Test
    public void transcodeTest() throws IllegalAccessException, NoSuchFieldException,
            IOException, URISyntaxException, TranscoderException {
        // Given
        String config  = Files.readString(
                Path.of(this
                        .getClass()
                        .getResource("/org/atpfivt/jsyntrax/test_style_config.ini")
                        .toURI())
        );
        String spec  = Files.readString(
                Path.of(this
                        .getClass()
                        .getResource("/org/atpfivt/jsyntrax/test_spec.txt")
                        .toURI())
        );
        Path stylePath = null;
        try {
            stylePath = Files.createTempFile("jsyntrax-test-style", ".ini");
        } finally {
            Files.deleteIfExists(stylePath);
        }
        Configuration configuration = Parser.parse(spec);
        Files.writeString(stylePath, config);
        StyleConfig s = new StyleConfig(1, false, stylePath);
        JSyntraxTestUtils.updateStyle(s);

        //When
        Unit root = configuration.getTrack();
        SVGCanvas canvas = new SVGCanvasBuilder().withStyle(s).generateSVG(root);

        //Then
        Path inputPath = Paths.get(MainTest.class.getResource("test_spec.txt").toURI());
        Path outPath = Files.createTempFile("jsyntrax-test-output", ".png");
        try {
            Main.main("-o", outPath.toString(), inputPath.toString());
            Approvals.verify(outPath.toFile());
        } finally {
            Files.deleteIfExists(outPath);
        }
    }
}
