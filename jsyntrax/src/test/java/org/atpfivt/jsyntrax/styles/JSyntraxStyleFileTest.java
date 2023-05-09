package org.atpfivt.jsyntrax.styles;

import org.approvaltests.Approvals;
import org.atpfivt.jsyntrax.Configuration;
import org.atpfivt.jsyntrax.JSyntraxTestUtils;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.groovy_parser.Parser;
import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.util.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.atpfivt.jsyntrax.JSyntraxTestUtils.OPTIONS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSyntraxStyleFileTest {
    private final SVGCanvasBuilder canvasBuilder;
    private final Path stylePath =
            Files.createTempFile("jsyntrax-test-style", ".ini");

    public JSyntraxStyleFileTest()
            throws IOException, NoSuchFieldException, IllegalAccessException {
        StyleConfig s = new StyleConfig(1, false);
        JSyntraxTestUtils.updateStyle(s);
        canvasBuilder = new SVGCanvasBuilder().withStyle(s);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.delete(stylePath);
    }

    @Test
    void colorFromString(){
        assertEquals(new Color(1, 2, 3, 4),
                StringUtils.colorFromString("(1, 2, 3, 4)"));
        assertEquals(new Color(1, 2, 3),
                StringUtils.colorFromString(" (1,2, 3) "));
    }

    @Test
    void fontFromString(){
        assertEquals(new Font("FooFont", Font.BOLD, 12),
                StringUtils.fontFromString("('FooFont', 12, 'bold')"));
        assertEquals(new Font("BarFont", Font.ITALIC | Font.BOLD, 10),
                StringUtils.fontFromString(" ('BarFont', 10, 'bold italic') "));
    }

    @Test
    void configParserTest() throws IOException, NoSuchFieldException, IllegalAccessException {
        StringBuilder config = new StringBuilder();
        config.append("[style]\n")
                .append("line_width = 50\n")
                .append("v_sep = 42\n")
                .append("text_color = (30, 40, 50)\n")
                .append("shadow_fill = (35, 46, 57, 212)\n")
                .append("[hex_bubble]\n")
                .append("pattern = '^(\\w.*)'\n")
                .append("shape = 'hex'\n")
                .append("font = ('Sans', 14, 'bold')\n")
                .append("fill = (255,15,3,129)");
        Files.writeString(stylePath, config);
        StyleConfig cfg = new StyleConfig(1, false, stylePath);

        NodeStyle ns = cfg.getNodeStyles().get(0);
        Assertions.assertAll(
                () -> assertEquals(50, cfg.getLineWidth()),
                () -> assertEquals(42, cfg.getVSep()),
                () -> assertEquals(new Color(30, 40, 50), cfg.getTextColor()),
                () -> assertEquals(new Color(35, 46, 57, 212), cfg.getShadowFill()),
                () -> assertEquals(1, cfg.getNodeStyles().size()),
                () -> assertEquals("hex_bubble", ns.getName()),
                () -> assertEquals("hex", ns.getShape()),
                () -> assertEquals(new Font("Sans", Font.BOLD, 14), ns.getFont()),
                () -> assertEquals(new Color(255, 15, 3, 129), ns.getFill())
        );
    }

    @Test
    public void svgFromConfigTest()
            throws IOException, URISyntaxException,
            NoSuchFieldException, IllegalAccessException {
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
        Configuration configuration = new Parser(spec).getNode();
        Files.writeString(stylePath, config);

        StyleConfig s = new StyleConfig(1, false, stylePath);
        JSyntraxTestUtils.updateStyle(s);

        //When
        Unit root = configuration.getTrack();
        SVGCanvas canvas = canvasBuilder.withStyle(s).generateSVG(root);

        //Then
        String result = canvas.generateSVG();
        Approvals.verify(result, OPTIONS);
    }
}
