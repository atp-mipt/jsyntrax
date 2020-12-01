package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.styles.Style;
import org.atpfivt.jsyntrax.util.Algorithm;
import org.atpfivt.jsyntrax.util.Pair;

import java.awt.Color;
import java.awt.Font;

public class BubbleElement extends BubbleElementBase {
    public BubbleElement(Pair<Integer, Integer> start, Pair<Integer, Integer> end, String href,
                         String text, Pair<Integer, Integer> textPos, Font font, String fontName,
                         Color textColor, int width, Color fill, String tag) {
        super(start, end, href, text, textPos, font, fontName, textColor, width, fill, tag);
    }

    @Override
    public void addShadow(StringBuilder sb, Style style) {
        int x0 = super.start.f + this.width + 1;
        int y0 = super.start.s + this.width + 1;
        int x1 = super.end.f + this.width + 1;
        int y1 = super.end.s + this.width + 1;

        String attributes = "fill=\"" + Algorithm.toHex(style.shadow_fill) + "\" " +
                "fill-opacity=\"" + Algorithm.fillOpacity(style.shadow_fill) + "\"";

        int rad = (y1 - y0) / 2;
        int lft = x0 + rad;
        int rgt = x1 - rad;

        int xc = (x0 + x1) / 2;
        int yc = (y0 + y1) / 2;

        if (Math.abs(rgt - lft) <= 1) {
            // Circlular bubble
            sb.append("<circle ")
                    .append("cx=\"").append(xc).append("\" ")
                    .append("cy=\"").append(yc).append("\" ")
                    .append("r=\"").append(rad).append("\" ")
                    .append(attributes).append(" />\n");
        }
        else {
            // Rounded bubble
            sb.append("<path d=\"M").append(lft).append(",").append(y1)
                    .append(" A").append(rad).append(",").append(rad)
                    .append(" 0 0,1 ").append(lft).append(",").append(y0)
                    .append(" H").append(rgt).append(" A").append(rad).append(",")
                    .append(rad).append(" 0 0,1 ").append(rgt).append(",").append(y1).append(" z\" ")
                    .append(attributes).append(" />\n");
        }
    }

    @Override
    public void toSVG(StringBuilder sb, Style style) {
        int x0 = super.start.f;
        int y0 = super.start.s;
        int x1 = super.end.f;
        int y1 = super.end.s;

        String attributes = "stroke=\"" + Algorithm.toHex(style.line_color) + "\" " +
                "stroke-width=\"" + this.width + "\" " +
                "fill=\"" + Algorithm.toHex(this.fill) + "\" " +
                "fill-opacity=\"" + Algorithm.fillOpacity(this.fill) + "\"";

        int rad = (y1 - y0) / 2;
        int lft = x0 + rad;
        int rgt = x1 - rad;

        int xc = (x0 + x1) / 2;
        int yc = (y0 + y1) / 2;

        if (Math.abs(rgt - lft) <= 1) {
            // Circlular bubble
            sb.append("<circle ")
                .append("cx=\"").append(xc).append("\" ")
                .append("cy=\"").append(yc).append("\" ")
                .append("r=\"").append(rad).append("\" ")
                .append(attributes).append(" />\n");
        }
        else {
            // Rounded bubble
            sb.append("<path d=\"M").append(lft).append(",").append(y1)
                    .append(" A").append(rad).append(",").append(rad)
                    .append(" 0 0,1 ").append(lft).append(",").append(y0)
                    .append(" H").append(rgt).append(" A").append(rad).append(",")
                    .append(rad).append(" 0 0,1 ").append(rgt).append(",").append(y1).append(" z\" ")
                    .append(attributes).append(" />\n");
        }

        // Add text
        addXMLText(sb, style);
    }
}
