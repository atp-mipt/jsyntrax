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
import groovy.json.internal.ArrayUtils;
import sun.font.FontDesignMetrics;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @brief class for building canvas by Unit
 */
public class SVGCanvasBuilder {
    public SVGCanvasBuilder() {}

    public SVGCanvas generateSVG(Unit unit) {
        this.style = new Style();
        this.canvas = new SVGCanvas(this.style);
        this.parseDiagram(unit, true);
        return this.canvas;
    }

    /**
     * @return tag of built Unit
     */
    private UnitEndPoint parseDiagram(Unit unit, boolean ltor) {
        if (null == unit) {
            return null;
        }

        if (unit instanceof NoneNode) {
            return parseNoneNode((NoneNode) unit);
        }

        if (unit instanceof Node) {
            return parseNode((Node) unit);
        }

        if (unit instanceof Bullet) {
            return parseBullet((Bullet)unit);
        }

        if (unit instanceof Line) {
            return parseLine((Line) unit, ltor);
        }

        if (unit instanceof Toploop) {
            return parseToploop((Toploop) unit, ltor);
        }

        if (unit instanceof Loop) {
            return parseLoop((Loop) unit, ltor);
        }

        if (unit instanceof Choice) {
            return parseChoice((Choice) unit, ltor);
        }

        if (unit instanceof Optx) {
            return parseOptx((Optx) unit, ltor);
        }

        if (unit instanceof Opt) {
            return parseOpt((Opt) unit, ltor);
        }

        if (unit instanceof Indentstack) {
            return parseIndentstack((Indentstack) unit, ltor);
        }

        if (unit instanceof Rightstack) {
            return parseRightstack((Rightstack) unit, ltor);
        }

        if (unit instanceof Stack) {
            return parseStack((Stack) unit, ltor);
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

        Pair<Integer, Integer> textSize = getTextSize(txt, font);
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

    private UnitEndPoint parseBullet(Bullet bullet) {
        String tag = this.canvas.new_tag("x", "");
        int w = this.style.outline_width;
        int r = w + 1;
        this.canvas.addElement(
                new OvalElement(new Pair<>(0, -r), new Pair<>(2 * r, r),
                        w, this.style.bullet_fill, tag));
        return new UnitEndPoint(tag, new Pair<>(2 * r, 0));
    }

    private UnitEndPoint parseLine(Line line, boolean ltor) {
        String tag = this.canvas.new_tag("x", "");

        int sep = this.style.h_sep;
        int width = this.style.line_width;
        Pair<Integer, Integer> pos = new Pair<> (0, 0);

        ArrayList<Unit> units = line.getUnits();

        int unitNum = 0;
        int unitStep = 1;
        int size = line.getUnits().size();

        if (!ltor) {
            unitNum = size - 1;
            unitStep = -1;
        }

        for (;0 <= unitNum && unitNum < size; unitNum += unitStep) {
            Unit unit = line.getUnits().get(unitNum);
            UnitEndPoint endPoint = this.parseDiagram(unit, ltor);
            if (endPoint == null) {
                continue;
            }

            if (pos.f != 0) {
                // has element before
                int xn = pos.f + sep;
                this.canvas.moveByTag(endPoint.tag, xn, pos.s);
                // create line from previous to this
                LineElement l = new LineElement(new Pair<>(pos.f - 1, pos.s), new Pair<>(xn, pos.s),
                        ltor ? "last" : "first", width, tag);
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

    private UnitEndPoint parseToploop(Toploop loop, boolean ltor) {
        String tag = this.canvas.new_tag("x", "");

        int sep = this.style.v_sep;
        int vsep = sep / 2;

        // parse forward
        UnitEndPoint fEndPoint = parseDiagram(loop.getForwardPart(), ltor);
        String ft = fEndPoint.tag;
        int fexx = fEndPoint.endpoint.f;
        int fexy = fEndPoint.endpoint.s;
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> fBox = this.canvas.getBoundingBoxByTag(ft);
        int fx0 = fBox.f.f;
        int fy0 = fBox.f.s;
        int fx1 = fBox.s.f;
        int fy1 = fBox.s.s;

        // parse backward
        UnitEndPoint bEndPoint = parseDiagram(loop.getBackwardPart(), !ltor);
        String bt = bEndPoint.tag;
        int bexx = bEndPoint.endpoint.f;
        int bexy = bEndPoint.endpoint.s;
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> bBox = this.canvas.getBoundingBoxByTag(bt);
        int bx0 = bBox.f.f;
        int by0 = bBox.f.s;
        int bx1 = bBox.s.f;
        int by1 = bBox.s.s;

        // move processes
        int fw = fx1 - fx0;
        int bw = bx1 - bx0;
        int dy = -(by1 - fy0 + vsep);
        this.canvas.moveByTag(bt, 0, dy);
        int biny = dy;
        bexy += dy;
        by0 += dy;
        by1 += dy;
        int mxx = 0;

        int dx = Math.abs(fw - bw) / 2;

        if (fw > bw) {
            this.canvas.moveByTag(bt, dx, 0);
            bexx += dx;
            this.canvas.addElement(
                    new LineElement(new Pair<>(0, biny), new Pair<>(dx, biny),
                            null, this.style.line_width, bt));
            this.canvas.addElement(
                    new LineElement(new Pair<>(bexx, bexy), new Pair<>(fx1, bexy),
                            (ltor || dx < 2 * vsep ? null : "first"), this.style.line_width, bt));
            mxx = fexx;
        }
        else if (bw > fw) {
            this.canvas.moveByTag(ft, dx, 0);
            fexx += dx;
            this.canvas.addElement(
                    new LineElement(new Pair<>(0, 0), new Pair<>(dx, fexy),
                            null, this.style.line_width, ft));
            this.canvas.addElement(
                    new LineElement(new Pair<>(fexx, fexy), new Pair<>(bx1, fexy),
                            null, this.style.line_width, ft));
            mxx = bexx;
        }
        else {
            mxx = fexx;
        }

        // Retag
        this.canvas.addTagByTag(tag, bt);
        this.canvas.addTagByTag(tag, ft);
        this.canvas.dropTag(bt);
        this.canvas.dropTag(ft);

        // move for left turnback
        this.canvas.moveByTag(tag, sep, 0);
        mxx += sep;
        this.canvas.addElement(
                new LineElement(new Pair<>(0, 0), new Pair<>(sep, 0),
                        null, this.style.line_width, tag));

        drawLeftTurnBack(tag, sep, 0, biny, ltor ? "up" : "down");
        drawRightTurnBack(tag, mxx, fexy, bexy, ltor ? "down" : "up");

        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> box = this.canvas.getBoundingBoxByTag(tag);
        int x1 = box.s.f;

        this.canvas.addElement(
                new LineElement(new Pair<>(mxx, fexy), new Pair<>(x1, fexy),
                        null, this.style.line_width, tag));
        return new UnitEndPoint(tag, new Pair<>(x1, fexy));
    }

    // TODO: impl
    private UnitEndPoint parseLoop(Loop loop, boolean ltor) {
        return null;
    }

    // TODO: impl
    private UnitEndPoint parseChoice(Choice choice, boolean ltor) {
        return null;
    }

    // TODO: impl
    private UnitEndPoint parseOptx(Optx opt, boolean ltor) {
        return null;
    }

    // TODO: impl
    private UnitEndPoint parseOpt(Opt opt, boolean ltor) {
        return null;
    }

    // TODO: impl
    private UnitEndPoint parseIndentstack(Indentstack stack, boolean ltor) {
        return null;
    }

    // TODO: impl
    private UnitEndPoint parseRightstack(Rightstack stack, boolean ltor) {
        return null;
    }

    // TODO: impl
    private UnitEndPoint parseStack(Stack stack, boolean ltor) {
        return null;
    }

    private void drawLeftTurnBack(String tag, int x, int y0_, int y1_, String flow) {
        int y0 = Math.min(y0_, y1_);
        int y1 = Math.max(y0_, y1_);

        if (y1 - y0 > 3 * this.style.max_radius) {
            int xr0 = x - this.style.max_radius;
            int xr1 = x + this.style.max_radius;
            this.canvas.addElement(
                    new ArcElement(new Pair<>(xr0, y0), new Pair<>(xr1, y0 + 2 * this.style.max_radius),
                            this.style.line_width, 90, 90, tag));
            int yr0 = y0 + this.style.max_radius;
            int yr1 = y1 - this.style.max_radius;
            if (Math.abs(yr1 - yr0) > 2 * this.style.max_radius) {
                int half_y = (yr0 + yr1) / 2;
                if (flow.equals("down")) {
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr0, yr0), new Pair<>(xr0, half_y),
                                    "last", this.style.line_width, tag));
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr0, half_y), new Pair<>(xr0, yr1),
                                    null, this.style.line_width, tag));
                }
                else {
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr0, yr1), new Pair<>(xr0, half_y),
                                    "last", this.style.line_width, tag));
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr0, half_y), new Pair<>(xr0, yr0),
                                    null, this.style.line_width, tag));
                }
            }
            else {
                this.canvas.addElement(
                        new LineElement(new Pair<>(xr0, yr0), new Pair<>(xr0, yr1),
                                null, this.style.line_width, tag));
            }

            this.canvas.addElement(
                    new ArcElement(new Pair<>(xr0, y1 - 2 * this.style.max_radius), new Pair<>(xr1, y1),
                            this.style.line_width, 180, 90, tag));
        }
        else {
            int r = (y1 - y0) / 2;
            int x0 = x - r;
            int x1 = x + r;
            this.canvas.addElement(
                    new ArcElement(new Pair<>(x0, y0), new Pair<>(x1, y1),
                            this.style.line_width, 90, 180, tag));
        }
    }

    private void drawRightTurnBack(String tag, int x, int y0_, int y1_, String flow) {
        int y0 = Math.min(y0_, y1_);
        int y1 = Math.max(y0_, y1_);

        if (y1 - y0 > 3 * this.style.max_radius) {
            int xr0 = x - this.style.max_radius;
            int xr1 = x + this.style.max_radius;

            this.canvas.addElement(
                    new ArcElement(new Pair<>(xr0, y0), new Pair<>(xr1, y0 + 2 * this.style.max_radius),
                            this.style.line_width, 90, -90, tag));
            int yr0 = y0 + this.style.max_radius;
            int yr1 = y1 - this.style.max_radius;

            if (Math.abs(yr1 - yr0) > 2 * this.style.max_radius) {
                int half_y = (yr1 + yr0) / 2;
                if (flow.equals("down")) {
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr1, yr0), new Pair<>(xr1, half_y),
                                    "last", this.style.line_width, tag));
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr1, half_y), new Pair<>(xr1, yr1),
                                    null, this.style.line_width, tag));
                }
                else {
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr1, yr1), new Pair<>(xr1, half_y),
                                    "last", this.style.line_width, tag));
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr1, half_y), new Pair<>(xr1, yr0),
                                    null, this.style.line_width, tag));
                }
            }
            else {
                this.canvas.addElement(
                        new LineElement(new Pair<>(xr1, yr0), new Pair<>(xr1, yr1),
                                null, this.style.line_width, tag));
            }

            this.canvas.addElement(
                    new ArcElement(new Pair<>(xr0, y1 - 2 * this.style.max_radius), new Pair<>(xr1, y1),
                            this.style.line_width, 0, -90, tag));
        }
        else {
            int r = (y1 - y0) / 2;
            int x0 = x - r;
            int x1 = x + r;
            this.canvas.addElement(
                    new ArcElement(new Pair<>(x0, y0), new Pair<>(x1, y1),
                            this.style.line_width, 90, -180, tag));
        }
    }

    private static Pair<Integer, Integer> getTextSize(String text, Font font) {
        FontMetrics metrics = FontDesignMetrics.getMetrics(font);

        // TODO: dirty...
        return new Pair<>(metrics.stringWidth(text) + 10, metrics.getHeight() + 10);
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
    private SVGCanvas canvas;
}
