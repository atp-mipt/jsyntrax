package org.atpfivt.jsyntrax.styles;

import org.approvaltests.Approvals;
import org.atpfivt.jsyntrax.Configuration;
import org.atpfivt.jsyntrax.JSyntraxTestUtils;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.groovy_parser.Parser;
import org.atpfivt.jsyntrax.styles.NodeStyle;
import org.atpfivt.jsyntrax.styles.Style;
import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.units.Unit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import static org.atpfivt.jsyntrax.JSyntraxTestUtils.OPTIONS;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.HashMap;

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
                .append("pattern = '^\\w'\n")
                .append("shape = 'hex'\n")
                .append("font = ('Sans', 14, 'bold')\n")
                .append("fill = (255,15,3,129)");
        Files.writeString(stylePath, config);
        StyleConfig cfg = new StyleConfig(stylePath);

        NodeStyle ns = cfg.nodeStyles.get(0);
        Assertions.assertAll(
                () -> assertEquals(50, cfg.line_width),
                () -> assertEquals(42, cfg.v_sep),
                () -> assertEquals(new Color(30, 40, 50), cfg.text_color),
                () -> assertEquals(new Color(35, 46, 57, 212), cfg.shadow_fill),
                () -> assertEquals(1, cfg.nodeStyles.size()),
                () -> assertEquals("hex_bubble", ns.name),
                () -> assertEquals("hex", ns.shape),
                () -> assertEquals(new Font("Sans", Font.BOLD, 14), ns.font),
                () -> assertEquals(new Color(255, 15, 3, 129), ns.fill)
        );
    }

    @Test
    void approvalTest() throws IOException {

        StringBuilder config = new StringBuilder()
                .append("[style]\n")
                .append("line_width = 3\n")
                .append("outline_width = 3\n")
                .append("padding = 5\n")
                .append("line_color = (0, 0, 0)\n")
                .append("max_radius = 29\n")
                .append("h_sep = 17\n")
                .append("v_sep = 9\n")
                .append("arrows = False\n")
                .append("text_color = (0, 0, 0)\n")
                .append("shadow = True\n")
                .append("shadow_fill = (0, 0, 0, 127)\n")
                .append("title_font = ('Sans', 22, 'bold')\n")
                .append("[hex_bubble]\n")
                .append("pattern = '^\\w'\n")
                .append("shape = 'hex'\n")
                .append("font = ('Sans', 14, 'bold')\n")
                .append("fill = (255,0,0,127)\n")
                .append("[box]\n")
                .append("pattern = '^/'\n")
                .append("shape = 'box'\n")
                .append("font = ('Sans', 14, 'bold')\n")
                .append("text_color = (100, 100, 100)\n")
                .append("fill = (136, 170, 238)\n")
                .append("[token]\n")
                .append("pattern = '.'\n")
                .append("shape = 'bubble'\n")
                .append("font = ('Times', 16, 'italic')\n")
                .append("fill = (0, 255, 0, 127)");
        Files.writeString(stylePath, config);

        Style s = new Style(1, false);
        s.updateByFile(stylePath);
        JSyntraxTestUtils.updateStyle(s);

        Configuration configuration = Parser.parse("stack(\n" +
                "line('attribute', '/(attribute) identifier', 'of'),\n" +
                "line(choice(toploop('/entity_designator', ','), 'others', 'all'), ':')\n" +
                ")");

        Unit root = configuration.getTrack();
        HashMap<String, String> urlMap = configuration.getUrlMap();


        SVGCanvas canvas = canvasBuilder.withStyle(s).generateSVG(root);
        String result = canvas.generateSVG();
        Approvals.verify(result, OPTIONS);
    }
}
