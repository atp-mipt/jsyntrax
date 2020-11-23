package com.syntrax.generators.elements;

import com.syntrax.styles.Style;
import com.syntrax.util.Algorithm;
import com.syntrax.util.Pair;

import java.awt.*;

public class BoxBubbleElement extends BubbleElementBase {
    public BoxBubbleElement(Pair<Integer, Integer> start, Pair<Integer, Integer> end, String text,
                            Pair<Integer, Integer> textPos, Font font, String fontName, Color textColor,
                            int width, Color fill, String tag) {
        super(start, end, text, textPos, font, fontName, textColor, width, fill, tag);
    }

    public void addShadow(StringBuilder sb, Style style) {
        int x0 = super.start.f + super.width + 1;
        int y0 = super.start.s + super.width + 1;
        int x1 = super.end.f + super.width + 1;
        int y1 = super.end.s + super.width + 1;

        String attributes = "fill=\"" + Algorithm.toHex(style.shadow_fill) + "\" " +
                "fill-opacity=\"" + Algorithm.fillOpacity(style.shadow_fill) + "\"";

        sb.append("<rect x=\"").append(x0).append("\" y=\"").append(y0)
                .append("\" width=\"").append(x1 - x0).append("\" height=\"").append(y1 - y0)
                .append("\" ").append(attributes).append(" />\n");
    }

    public void toSVG(StringBuilder sb, Style style) {
        int x0 = super.start.f;
        int y0 = super.start.s;
        int x1 = super.end.f;
        int y1 = super.end.s;

        String attributes = "stroke=\"" + Algorithm.toHex(style.line_color) + "\" " +
                "stroke-width=\"" + this.width + "\" " +
                "fill=\"" + Algorithm.toHex(this.fill) + "\"";

        sb.append("<rect x=\"").append(x0).append("\" y=\"").append(y0)
                .append("\" width=\"").append(x1 - x0).append("\" height=\"").append(y1 - y0)
                .append("\" ").append(attributes).append(" />\n");

        // Add text
        int th = Math.abs(super.textPos.s);
        int x = (x0 + x1) / 2;
        int y =  (y0 + y1) / 2 + th / 2;

        String txt = Algorithm.escapeXML(super.text);

        // TODO: add href

        sb.append("<text class=\"").append(super.fontName).append("\" x=\"").append(x)
                .append("\" y=\"").append(y).append("\">").append(txt).append("</text>\n");
    }
}
