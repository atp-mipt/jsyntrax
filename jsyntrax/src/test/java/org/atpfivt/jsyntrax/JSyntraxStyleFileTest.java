package org.atpfivt.jsyntrax;

import org.approvaltests.Approvals;
import org.approvaltests.core.ApprovalFailureReporter;
import org.approvaltests.core.Options;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.groovy_parser.Parser;
import org.atpfivt.jsyntrax.styles.NodeStyle;
import org.atpfivt.jsyntrax.styles.Style;
import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.units.tracks.stack.Stack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSyntraxStyleFileTest {

    private final static Options options = new Options().forFile().withExtension(".svg");
    private final SVGCanvasBuilder canvasBuilder;
    private final Font testFont;

    public JSyntraxStyleFileTest() {
        testFont = getTestFont();

        Style s = new Style(1, false);
        s.title_font = testFont;

        for (NodeStyle ns : s.nodeStyles) {
            ns.font = testFont;
        }
        canvasBuilder = new SVGCanvasBuilder().withStyle(s);
    }

    private Font getTestFont() {
        try {
            return Font.createFont(Font.TRUETYPE_FONT,
                    JSyntraxTest.class.getResourceAsStream("PTSans-Regular.ttf"))
                    .deriveFont(12.f);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void configParserTest() throws IOException {
        Path stylePath = Paths.get("style.ini");
        if (!Files.exists(stylePath)) {
            Files.createFile(stylePath);
        }
        Assertions.assertTrue(Files.isRegularFile(stylePath));
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

        Assertions.assertEquals(cfg.line_width, 50);
        Assertions.assertEquals(cfg.v_sep, 42);

        Assertions.assertEquals(cfg.text_color.getRed(), 30);
        Assertions.assertEquals(cfg.text_color.getGreen(), 40);
        Assertions.assertEquals(cfg.text_color.getBlue(), 50);

        Assertions.assertEquals(cfg.shadow_fill.getRed(), 35);
        Assertions.assertEquals(cfg.shadow_fill.getGreen(), 46);
        Assertions.assertEquals(cfg.shadow_fill.getBlue(), 57);
        Assertions.assertEquals(cfg.shadow_fill.getAlpha(), 212);

        Assertions.assertEquals(cfg.nodeStyles.size(), 1);
        NodeStyle ns = cfg.nodeStyles.get(0);
        Assertions.assertEquals(ns.name, "hex_bubble");
        Assertions.assertEquals(ns.shape, "hex");

        Assertions.assertEquals(ns.font.getName(), "Sans");
        Assertions.assertEquals(ns.font.getStyle(), 1);
        Assertions.assertEquals(ns.font.getSize(), 14);

        Assertions.assertEquals(ns.fill.getRed(), 255);
        Assertions.assertEquals(ns.fill.getGreen(), 15);
        Assertions.assertEquals(ns.fill.getBlue(), 3);
        Assertions.assertEquals(ns.fill.getAlpha(), 129);
    }

    @Test
    void approvalTest() throws IOException {
        Path stylePath = Paths.get("style.ini");
        if (!Files.exists(stylePath)) {
            Files.createFile(stylePath);
        }
        Assertions.assertTrue(Files.isRegularFile(stylePath));
        StringBuilder config = new StringBuilder();
        config.append("[style]\n")
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
                .append("fill = '(136, 170, 238)'\n")
                .append("[token]\n")
                .append("pattern = '.'\n")
                .append("shape = 'bubble'\n")
                .append("font = ('Times', 16, 'italic')\n")
                .append("fill = (0, 255, 0, 127)");
        Files.writeString(stylePath, config);

        Style s = new Style(1, false);
        s.updateByFile(stylePath);
        s.title_font = testFont;

        for (NodeStyle ns : s.nodeStyles) {
            ns.font = testFont;
        }

        Unit root = Parser.parse("stack(\n" +
                "line('attribute', '/(attribute) identifier', 'of'),\n" +
                "line(choice(toploop('/entity_designator', ','), 'others', 'all'), ':')\n" +
                ")");

        SVGCanvas canvas = canvasBuilder.withStyle(s).generateSVG(root);
        String result = canvas.generateSVG();
        Approvals.verify(result, options);
    }
}
