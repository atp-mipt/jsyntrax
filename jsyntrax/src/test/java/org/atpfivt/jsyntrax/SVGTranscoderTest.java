package org.atpfivt.jsyntrax;

import org.approvaltests.Approvals;
import org.approvaltests.approvers.FileApprover;
import org.approvaltests.core.Options;
import org.approvaltests.core.VerifyResult;
import org.approvaltests.writers.FileApprovalWriter;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.groovy_parser.Parser;
import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.units.Unit;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;



public class SVGTranscoderTest {
    @Test
    public void transcodeTest() throws IllegalAccessException, NoSuchFieldException,
            IOException, URISyntaxException {
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
        Path configPath = Paths.get(MainTest.class.getResource("test_style_config.ini").toURI());
        Path outPath = Files.createTempFile("jsyntrax-test-output", ".png");

        FileApprover binaryFileApprover = new FileApprover(
                new FileApprovalWriter(outPath.toFile()),
                new Options().forFile().getNamer(),
                this::imageApprover
        );
        try {
            Main.main("-o", outPath.toString(), inputPath.toString(), "-s", configPath.toString());
            Approvals.verify(binaryFileApprover);
        } finally {
            Files.deleteIfExists(outPath);
        }
    }

    private VerifyResult imageApprover(File received, File approved) {
        boolean success = Arrays.equals(
                getImageBytes(received),
                getImageBytes(approved)
        );
        return VerifyResult.from(success);
    }

    public byte[] getImageBytes(File input) {
        byte[] imageBytes = null;
        try {
            BufferedImage img = ImageIO.read(input);
            imageBytes = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return imageBytes;
    }
}
