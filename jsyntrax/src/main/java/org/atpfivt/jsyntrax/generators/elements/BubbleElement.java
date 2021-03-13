package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.util.StringUtils;
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
    public void addShadow(StringBuilder sb, StyleConfig style) {
        int x0 = super.getStart().f + this.getWidth() + 1;
        int y0 = super.getStart().s + this.getWidth() + 1;
        int x1 = super.getEnd().f + this.getWidth() + 1;
        int y1 = super.getEnd().s + this.getWidth() + 1;

        String attributes = "fill=\"" + StringUtils.toHex(style.getShadowFill()) + "\" "
                + "fill-opacity=\"" + StringUtils.fillOpacity(style.getShadowFill()) + "\"";

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
        } else {
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
    public void toSVG(StringBuilder sb, StyleConfig style) {
        int x0 = super.getStart().f;
        int y0 = super.getStart().s;
        int x1 = super.getEnd().f;
        int y1 = super.getEnd().s;

        String attributes = "stroke=\"" + StringUtils.toHex(style.getLineColor()) + "\" "
                + "stroke-width=\"" + this.getWidth() + "\" "
                + "fill=\"" + StringUtils.toHex(this.getFill()) + "\" "
                + "fill-opacity=\"" + StringUtils.fillOpacity(this.getFill()) + "\"";

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
        } else {
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
