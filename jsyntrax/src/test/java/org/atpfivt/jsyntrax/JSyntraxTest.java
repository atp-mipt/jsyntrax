package org.atpfivt.jsyntrax;

import org.approvaltests.Approvals;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.styles.NodeStyle;
import org.atpfivt.jsyntrax.styles.Style;
import org.atpfivt.jsyntrax.units.tracks.Choice;
import org.atpfivt.jsyntrax.units.tracks.Line;
import org.atpfivt.jsyntrax.units.tracks.loop.Loop;
import org.atpfivt.jsyntrax.units.tracks.loop.Toploop;
import org.atpfivt.jsyntrax.units.tracks.opt.Opt;
import org.atpfivt.jsyntrax.units.tracks.opt.Optx;
import org.atpfivt.jsyntrax.units.tracks.stack.Indentstack;
import org.atpfivt.jsyntrax.units.tracks.stack.Rightstack;
import org.atpfivt.jsyntrax.units.tracks.stack.Stack;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.atpfivt.jsyntrax.groovy_parser.SyntraxScript.*;


public class JSyntraxTest {

    private final SVGCanvasBuilder canvasBuilder;
    private final Font testFont;

    public JSyntraxTest() {
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
    void lineTest() {
        Line l = line('[', "foo", ',', "/bar", ']');
        SVGCanvas c = canvasBuilder.generateSVG(l);
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void difficultLoopTest() {
        Loop l = loop(line("/forward", "path"), line("backward", "path"));
        SVGCanvas c = canvasBuilder.generateSVG(l);
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void toploopTest() {
        Toploop l = toploop(line('(', "forward", ')'), line(')', "backward", '('));
        SVGCanvas c = canvasBuilder.generateSVG(l);
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void choiceTest() {
        Choice choice = choice('A', 'B', 'C');
        SVGCanvas c = canvasBuilder.generateSVG(choice);
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void optTest() {
        Opt opt = opt('A', 'B', 'C');
        SVGCanvas c = canvasBuilder.generateSVG(opt);
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void optxTest() {
        Optx optx = optx('A', 'B', 'C');
        SVGCanvas c = canvasBuilder.generateSVG(optx);
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void stackTest() {
        Stack s = stack(
                line("top", "line"),
                line("bottom", "line")
        );
        SVGCanvas c = canvasBuilder.generateSVG(s);
        String result = c.generateSVG();
        Approvals.verify(result);
    }


    @Test
    void verticalStackTest() {
        Stack s = stack(
                line('A', 'B'),
                opt("bypass"),
                line("finish")
        );
        SVGCanvas c = canvasBuilder.generateSVG(s);
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void indentStackTest() {
        Indentstack indentStack = indentstack(3,
                line("top", "line"),
                line("bottom", "line")
        );
        SVGCanvas c = canvasBuilder.generateSVG(indentStack);
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void rightStackTest() {
        Rightstack rightStack = rightstack(
                line("top", "line", "with", "more", "code"),
                line("bottom", "line")
        );
        SVGCanvas c = canvasBuilder.generateSVG(rightStack);
        String result = c.generateSVG();
        Approvals.verify(result);
    }
}