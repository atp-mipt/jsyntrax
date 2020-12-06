package org.atpfivt.jsyntrax;

import org.approvaltests.Approvals;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
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


<<<<<<< HEAD
=======
import java.util.Collections;

>>>>>>> added approval tests
import static org.atpfivt.jsyntrax.groovy_parser.SyntraxScript.*;


public class JSyntraxTest {
    @Test
    void lineTest(){
        Line l = line('[', "foo", ',', "/bar", ']');
<<<<<<< HEAD
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(l);
=======
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(l, Collections.emptyMap());
>>>>>>> added approval tests
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void difficultLoopTest(){
        Loop l = loop(line("/forward", "path"), line("backward", "path"));
<<<<<<< HEAD
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(l);
=======
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(l, Collections.emptyMap());
>>>>>>> added approval tests
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void toploopTest(){
        Toploop l = toploop(line('(', "forward", ')'), line(')', "backward", '('));
<<<<<<< HEAD
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(l);
=======
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(l, Collections.emptyMap());
>>>>>>> added approval tests
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void choiceTest(){
        Choice choice = choice('A', 'B', 'C');
<<<<<<< HEAD
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(choice);
=======
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(choice, Collections.emptyMap());
>>>>>>> added approval tests
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void optTest(){
        Opt opt = opt('A', 'B', 'C');
<<<<<<< HEAD
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(opt);
=======
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(opt, Collections.emptyMap());
>>>>>>> added approval tests
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void optxTest(){
        Optx optx = optx('A', 'B', 'C');
<<<<<<< HEAD
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(optx);
=======
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(optx, Collections.emptyMap());
>>>>>>> added approval tests
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void stackTest(){
        Stack s = stack(
                line("top", "line"),
                line("bottom", "line")
        );
<<<<<<< HEAD
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(s);
=======
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(s, Collections.emptyMap());
>>>>>>> added approval tests
        String result = c.generateSVG();
        Approvals.verify(result);
    }


    @Test
    void verticalStackTest(){
        Stack s = stack(
                line('A', 'B'),
                opt("bypass"),
                line("finish")
        );
<<<<<<< HEAD
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(s);
=======
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(s, Collections.emptyMap());
>>>>>>> added approval tests
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void indentStackTest(){
        Indentstack indentStack = indentstack(3,
                line("top", "line"),
                line("bottom", "line")
        );
<<<<<<< HEAD
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(indentStack);
=======
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(indentStack, Collections.emptyMap());
>>>>>>> added approval tests
        String result = c.generateSVG();
        Approvals.verify(result);
    }

    @Test
    void rightStackTest(){
        Rightstack rightStack = rightstack(
                line("top", "line", "with", "more", "code"),
                line("bottom", "line")
        );
<<<<<<< HEAD
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(rightStack);
=======
        SVGCanvas c = new SVGCanvasBuilder().generateSVG(rightStack, Collections.emptyMap());
>>>>>>> added approval tests
        String result = c.generateSVG();
        Approvals.verify(result);
    }
}