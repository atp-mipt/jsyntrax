package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.util.StringUtils;
import org.atpfivt.jsyntrax.util.Pair;

import java.awt.Color;
import java.awt.Font;

public class HexBubbleElement extends BubbleElementBase {
    public HexBubbleElement(Pair<Integer, Integer> start, Pair<Integer, Integer> end, String href,
                            String text, Pair<Integer, Integer> textPos, Font font, String fontName,
                            Color textColor, int width, Color fill, String tag) {
        super(start, end, href, text, textPos, font, fontName, textColor, width, fill, tag);
    }

    @Override
    public void addShadow(StringBuilder sb, StyleConfig style) {
        int x0 = super.getStart().f + super.getWidth() + 1;
        int y0 = super.getStart().s + super.getWidth() + 1;
        int x1 = super.getEnd().f + super.getWidth() + 1;
        int y1 = super.getEnd().s + super.getWidth() + 1;

        String attributes = "fill=\"" + StringUtils.toHex(style.getShadowFill()) + "\" "
                + "fill-opacity=\"" + StringUtils.fillOpacity(style.getShadowFill()) + "\"";

        int rad = (y1 - y0) / 2;
        int lft = x0 + rad;
        int rgt = x1 - rad;
        int rpad = rad / 2;

        int xc = (x0 + x1) / 2;
        int yc = (y0 + y1) / 2;

        if (Math.abs(rgt - lft) <= 1) {
            lft = xc;
            rgt = yc;
        }

        sb.append("<path d=\"M").append(lft - rpad).append(",").append(y1)
                .append(" H").append(rgt + rpad).append(" L").append(rgt + rad)
                .append(",").append(yc).append(" L").append(rgt + rpad)
                .append(",").append(y0).append(" H").append(lft - rpad)
                .append(" L").append(lft - rad).append(",").append(yc)
                .append(" z\" ").append(attributes).append(" />\n");
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
        int rpad = rad / 2;

        int xc = (x0 + x1) / 2;
        int yc = (y0 + y1) / 2;

        if (Math.abs(rgt - lft) <= 1) {
            lft = xc;
            rgt = yc;
        }

        sb.append("<path d=\"M").append(lft - rpad).append(",").append(y1)
                .append(" H").append(rgt + rpad).append(" L").append(rgt + rad)
                .append(",").append(yc).append(" L").append(rgt + rpad)
                .append(",").append(y0).append(" H").append(lft - rpad)
                .append(" L").append(lft - rad).append(",").append(yc)
                .append(" z\" ").append(attributes).append(" />\n");

        // Add text
        addXMLText(sb, style);
    }
}
