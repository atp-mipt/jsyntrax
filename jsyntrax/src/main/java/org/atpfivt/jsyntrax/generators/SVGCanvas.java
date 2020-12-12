package org.atpfivt.jsyntrax.generators;

import org.atpfivt.jsyntrax.generators.elements.Element;
import org.atpfivt.jsyntrax.styles.NodeStyle;
import org.atpfivt.jsyntrax.styles.Style;
import org.atpfivt.jsyntrax.util.Algorithm;
import org.atpfivt.jsyntrax.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SVGCanvas {

    public SVGCanvas(Style style, Map<String, String> urlMap) {
        this.urlMap = urlMap;
        this.style = style;
        this.tagcnt = new HashMap<>();
        this.elements = new ArrayList<>();
    }

    // default prefix = "x", default suffix = ""
    public String new_tag(String prefix, String suffix) {
        String f = prefix + "___" + suffix;
        Integer value = tagcnt.getOrDefault(f, 0);
        tagcnt.put(f, value + 1);
        return prefix + value.toString() + suffix;
    }

    public void addElement(Element e) {
        this.elements.add(e);
    }

    public void addTagByTag(String addTag, String tag) {
        for (Element e : elements) {
            if (e.isTagged(tag)) {
                e.addTag(addTag);
            }
        }
    }

    public void dropTag(final String tag) {
        for (Element e : elements) {
            if (e.isTagged(tag)) {
                e.delTag(tag);
            }
        }
    }

    public void moveByTag(String tag, int dx, int dy) {
        for (Element e : elements) {
            if (e.isTagged(tag)) {
                e.start.f += dx;
                e.start.s += dy;
                e.end.f += dx;
                e.end.s += dy;
            }
        }
    }

    public void scaleByTag(String tag, double scale) {
        for (Element e : elements) {
            if (e.isTagged(tag)) {
                e.scale(scale);
            }
        }
    }

    public Optional<String> getCanvasTag() {
        return elements.stream().findAny().flatMap(Element::getAnyTag);
    }

    public Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> getBoundingBoxByTag(String tag) {
        Pair<Integer, Integer> start = null;
        Pair<Integer, Integer> end = null;
        for (Element e: this.elements)
        {
            if (e.isTagged(tag)) {
                if (start == null) {
                    start = new Pair<>(e.start);
                    end = new Pair<>(e.end);
                }
                start.f = Math.min(start.f, e.start.f);
                start.f = Math.min(start.f, e.end.f);
                start.s = Math.min(start.s, e.start.s);
                start.s = Math.min(start.s, e.end.s);

                end.f = Math.max(end.f, e.start.f);
                end.f = Math.max(end.f, e.end.f);
                end.s = Math.max(end.s, e.start.s);
                end.s = Math.max(end.s, e.end.s);
            }
        }
        return new Pair<>(start, end);
    }

    public String generateSVG() {
        StringBuilder sb = new StringBuilder();
        double scale = style.scale;

        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> res = getBoundingBoxByTag("all");

        // move to picture to (0, 0)
        moveByTag("all", -res.f.f, -res.f.s);
        scaleByTag("all", scale);
        moveByTag("all", style.padding, style.padding);

        res = getBoundingBoxByTag("all");
        Pair<Integer, Integer> end = res.s;

        int W = end.f + style.padding;
        int H = end.s + style.padding;

        // collect fonts
        HashMap<String, Pair<Font, Color>> fonts = new HashMap<>();
        fonts.put("title_font", new Pair<>(this.style.title_font, this.style.text_color));
        for (NodeStyle ns : this.style.nodeStyles) {
            fonts.put(ns.name + "_font", new Pair<>(ns.font, ns.text_color));
        }

        // header
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n")
                .append("<!-- Created by Jyntrax https://github.com/atp-mipt/jsyntrax -->\n");
        sb.append("<svg xmlns=\"http://www.w3.org/2000/svg\"\n");
        sb.append("xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n");
        sb.append("xml:space=\"preserve\"\n");
        sb.append("width=\"").append(W).append("\" ")
                .append("height=\"").append(H).append("\" ")
                .append("version=\"1.1\">\n");
        // styles
        sb.append("<style type=\"text/css\">\n");
        sb.append("<![CDATA[\n");
        // fonts
        for (Map.Entry<String, Pair<Font, Color>> fontPair : fonts.entrySet()) {
            String fontName = fontPair.getKey();
            String fontFamily = fontPair.getValue().f.getName();
            String fontSize = Integer.toString((int)(fontPair.getValue().f.getSize() * scale));
            String fontWeight = "normal";
            if ((fontPair.getValue().f.getStyle() & Font.BOLD) == Font.BOLD) {
                fontWeight = "bold";
            }
            String fontStyle = "normal";
            if ((fontPair.getValue().f.getStyle() & Font.ITALIC) == Font.ITALIC) {
                fontStyle = "italic";
            }

            String hex = Algorithm.toHex(fontPair.getValue().s);

            sb.append(".").append(fontName).append(" ");
            sb.append("{fill:").append(hex).append("; text-anchor:middle;\n");
            sb.append("font-family:").append(fontFamily).append("; ");
            sb.append("font-size:").append(fontSize).append("pt; ");
            sb.append("font-weight:").append(fontWeight).append("; ");
            sb.append("font-style:").append(fontStyle).append("; ");
            sb.append("}\n");
        }
        // other fonts
        sb.append(".label {fill: #000; text-anchor:middle; font-size:16pt; font-weight:bold; font-family:Sans;}\n");
        sb.append(".link {fill: #0D47A1;}\n");
        sb.append(".link:hover {fill: #0D47A1; text-decoration:underline;}\n");
        sb.append(".link:visited {fill: #4A148C;}\n");
        // close
        sb.append("]]>\n</style>\n");
        // defs
        sb.append("<defs>\n");
        sb.append("<marker id=\"arrow\" markerWidth=\"5\" markerHeight=\"4\" ")
                .append("refX=\"2.5\" refY=\"2\" orient=\"auto\" markerUnits=\"strokeWidth\">\n");
        String hex = Algorithm.toHex(this.style.line_color);
        sb.append("<path d=\"M0,0 L0.5,2 L0,4 L4.5,2 z\" fill=\"").append(hex).append("\" />\n");
        sb.append("</marker>\n</defs>\n");

        // elements
        if (!style.transparent) {
            sb.append("<rect width=\"100%\" height=\"100%\" fill=\"white\"/>\n");
        }
        for (Element e : this.elements) {

            if (style.shadow) {
                e.addShadow(sb, this.style);
            }
            e.toSVG(sb, this.style);
        }
        // end
        sb.append("</svg>\n");
        return sb.toString();
    }

    public final Map<String, String> urlMap;

    private final Style style;

    private final HashMap<String, Integer> tagcnt;

    private final ArrayList<Element> elements;
}
