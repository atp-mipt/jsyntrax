package org.atpfivt.jsyntrax.styles;

import org.approvaltests.Approvals;
import org.atpfivt.jsyntrax.Configuration;
import org.atpfivt.jsyntrax.JSyntraxTestUtils;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.groovy_parser.Parser;
import org.atpfivt.jsyntrax.units.Unit;
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

    public JSyntraxStyleFileTest() throws IOException {
        Style s = new Style(1, false);
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
                StyleConfig.colorFromString("(1, 2, 3, 4)"));
        assertEquals(new Color(1, 2, 3),
                StyleConfig.colorFromString(" (1,2, 3) "));
    }

    @Test
    void fontFromString(){
        assertEquals(new Font("FooFont", Font.BOLD, 12),
                StyleConfig.fontFromString("('FooFont', 12, 'bold')"));
        assertEquals(new Font("BarFont", Font.ITALIC | Font.BOLD, 10),
                StyleConfig.fontFromString(" ('BarFont', 10, 'bold italic') "));
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
        StyleConfig cfg = new StyleConfig(stylePath);

        NodeStyle ns = cfg.nodeStyles.get(0);
        Assertions.assertAll(
                () -> assertEquals(50, cfg.getLineWidth()),
                () -> assertEquals(42, cfg.getVSep()),
                () -> assertEquals(new Color(30, 40, 50), cfg.getTextColor()),
                () -> assertEquals(new Color(35, 46, 57, 212), cfg.getShadowFill()),
                () -> assertEquals(1, cfg.nodeStyles.size()),
                () -> assertEquals("hex_bubble", ns.name),
                () -> assertEquals("hex", ns.shape),
                () -> assertEquals(new Font("Sans", Font.BOLD, 14), ns.font),
                () -> assertEquals(new Color(255, 15, 3, 129), ns.fill)
        );
    }

    @Test
    public void mustPass() throws URISyntaxException, IOException {
        Path.of(this.getClass()
                .getResource("/org/atpfivt/jsyntrax/test_style_config.ini")
                .toURI());

    }

    @Test
    public void svgFromConfigTest() throws IOException, URISyntaxException {
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
        Style s        = new Style(1, false);
        Configuration configuration = Parser.parse(spec);

        Files.writeString(stylePath, config);

        s.updateByFile(stylePath);
        JSyntraxTestUtils.updateStyle(s);

        //When
        Unit root = configuration.getTrack();
        SVGCanvas canvas = canvasBuilder.withStyle(s).generateSVG(root);

        //Then
        String result = canvas.generateSVG();
        Approvals.verify(result, OPTIONS);
    }
}
