package org.atpfivt.jsyntrax.generators;

import org.atpfivt.jsyntrax.Configuration;
import org.atpfivt.jsyntrax.generators.elements.ArcElement;
import org.atpfivt.jsyntrax.generators.elements.BoxBubbleElement;
import org.atpfivt.jsyntrax.generators.elements.BubbleElement;
import org.atpfivt.jsyntrax.generators.elements.BubbleElementBase;
import org.atpfivt.jsyntrax.generators.elements.Element;
import org.atpfivt.jsyntrax.generators.elements.HexBubbleElement;
import org.atpfivt.jsyntrax.generators.elements.LineElement;
import org.atpfivt.jsyntrax.generators.elements.OvalElement;
import org.atpfivt.jsyntrax.generators.elements.TitleElement;
import org.atpfivt.jsyntrax.styles.NodeStyle;
import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.units.nodes.Bullet;
import org.atpfivt.jsyntrax.units.nodes.Node;
import org.atpfivt.jsyntrax.units.nodes.NoneNode;
import org.atpfivt.jsyntrax.units.tracks.Choice;
import org.atpfivt.jsyntrax.units.tracks.Line;
import org.atpfivt.jsyntrax.units.tracks.loop.Loop;
import org.atpfivt.jsyntrax.units.tracks.loop.Toploop;
import org.atpfivt.jsyntrax.units.tracks.opt.Opt;
import org.atpfivt.jsyntrax.units.tracks.opt.Optx;
import org.atpfivt.jsyntrax.units.tracks.stack.Indentstack;
import org.atpfivt.jsyntrax.units.tracks.stack.Rightstack;
import org.atpfivt.jsyntrax.units.tracks.stack.Stack;
import org.atpfivt.jsyntrax.util.Pair;
import org.atpfivt.jsyntrax.visitors.Visitor;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @brief class for building canvas by Unit
 */
public final class SVGCanvasBuilder implements Visitor {
    private Map<String, String> urlMap;
    private StyleConfig style;
    private SVGCanvas canvas;

    public SVGCanvasBuilder()
            throws IllegalAccessException, NoSuchFieldException, IOException {
        this.style = new StyleConfig(1, false);
        this.urlMap = Collections.emptyMap();
    }

    public SVGCanvasBuilder withStyle(StyleConfig style) {
        this.style = style;
        return this;
    }

    public SVGCanvasBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public SVGCanvas generateSVG(Unit root) {
        this.canvas = new SVGCanvas(this.style);
        parseDiagram(new Line(
                new ArrayList<>(List.of(new Bullet(), root, new Bullet()))
        ), true);

        if (title == null) {
            return canvas;
        }

        String tag = canvas.getCanvasTag().orElse(null);
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> bbox =
                canvas.getBoundingBoxByTag(tag);

        String titleTag = canvas.newTag("x", "-title");
        Element e = new TitleElement(
                title,
                new Font(style.getTitleFont().getFamily(), style.getTitleFont().getStyle(),
                        style.getTitleFont().getSize() + 6),
                "title_font",
                titleTag
        );
        canvas.addElement(e);

        // set left / middle / right location
        switch (style.getTitlePos()) {
            case bl:
            case tl:
                canvas.moveByTag(titleTag, 2 * style.getPadding(), 0);
                break;
            case bm:
            case tm:
                canvas.moveByTag(titleTag,
                        (bbox.f.f + bbox.s.f - e.getEnd().f) / 2
                                - 2 * style.getPadding(), 0);
                break;
            case br:
            case tr:
                canvas.moveByTag(titleTag,
                        bbox.s.f - e.getEnd().f - 2 * style.getPadding(), 0);
                break;
        }

        // set top / bottom location
        switch (style.getTitlePos()) {
            case tl:
            case tm:
            case tr:
                canvas.moveByTag(tag, 0, e.getEnd().s + 2 * style.getPadding());
                break;
            case bl:
            case bm:
            case br:
                canvas.moveByTag(titleTag, 0, bbox.s.s + 2 * style.getPadding());
                break;
        }
        return canvas;
    }

    /**
     * @return tag of built Unit
     */
    private void parseDiagram(Unit unit, boolean ltor) {
        if (null == unit) {
            setUnitEndPoint(null);
            return;
        }

        setLtor(ltor);
        unit.accept(this);
    }

    /**
     * @return tag of built Unit
     */
    private UnitEndPoint getDiagramParseResult(Unit unit, boolean ltor) {
        parseDiagram(unit, ltor);
        return getUnitEndPoint();
    }

    @Override
    public void visitNoneNode(NoneNode unit) {
        String tag = this.canvas.newTag("x", "");

        Element e = new LineElement(new Pair<>(0, 0), new Pair<>(1, 0),
                null, this.style.getOutlineWidth(), tag);
        e.setStart(new Pair<>(0, 0));
        e.setEnd(new Pair<>(1, 0));
        this.canvas.addElement(e);

        setUnitEndPoint(new UnitEndPoint(tag, e.getEnd()));
    }

    @Override
    public void visitConfiguration(Configuration unit) {
        urlMap = unit.getUrlMap();
        unit.getTrack().accept(this);
    }

    @Override
    public void visitNode(Node unit) {
        String txt = unit.toString();

        NodeStyle ns = this.style.getNodeStyle(txt);
        txt = ns.unwrapTextContent(txt);

        Font font = ns.getFont();
        String fontName = ns.getName() + "_font";

        Color fill = ns.getFill();
        Color textColor = ns.getTextColor();

        Pair<Integer, Integer> textSize = getTextSize(txt, font);
        int x0 = -textSize.f / 2;
        int y0 = -textSize.s / 2;
        int x1 = x0 + textSize.f;
        int y1 = y0 + textSize.s;

        int h = y1 - y0 + 1;
        int rad = (h + 1) / 2;

        int lft = x0;
        int rgt = x1;
        int top = y1 - 2 * rad;

        if (ns.getShape().equals("bubble") || ns.getShape().equals("hex")) {
            lft += rad / 2 - 2;
            rgt -= rad / 2 - 2;
        } else {
            lft -= 5;
            rgt += 5;
        }

        if (lft > rgt) {
            lft = (x0 + x1) / 2;
            rgt = lft;
        }

        String tag = this.canvas.newTag("x", "-box");
        Pair<Integer, Integer> start;
        Pair<Integer, Integer> end;

        BubbleElementBase b;
        String href = this.urlMap.get(txt);
        switch (ns.getShape()) {
            case "bubble":
                start = new Pair<>(lft - rad, top);
                end = new Pair<>(rgt + rad, y1);
                b = new BubbleElement(start, end, href,
                        txt, new Pair<>(x0, y0), font,
                        fontName, textColor, this.style.getOutlineWidth(), fill, tag);
                break;
            case "hex":
                start = new Pair<>(lft - rad, top);
                end = new Pair<>(rgt + rad, y1);
                b = new HexBubbleElement(start, end, href,
                        txt, new Pair<>(x0, y0), font,
                        fontName, textColor, this.style.getOutlineWidth(), fill, tag);
                break;
            default: {
                start = new Pair<>(lft, top);
                end = new Pair<>(rgt, y1);
                b = new BoxBubbleElement(start, end, href,
                        txt, new Pair<>(x0, y0), font,
                        fontName, textColor, this.style.getOutlineWidth(), fill, tag);
                break;
            }
        }
        this.canvas.addElement(b);

        x0 = start.f;
        x1 = end.f;
        int width = x1 - x0;
        this.canvas.moveByTag(tag, -x0, 2);
        setUnitEndPoint(new UnitEndPoint(tag, new Pair<>(width, 0)));
    }

    @Override
    public void visitBullet(Bullet unit) {
        String tag = this.canvas.newTag("x", "");
        int w = this.style.getOutlineWidth();
        int r = w + 1;
        this.canvas.addElement(
                new OvalElement(new Pair<>(0, -r), new Pair<>(2 * r, r),
                        w, this.style.getBulletFill(), tag));

        setUnitEndPoint(new UnitEndPoint(tag, new Pair<>(2 * r, 0)));
    }

    @Override
    public void visitLine(Line line) {
        boolean ltor = getLtor();
        String tag = this.canvas.newTag("x", "");

        int sep = this.style.getHSep();
        int width = this.style.getLineWidth();
        Pair<Integer, Integer> pos = new Pair<>(0, 0);

        int unitNum = 0;
        int unitStep = 1;
        int size = line.getUnits().size();

        if (!ltor) {
            unitNum = size - 1;
            unitStep = -1;
        }

        for (; 0 <= unitNum && unitNum < size; unitNum += unitStep) {
            Unit unit = line.getUnits().get(unitNum);
            UnitEndPoint endPoint = this.getDiagramParseResult(unit, ltor);
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
            } else {
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

        setUnitEndPoint(new UnitEndPoint(tag, pos));
    }

    @Override
    public void visitToploop(Toploop loop) {
        boolean ltor = getLtor();
        String tag = this.canvas.newTag("x", "");

        int sep = this.style.getVSep();
        int vsep = sep / 2;

        // parse forward
        UnitEndPoint fEndPoint = getDiagramParseResult(loop.getForwardPart(), ltor);
        String ft = fEndPoint.tag;
        int fexx = fEndPoint.endpoint.f;
        int fexy = fEndPoint.endpoint.s;
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> fBox = this.canvas.getBoundingBoxByTag(ft);
        int fx0 = fBox.f.f;
        int fy0 = fBox.f.s;
        int fx1 = fBox.s.f;

        // parse backward
        UnitEndPoint bEndPoint = getDiagramParseResult(loop.getBackwardPart(), !ltor);
        String bt = bEndPoint.tag;
        int bexx = bEndPoint.endpoint.f;
        int bexy = bEndPoint.endpoint.s;
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> bBox = this.canvas.getBoundingBoxByTag(bt);
        int bx0 = bBox.f.f;
        int bx1 = bBox.s.f;
        int by1 = bBox.s.s;

        // move processes
        int fw = fx1 - fx0;
        int bw = bx1 - bx0;
        int dy = -(by1 - fy0 + vsep);
        this.canvas.moveByTag(bt, 0, dy);
        bexy += dy;
        int mxx;

        int dx = Math.abs(fw - bw) / 2;

        if (fw > bw) {
            this.canvas.moveByTag(bt, dx, 0);
            bexx += dx;
            this.canvas.addElement(
                    new LineElement(new Pair<>(0, dy), new Pair<>(dx, dy),
                            null, this.style.getLineWidth(), bt));
            this.canvas.addElement(
                    new LineElement(
                            new Pair<>(bexx, bexy),
                            new Pair<>(fx1, bexy),
                            ltor || dx < 2 * vsep ? null : "first",
                            this.style.getLineWidth(), bt));
            mxx = fexx;
        } else if (bw > fw) {
            this.canvas.moveByTag(ft, dx, 0);
            fexx += dx;
            this.canvas.addElement(
                    new LineElement(
                            new Pair<>(0, 0),
                            new Pair<>(dx, fexy),
                            null, this.style.getLineWidth(), ft));
            this.canvas.addElement(
                    new LineElement(
                            new Pair<>(fexx, fexy),
                            new Pair<>(bx1, fexy),
                            null, this.style.getLineWidth(), ft));
            mxx = bexx;
        } else {
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
                        null, this.style.getLineWidth(), tag));

        drawLeftTurnBack(tag, sep, 0, dy, ltor ? "up" : "down");
        drawRightTurnBack(tag, mxx, fexy, bexy, ltor ? "down" : "up");

        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> box = this.canvas.getBoundingBoxByTag(tag);
        int x1 = box.s.f;

        this.canvas.addElement(
                new LineElement(new Pair<>(mxx, fexy), new Pair<>(x1, fexy),
                        null, this.style.getLineWidth(), tag));

        setUnitEndPoint(new UnitEndPoint(tag, new Pair<>(x1, fexy)));
    }

    @Override
    public void visitLoop(Loop loop) {
        boolean ltor = getLtor();
        String tag = this.canvas.newTag("x", "");
        int sep = this.style.getVSep();
        int vsep = sep / 2;

        // parse forward
        UnitEndPoint fEndPoint = getDiagramParseResult(loop.getForwardPart(), ltor);
        String ft = fEndPoint.tag;
        int fexx = fEndPoint.endpoint.f;
        int fexy = fEndPoint.endpoint.s;
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> fBox = this.canvas.getBoundingBoxByTag(ft);
        int fx0 = fBox.f.f;
        int fx1 = fBox.s.f;
        int fy1 = fBox.s.s;

        // parse backward
        UnitEndPoint bEndPoint = getDiagramParseResult(loop.getBackwardPart(), !ltor);
        String bt = bEndPoint.tag;
        int bexx = bEndPoint.endpoint.f;
        int bexy = bEndPoint.endpoint.s;
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> bBox = this.canvas.getBoundingBoxByTag(bt);
        int bx0 = bBox.f.f;
        int by0 = bBox.f.s;
        int bx1 = bBox.s.f;

        // move processes
        int fw = fx1 - fx0;
        int bw = bx1 - bx0;
        int dy = fy1 - by0 + vsep;
        this.canvas.moveByTag(bt, 0, dy);
        bexy += dy;
        int mxx;

        if (fw > bw) {
            int dx;
            if (fexx < fw && fexx >= bw) {
                dx = (fexx - bw) / 2;
                this.canvas.moveByTag(bt, dx, 0);
                bexx += dx;
                this.canvas.addElement(
                        new LineElement(new Pair<>(0, dy), new Pair<>(dx, dy),
                                null, this.style.getLineWidth(), bt));
                this.canvas.addElement(
                        new LineElement(new Pair<>(bexx, bexy), new Pair<>(fexx, bexy),
                                "first", this.style.getLineWidth(), bt));
            } else {
                dx = (fw - bw) / 2;
                this.canvas.moveByTag(bt, dx, 0);
                bexx += dx;

                this.canvas.addElement(
                        new LineElement(new Pair<>(0, dy), new Pair<>(dx, dy),
                                ltor || dx < 2 * vsep ? null : "last", this.style.getLineWidth(), bt));
                this.canvas.addElement(
                        new LineElement(new Pair<>(bexx, bexy), new Pair<>(fx1, bexy),
                                !ltor || dx < 2 * vsep ? null : "first", this.style.getLineWidth(), bt));
            }
            mxx = fexx;
        } else if (bw > fw) {
            int dx = (bw - fw) / 2;
            this.canvas.moveByTag(ft, dx, 0);
            fexx += dx;
            this.canvas.addElement(
                    new LineElement(new Pair<>(0, 0), new Pair<>(dx, fexy),
                            ltor ? "last" : "first", this.style.getLineWidth(), ft));
            this.canvas.addElement(
                    new LineElement(new Pair<>(fexx, fexy), new Pair<>(bx1, fexy),
                            null, this.style.getLineWidth(), ft));
            mxx = bexx;
        } else {
            mxx = fexx;
        }

        // retag
        this.canvas.addTagByTag(tag, bt);
        this.canvas.addTagByTag(tag, ft);
        this.canvas.dropTag(bt);
        this.canvas.dropTag(ft);

        // move for left turnback
        this.canvas.moveByTag(tag, sep, 0);

        mxx += sep;
        this.canvas.addElement(
                new LineElement(new Pair<>(0, 0), new Pair<>(sep, 0),
                        null, this.style.getLineWidth(), tag));

        drawLeftTurnBack(tag, sep, 0, dy, ltor ? "up" : "down");
        drawRightTurnBack(tag, mxx, fexy, bexy, ltor ? "down" : "up");

        int exitX = mxx + this.style.getMaxRadius();
        this.canvas.addElement(
                new LineElement(new Pair<>(mxx, fexy), new Pair<>(exitX, fexy),
                        null, this.style.getLineWidth(), tag));

        setUnitEndPoint(new UnitEndPoint(tag, new Pair<>(exitX, fexy)));
    }

    @Override
    public void visitChoice(Choice choice) {
        boolean ltor = getLtor();
        String tag = this.canvas.newTag("x", "");

        int sep = this.style.getVSep();
        int vsep = sep / 2;

        int n = choice.getUnits().size();

        if (n == 0) {
            setUnitEndPoint(null);
            return;
        }
        ArrayList<UnitEndPoint> res = new ArrayList<>();
        int mxw = 0;

        for (int i = 0; i < n; ++i) {
            res.add(getDiagramParseResult(choice.getUnits().get(i), ltor));

            Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> box =
                    this.canvas.getBoundingBoxByTag(res.get(i).tag);
            int w = box.s.f - box.f.f;
            if (i != 0) {
                w += 20;
            }
            mxw = Math.max(mxw, w);
        }

        int x2 = sep * 2;
        int x3 = mxw + x2;
        int x4 = x3 + sep;
        int x5 = x4 + sep;

        int exy = 0;
        int btm = 0;

        for (int i = 0; i < n; ++i) {
            String t = res.get(i).tag;
            int texx = res.get(i).endpoint.f;
            int texy = res.get(i).endpoint.s;
            Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> box =
                    this.canvas.getBoundingBoxByTag(t);
            int w = box.s.f - box.f.f;
            int dx = (mxw - w) / 2 + x2;
            if (w > 10 && dx > x2 + 10) {
                dx = x2 + 10;
            }
            this.canvas.moveByTag(t, dx, 0);
            texx += dx;
            box = this.canvas.getBoundingBoxByTag(t);
            int ty0 = box.f.s;
            int ty1 = box.s.s;

            if (i == 0) {
                this.canvas.addElement(
                        new LineElement(new Pair<>(0, 0), new Pair<>(dx, 0),
                                ltor && dx > x2 ? "last" : null, this.style.getLineWidth(), tag));
                this.canvas.addElement(
                        new LineElement(new Pair<>(texx, texy), new Pair<>(x5 + 1, texy),
                                ltor ? null : "first", this.style.getLineWidth(), tag));
                exy = texy;
                this.canvas.addElement(
                        new ArcElement(new Pair<>(-sep, 0), new Pair<>(sep, sep * 2),
                                this.style.getLineWidth(), 90, -90, tag));
                btm = ty1;
            } else {
                int dy = Math.max(btm - ty0 + vsep, 2 * sep);
                this.canvas.moveByTag(t, 0, dy);
                texy += dy;
                if (dx > x2) {
                    this.canvas.addElement(
                            new LineElement(new Pair<>(x2, dy), new Pair<>(dx, dy),
                                    ltor ? "last" : null, this.style.getLineWidth(), tag));
                    this.canvas.addElement(
                            new LineElement(new Pair<>(texx, texy), new Pair<>(x3, texy),
                                    ltor ? null : "first", this.style.getLineWidth(), tag));
                }
                int y1 = dy - 2 * sep;
                this.canvas.addElement(
                        new ArcElement(new Pair<>(sep, y1), new Pair<>(sep + 2 * sep, dy),
                                this.style.getLineWidth(), 180, 90, tag));
                int y2 = texy - 2 * sep;
                this.canvas.addElement(
                        new ArcElement(new Pair<>(x3 - sep, y2), new Pair<>(x4, texy),
                                this.style.getLineWidth(), 270, 90, tag));
                if (i + 1 == n) {
                    this.canvas.addElement(
                            new ArcElement(new Pair<>(x4, exy), new Pair<>(x4 + 2 * sep, exy + 2 * sep),
                                    this.style.getLineWidth(), 180, -90, tag));
                    this.canvas.addElement(
                            new LineElement(new Pair<>(sep, dy - sep), new Pair<>(sep, sep),
                                    null, this.style.getLineWidth(), tag));
                    this.canvas.addElement(
                            new LineElement(new Pair<>(x4, texy - sep), new Pair<>(x4, exy + sep),
                                    null, this.style.getLineWidth(), tag));
                }
                btm = ty1 + dy;
            }

            // retag
            this.canvas.addTagByTag(tag, t);
            this.canvas.dropTag(t);
        }

        setUnitEndPoint(new UnitEndPoint(tag, new Pair<>(x5, exy)));
    }

    @Override
    public void visitOptx(Optx opt) {
        boolean ltor = getLtor();
        Choice c = new Choice(new ArrayList<>() {{
            add(new Line(opt.getUnits()));
            add(new NoneNode());
        }});

        parseDiagram(c, ltor);
    }

    @Override
    public void visitOpt(Opt opt) {
        boolean ltor = getLtor();
        Choice c = new Choice(new ArrayList<>() {{
            add(new NoneNode());
            add(new Line(opt.getUnits()));
        }});

        parseDiagram(c, ltor);
    }

    /**
     * Unobvious fact: self.indent < 0 if (stack instanceof Rightstack)
     */
    private void parseStack(Stack stack) {
        boolean ltor = getLtor();
        String tag = this.canvas.newTag("x", "");

        int sep = this.style.getVSep() * 2;
        int btm = 0;
        int n = stack.getUnits().size();
        if (n == 0) {
            setUnitEndPoint(null);
            return;
        }
        int nextBypassY = 0;
        int bypassX = 0;
        int bypassY;
        int exitX = 0;
        int exitY = 0;
        int w;
        int e2;
        int e3;
        int ex2;
        int enterX;
        int enterY;
        int backY;
        int midY;
        int bypass = 0;

        for (int i = 0; i < n; ++i) {
            Unit unit = stack.getUnits().get(i);
            Unit term;
            bypassY = nextBypassY;
            if (i != 0 && indent >= 0 && unit.getUnits().size() != 0 && unit instanceof Opt) {
                bypass = 1;
                term = new Line(unit.getUnits());
            } else {
                bypass = 0;
                term = unit;
                nextBypassY = 0;
            }

            UnitEndPoint ep = getDiagramParseResult(term, ltor);
            String t = ep.tag;
            int exx = ep.endpoint.f;
            int exy = ep.endpoint.s;
            Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> box =
                    this.canvas.getBoundingBoxByTag(t);
            int tx0 = box.f.f;
            int ty0 = box.f.s;
            int tx1 = box.s.f;

            if (i == 0) {
                exitX = exx;
                exitY = exy;
            } else {
                enterY = btm - ty0 + sep * 2 + 2;
                if (bypass == 1) {
                    nextBypassY = enterY - this.style.getMaxRadius();
                }
                if (indent < 0) {
                    w = tx1 - tx0;
                    enterX = exitX - w + sep * indent;
                    ex2 = sep * 2 - indent;
                    if (ex2 > enterX) {
                        enterX = ex2;
                    }
                } else {
                    enterX = sep * 2 + indent;
                }
                backY = btm + sep + 1;

                if (bypassY > 0) {
                    midY = (bypassY + this.style.getMaxRadius() + backY) / 2;
                    this.canvas.addElement(
                            new LineElement(new Pair<>(bypassX, bypassY), new Pair<>(bypassX, midY),
                                    "last", this.style.getLineWidth(), tag));
                    this.canvas.addElement(
                            new LineElement(new Pair<>(bypassX, midY),
                                    new Pair<>(bypassX, backY + this.style.getMaxRadius()),
                                    null, this.style.getLineWidth(), tag));
                }

                this.canvas.moveByTag(t, enterX, enterY);
                e2 = exitX + sep;
                this.canvas.addElement(
                        new LineElement(new Pair<>(exitX, exitY), new Pair<>(e2, exitY),
                                null, this.style.getLineWidth(), tag));
                drawRightTurnBack(tag, e2, exitY, backY, "down");
                e3 = enterX - sep;
                bypassX = e3 - this.style.getMaxRadius();
                int emid = (e2 + e3) / 2;
                this.canvas.addElement(
                        new LineElement(new Pair<>(e2, backY), new Pair<>(emid, backY),
                                "last", this.style.getLineWidth(), tag));
                this.canvas.addElement(
                        new LineElement(new Pair<>(emid, backY), new Pair<>(e3, backY),
                                null, this.style.getLineWidth(), tag));
                drawLeftTurnBack(tag, e3, backY, enterY, "down");
                this.canvas.addElement(
                        new LineElement(new Pair<>(e3, enterY), new Pair<>(enterX, enterY),
                                "last", this.style.getLineWidth(), tag));
                exitX = enterX + exx;
                exitY = enterY + exy;
            }

            // retag
            this.canvas.addTagByTag(tag, t);
            this.canvas.dropTag(t);
            btm = this.canvas.getBoundingBoxByTag(tag).s.s;
        }

        if (bypass == 1) {
            int fwdY = btm + sep + 1;
            midY = (nextBypassY + this.style.getMaxRadius() + fwdY) / 2;
            int descenderX = exitX + this.style.getMaxRadius();
            this.canvas.addElement(
                    new LineElement(new Pair<>(bypassX, nextBypassY), new Pair<>(bypassX, midY),
                            "last", this.style.getLineWidth(), tag));
            this.canvas.addElement(
                    new LineElement(new Pair<>(bypassX, midY),
                            new Pair<>(bypassX, fwdY - this.style.getMaxRadius()),
                            null, this.style.getLineWidth(), tag));
            this.canvas.addElement(
                    new ArcElement(new Pair<>(bypassX, fwdY - 2 * this.style.getMaxRadius()),
                            new Pair<>(bypassX + 2 * this.style.getMaxRadius(), fwdY),
                            this.style.getLineWidth(), 180, 90, tag));
            this.canvas.addElement(
                    new ArcElement(new Pair<>(exitX - this.style.getMaxRadius(), exitY),
                            new Pair<>(descenderX, exitY + 2 * this.style.getMaxRadius()),
                            this.style.getLineWidth(), 90, -90, tag));
            this.canvas.addElement(
                    new ArcElement(new Pair<>(descenderX, fwdY - 2 * this.style.getMaxRadius()),
                            new Pair<>(descenderX + 2 * this.style.getMaxRadius(), fwdY),
                            this.style.getLineWidth(), 180, 90, tag));
            exitX += 2 * this.style.getMaxRadius();
            int halfX = (exitX + indent) / 2;
            this.canvas.addElement(
                    new LineElement(new Pair<>(bypassX + this.style.getMaxRadius(), fwdY),
                            new Pair<>(halfX, fwdY),
                            "last", this.style.getLineWidth(), tag));
            this.canvas.addElement(
                    new LineElement(new Pair<>(halfX, fwdY), new Pair<>(exitX, fwdY),
                            null, this.style.getLineWidth(), tag));
            this.canvas.addElement(
                    new LineElement(new Pair<>(descenderX, exitY + this.style.getMaxRadius()),
                            new Pair<>(descenderX, fwdY - this.style.getMaxRadius()),
                            "last", this.style.getLineWidth(), tag));
            exitY = fwdY;
        }

        setUnitEndPoint(new UnitEndPoint(tag, new Pair<>(exitX, exitY)));
    }

    @Override
    public void visitStack(Stack stack) {
        setIndent(0);
        parseStack(stack);
    }

    @Override
    public void visitRightstack(Rightstack unit) {
        setIndent(-1);
        parseStack(unit);
    }

    @Override
    public void visitIndentstack(Indentstack unit) {
        int sep = this.style.getHSep() * unit.getIndent();
        setIndent(sep);
        parseStack(unit);
    }

    private void drawLeftTurnBack(String tag, int x, int yy0, int yy1, String flow) {
        int y0 = Math.min(yy0, yy1);
        int y1 = Math.max(yy0, yy1);

        if (y1 - y0 > 3 * this.style.getMaxRadius()) {
            int xr0 = x - this.style.getMaxRadius();
            int xr1 = x + this.style.getMaxRadius();
            this.canvas.addElement(
                    new ArcElement(new Pair<>(xr0, y0), new Pair<>(xr1, y0 + 2 * this.style.getMaxRadius()),
                            this.style.getLineWidth(), 90, 90, tag));
            int yr0 = y0 + this.style.getMaxRadius();
            int yr1 = y1 - this.style.getMaxRadius();
            if (Math.abs(yr1 - yr0) > 2 * this.style.getMaxRadius()) {
                int halfY = (yr0 + yr1) / 2;
                if ("down".equals(flow)) {
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr0, yr0), new Pair<>(xr0, halfY),
                                    "last", this.style.getLineWidth(), tag));
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr0, halfY), new Pair<>(xr0, yr1),
                                    null, this.style.getLineWidth(), tag));
                } else {
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr0, yr1), new Pair<>(xr0, halfY),
                                    "last", this.style.getLineWidth(), tag));
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr0, halfY), new Pair<>(xr0, yr0),
                                    null, this.style.getLineWidth(), tag));
                }
            } else {
                this.canvas.addElement(
                        new LineElement(new Pair<>(xr0, yr0), new Pair<>(xr0, yr1),
                                null, this.style.getLineWidth(), tag));
            }

            this.canvas.addElement(
                    new ArcElement(new Pair<>(xr0, y1 - 2 * this.style.getMaxRadius()), new Pair<>(xr1, y1),
                            this.style.getLineWidth(), 180, 90, tag));
        } else {
            int r = (y1 - y0) / 2;
            int x0 = x - r;
            int x1 = x + r;
            this.canvas.addElement(
                    new ArcElement(new Pair<>(x0, y0), new Pair<>(x1, y1),
                            this.style.getLineWidth(), 90, 180, tag));
        }
    }

    private void drawRightTurnBack(String tag, int x, int yy0, int yy1, String flow) {
        int y0 = Math.min(yy0, yy1);
        int y1 = Math.max(yy0, yy1);

        if (y1 - y0 > 3 * this.style.getMaxRadius()) {
            int xr0 = x - this.style.getMaxRadius();
            int xr1 = x + this.style.getMaxRadius();

            this.canvas.addElement(
                    new ArcElement(new Pair<>(xr0, y0), new Pair<>(xr1, y0 + 2 * this.style.getMaxRadius()),
                            this.style.getLineWidth(), 90, -90, tag));
            int yr0 = y0 + this.style.getMaxRadius();
            int yr1 = y1 - this.style.getMaxRadius();

            if (Math.abs(yr1 - yr0) > 2 * this.style.getMaxRadius()) {
                int halfY = (yr1 + yr0) / 2;
                if ("down".equals(flow)) {
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr1, yr0), new Pair<>(xr1, halfY),
                                    "last", this.style.getLineWidth(), tag));
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr1, halfY), new Pair<>(xr1, yr1),
                                    null, this.style.getLineWidth(), tag));
                } else {
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr1, yr1), new Pair<>(xr1, halfY),
                                    "last", this.style.getLineWidth(), tag));
                    this.canvas.addElement(
                            new LineElement(new Pair<>(xr1, halfY), new Pair<>(xr1, yr0),
                                    null, this.style.getLineWidth(), tag));
                }
            } else {
                this.canvas.addElement(
                        new LineElement(new Pair<>(xr1, yr0), new Pair<>(xr1, yr1),
                                null, this.style.getLineWidth(), tag));
            }

            this.canvas.addElement(
                    new ArcElement(new Pair<>(xr0, y1 - 2 * this.style.getMaxRadius()), new Pair<>(xr1, y1),
                            this.style.getLineWidth(), 0, -90, tag));
        } else {
            int r = (y1 - y0) / 2;
            int x0 = x - r;
            int x1 = x + r;
            this.canvas.addElement(
                    new ArcElement(new Pair<>(x0, y0), new Pair<>(x1, y1),
                            this.style.getLineWidth(), 90, -180, tag));
        }
    }

    public static Pair<Integer, Integer> getTextSize(String text, Font font) {

        Rectangle2D r = font.getStringBounds(text,
                new FontRenderContext(null, true, true));

        return new Pair<>((int) (r.getMaxX() - r.getMinX() + text.length() + 10),
                (int) (r.getMaxY() - r.getMinY() + 10));
    }

    /**
     * @brief class for return in parse functions
     * @details contain pair of tag end endpoint
     */
    public static class UnitEndPoint {
        UnitEndPoint(String tag, Pair<Integer, Integer> endpoint) {
            this.tag = tag;
            this.endpoint = endpoint;
        }

        public String tag;
        public Pair<Integer, Integer> endpoint;
    }

    public UnitEndPoint getUnitEndPoint() {
        return unitEndPoint;
    }

    public void setUnitEndPoint(UnitEndPoint unitEndPoint) {
        this.unitEndPoint = unitEndPoint;
    }

    public boolean getLtor() {
        return ltor;
    }

    public void setLtor(boolean ltor) {
        this.ltor = ltor;
    }

    public Integer getIndent() {
        return indent;
    }

    public void setIndent(Integer indent) {
        this.indent = indent;
    }

    private UnitEndPoint unitEndPoint;
    private boolean ltor;
    private Integer indent;
    private String title;
}
