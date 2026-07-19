package org.atpfivt.jsyntrax.styles;

import org.approvaltests.Approvals;
import org.atpfivt.jsyntrax.Configuration;
import org.atpfivt.jsyntrax.JSyntraxTestUtils;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.parser.Parser;
import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.util.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.atpfivt.jsyntrax.JSyntraxTestUtils.OPTIONS;
import static org.assertj.core.api.Assertions.assertThat;

public class JSyntraxStyleFileTest {
    private final SVGCanvasBuilder canvasBuilder;
    private final Path stylePath =
            Files.createTempFile("jsyntrax-test-style", ".ini");

    public JSyntraxStyleFileTest()
            throws IOException {
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
        assertThat(StringUtils.colorFromString("(1, 2, 3, 4)"))
                .isEqualTo(new Color(1, 2, 3, 4));
        assertThat(StringUtils.colorFromString(" (1,2, 3) "))
                .isEqualTo(new Color(1, 2, 3));
    }

    @Test
    void fontFromString(){
        assertThat(StringUtils.fontFromString("('FooFont', 12, 'bold')"))
                .isEqualTo(new Font("FooFont", Font.BOLD, 12));
        assertThat(StringUtils.fontFromString(" ('BarFont', 10, 'bold italic') "))
                .isEqualTo(new Font("BarFont", Font.ITALIC | Font.BOLD, 10));
    }

    @Test
    void configParserTest() throws IOException {
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
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(cfg.getLineWidth()).isEqualTo(50);
            softly.assertThat(cfg.getVSep()).isEqualTo(42);
            softly.assertThat(cfg.getTextColor()).isEqualTo(new Color(30, 40, 50));
            softly.assertThat(cfg.getShadowFill()).isEqualTo(new Color(35, 46, 57, 212));
            softly.assertThat(cfg.getNodeStyles()).hasSize(1);
            softly.assertThat(ns.getName()).isEqualTo("hex_bubble");
            softly.assertThat(ns.getShape()).isEqualTo("hex");
            softly.assertThat(ns.getFont()).isEqualTo(new Font("Sans", Font.BOLD, 14));
            softly.assertThat(ns.getFill()).isEqualTo(new Color(255, 15, 3, 129));
        });
    }

    @Test
    public void svgFromConfigTest()
            throws IOException, URISyntaxException {
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
