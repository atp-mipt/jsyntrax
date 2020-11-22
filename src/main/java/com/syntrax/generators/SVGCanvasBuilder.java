package com.syntrax.generators;

import com.syntrax.generators.elements.*;
import com.syntrax.styles.NodeStyle;
import com.syntrax.styles.Style;
import com.syntrax.units.Unit;
import com.syntrax.units.nodes.*;
import com.syntrax.units.tracks.Choice;
import com.syntrax.units.tracks.loop.*;
import com.syntrax.units.tracks.opt.*;
import com.syntrax.units.tracks.stack.*;
import com.syntrax.units.tracks.*;
import com.syntrax.util.Pair;
import org.jfree.graphics2d.svg.SVGGraphics2D;

import java.awt.*;

/**
 * @brief class for building canvas by Unit
 */
public class SVGCanvasBuilder {
    public SVGCanvasBuilder() {}

    public Canvas generateSVG(Unit unit) {
        this.style = new Style();
        this.canvas = new Canvas(this.style);
        this.parseDiagram(unit);
        return this.canvas;
    }

    /**
     * @return tag of built Unit
     */
    private UnitEndPoint parseDiagram(Unit unit) {
        if (null == unit) {
            return null;
        }

        if (unit instanceof NoneNode) {
            return parseNoneNode((NoneNode) unit);
        }

        if (unit instanceof Node) {
            return parseNode((Node) unit);
        }

        if (unit instanceof Line) {
            return parseLine((Line) unit);
        }

        if (unit instanceof Toploop) {
            return parseToploop((Toploop) unit);
        }

        if (unit instanceof Loop) {
            return parseLoop((Loop) unit);
        }

        if (unit instanceof Choice) {
            return parseChoice((Choice) unit);
        }

        if (unit instanceof Optx) {
            return parseOptx((Optx) unit);
        }

        if (unit instanceof Opt) {
            return parseOpt((Opt) unit);
        }

        if (unit instanceof Indentstack) {
            return parseIndentstack((Indentstack) unit);
        }

        if (unit instanceof Rightstack) {
            return parseRightstack((Rightstack) unit);
        }

        if (unit instanceof Stack) {
            return parseStack((Stack) unit);
        }

        return null;
    }

    private UnitEndPoint parseNoneNode(NoneNode node) {
        String tag = this.canvas.new_tag("x", "");

        Element e = new LineElement(new Pair<>(0, 0), new Pair<>(1, 0),
                null, this.style.outline_width, tag);
        e.setStart(new Pair<Integer, Integer>(0, 0));
        e.setEnd(new Pair<Integer, Integer>(1, 0));
        this.canvas.addElement(e);

        return new UnitEndPoint(tag, e.getEnd());
    }

    private UnitEndPoint parseNode(Node node) {
        String txt = node.toString();

        NodeStyle ns = this.style.getNodeStyle(txt);
        txt = ns.modify(txt);

        Font font = ns.font;
        String fontName = ns.name + "_font";

        Color fill = ns.fill;
        Color textColor = ns.text_color;

        // TODO: add url mapping

        Pair<Integer, Integer> textSize = TextElement.getTextSize(txt, font);
        int x0 = -textSize.f / 2;
        int y0 = -textSize.s / 2;
        int x1 = x0 + textSize.f;
        int y1 = y0 + textSize.s;

        int h = y1 - y0 + 1;
        int rad = (h + 1) / 2;

        int lft = x0;
        int rgt = x1;
        int btm = y1;
        int top = btm - 2 * rad;

        if (ns.shape.equals("bubble") || ns.shape.equals("hex")) {
            lft += rad / 2 - 2;
            rgt -= rad / 2 - 2;
        }
        else {
            lft -= 5;
            rgt += 5;
        }

        if (lft > rgt) {
            // TODO: they are equal ???
            lft = (x0 + x1) / 2;
            rgt = lft;
        }

        String tag = this.canvas.new_tag("x", "-box");

        Pair<Integer, Integer> start;
        Pair<Integer, Integer> end;

        BubbleElementBase b;
        switch (ns.shape) {
            case "bubble":
                start = new Pair<>(lft - rad, top);
                end = new Pair<>(rgt + rad, btm);
                b = new BubbleElement(start, end, txt, new Pair<>(x0, y0), font,
                        fontName, textColor, this.style.outline_width, fill, tag);
                break;
            case "hex":
                start = new Pair<>(lft - rad, top);
                end = new Pair<>(rgt + rad, btm);
                b = new HexBubbleElement(start, end, txt, new Pair<>(x0, y0), font,
                        fontName, textColor, this.style.outline_width, fill, tag);
                break;
            default:
                start = new Pair<>(lft, top);
                end = new Pair<>(rgt, btm);
                b = new BoxBubbleElement(start, end, txt, new Pair<>(x0, y0), font,
                        fontName, textColor, this.style.outline_width, fill, tag);
                break;
        }
        this.canvas.addElement(b);

        x0 = start.f;
        x1 = end.f;

        int width = x1 - x0;

        this.canvas.moveByTag(tag, -x0, 2);

        //c.tag_raise(id1) # Bring text above any filled bubbles
        
        return new UnitEndPoint(tag, new Pair<>(width, 0));
    }

    private UnitEndPoint parseLine(Line line) {
        String tag = this.canvas.new_tag("x", "");

        int sep = this.style.h_sep;
        int width = this.style.line_width;
        Pair<Integer, Integer> pos = new Pair<> (0, 0);

        for (Unit unit : line.getUnits()) {
            UnitEndPoint endPoint = this.parseDiagram(unit);
            if (endPoint == null) {
                continue;
            }

            if (pos.f != 0) {
                // has element before
                int xn = pos.f + sep;
                this.canvas.moveByTag(endPoint.tag, xn, pos.s);
                // create line from previous to this
                LineElement l = new LineElement(new Pair<>(pos.f - 1, pos.s + 2), new Pair<>(xn, pos.s + 2),
                        "last", width, tag);
                this.canvas.addElement(l);
                pos.f = xn + endPoint.endpoint.f;
            }
            else {
                // first element on line
                pos.f = endPoint.endpoint.f;
            }
            pos.s = endPoint.endpoint.s;

            // tag this unit
            this.canvas.addTagByTag(tag, endPoint.tag);
            // delete old tag
            this.canvas.dropTag(endPoint.tag);
        }

        if (pos.f == 0) {
            // line is empty
            pos.f = sep * 2;
            this.canvas.addElement(
                    new LineElement(new Pair<>(0, 0), new Pair<>(sep, 0),
                            null, width, tag));
            this.canvas.addElement(
                    new LineElement(new Pair<>(sep, 0), new Pair<>(pos.f, 0),
                            "last", width, tag));
            pos.f = sep;
        }

        return new UnitEndPoint(tag, pos);
    }

    // TODO: impl
    private UnitEndPoint parseToploop(Toploop loop) {
        return null;
    }

    // TODO: impl
    private UnitEndPoint parseLoop(Loop loop) {
        return null;
    }

    // TODO: impl
    private UnitEndPoint parseChoice(Choice choice) {
        return null;
    }

    // TODO: impl
    private UnitEndPoint parseOptx(Optx opt) {
        return null;
    }

    // TODO: impl
    private UnitEndPoint parseOpt(Opt opt) {
        return null;
    }

    // TODO: impl
    private UnitEndPoint parseIndentstack(Indentstack stack) {
        return null;
    }

    // TODO: impl
    private UnitEndPoint parseRightstack(Rightstack stack) {
        return null;
    }

    // TODO: impl
    private UnitEndPoint parseStack(Stack stack) {
        return null;
    }

    /**
     * @brief class for return in parse functions
     * @details contain pair of tag end endpoint
     */
    private static class UnitEndPoint {
        UnitEndPoint(String tag, Pair<Integer, Integer> endpoint) {
            this.tag = tag;
            this.endpoint = endpoint;
        }

        public String tag;
        public Pair<Integer, Integer> endpoint;
    }

    private Style style;
    private Canvas canvas;
}
