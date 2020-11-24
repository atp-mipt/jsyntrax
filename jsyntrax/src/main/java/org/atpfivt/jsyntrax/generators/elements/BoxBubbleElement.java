package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.styles.Style;
import org.atpfivt.jsyntrax.util.Algorithm;
import org.atpfivt.jsyntrax.util.Pair;

import java.awt.*;
import java.net.URL;

public class BoxBubbleElement extends BubbleElementBase {
    public BoxBubbleElement(Pair<Integer, Integer> start, Pair<Integer, Integer> end, URL href,
                            String text, Pair<Integer, Integer> textPos, Font font, String fontName,
                            Color textColor, int width, Color fill, String tag) {
        super(start, end, href, text, textPos, font, fontName, textColor, width, fill, tag);
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
                "fill=\"" + Algorithm.toHex(this.fill) + "\" " +
                "fill-opacity=\"" + Algorithm.fillOpacity(this.fill) + "\"";

        sb.append("<rect x=\"").append(x0).append("\" y=\"").append(y0)
                .append("\" width=\"").append(x1 - x0).append("\" height=\"").append(y1 - y0)
                .append("\" ").append(attributes).append(" />\n");

        // Add text
        addXMLText(sb);
    }
}
