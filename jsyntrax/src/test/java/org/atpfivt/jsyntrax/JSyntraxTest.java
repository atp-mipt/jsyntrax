package org.atpfivt.jsyntrax;

import org.approvaltests.Approvals;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.styles.Style;
import org.atpfivt.jsyntrax.units.Unit;
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

import static org.atpfivt.jsyntrax.JSyntraxTestUtils.OPTIONS;
import static org.atpfivt.jsyntrax.groovy_parser.SyntraxScript.*;


public class JSyntraxTest {

    private final SVGCanvasBuilder canvasBuilder;

    public JSyntraxTest() {
        Style s = new Style(1, false);
        JSyntraxTestUtils.updateStyle(s);
        canvasBuilder = new SVGCanvasBuilder().withStyle(s);
    }

    @Test
    void lineTest() {
        Line l = line('[', "foo", ',', "/bar", ']');
        SVGCanvas c = canvasBuilder.generateSVG(l);
        String result = c.generateSVG();
        Approvals.verify(result, OPTIONS);
    }

    @Test
    void difficultLoopTest() {
        Loop l = loop(line("/forward", "path"), line("backward", "path"));
        SVGCanvas c = canvasBuilder.generateSVG(l);
        String result = c.generateSVG();
        Approvals.verify(result, OPTIONS);
    }

    @Test
    void toploopTest() {
        Toploop l = toploop(line('(', "forward", ')'), line(')', "backward", '('));
        SVGCanvas c = canvasBuilder.generateSVG(l);
        String result = c.generateSVG();
        Approvals.verify(result, OPTIONS);
    }

    @Test
    void choiceTest() {
        Choice choice = choice('A', 'B', 'C');
        SVGCanvas c = canvasBuilder.generateSVG(choice);
        String result = c.generateSVG();
        Approvals.verify(result, OPTIONS);
    }

    @Test
    void optTest() {
        Opt opt = opt('A', 'B', 'C');
        SVGCanvas c = canvasBuilder.generateSVG(opt);
        String result = c.generateSVG();
        Approvals.verify(result, OPTIONS);
    }

    @Test
    void optxTest() {
        Optx optx = optx('A', 'B', 'C');
        SVGCanvas c = canvasBuilder.generateSVG(optx);
        String result = c.generateSVG();
        Approvals.verify(result, OPTIONS);
    }

    @Test
    void stackTest() {
        Stack s = stack(
                line("top", "line"),
                line("bottom", "line")
        );
        SVGCanvas c = canvasBuilder.generateSVG(s);
        String result = c.generateSVG();
        Approvals.verify(result, OPTIONS);
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
        Approvals.verify(result, OPTIONS);
    }

    @Test
    void indentStackTest() {
        Indentstack indentStack = indentstack(3,
                line("top", "line"),
                line("bottom", "line")
        );
        SVGCanvas c = canvasBuilder.generateSVG(indentStack);
        String result = c.generateSVG();
        Approvals.verify(result, OPTIONS);
    }

    @Test
    void rightStackTest() {
        Rightstack rightStack = rightstack(
                line("top", "line", "with", "more", "code"),
                line("bottom", "line")
        );
        SVGCanvas c = canvasBuilder.generateSVG(rightStack);
        String result = c.generateSVG();
        Approvals.verify(result, OPTIONS);
    }

    @Test
    void titleTest() {
        Unit root = line("/create_grain", loop(";",
                choice("/create_sequence",
                        "/create_table",
                        "/add_foreign_key",
                        "/create_index",
                        "/create_view",
                        "/create_materialized_view",
                        "/create_function"))
        );

        SVGCanvas c = canvasBuilder.withTitle("TestTitle").generateSVG(root);
        canvasBuilder.withTitle(null);
        String result = c.generateSVG();
        Approvals.verify(result, OPTIONS);
    }
}