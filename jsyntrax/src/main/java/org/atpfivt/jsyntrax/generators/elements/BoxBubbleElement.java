package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.util.StringUtils;
import org.atpfivt.jsyntrax.util.Pair;

import java.awt.Color;
import java.awt.Font;

public class BoxBubbleElement extends BubbleElementBase {
    public BoxBubbleElement(Pair<Integer, Integer> start, Pair<Integer, Integer> end, String href,
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

        sb.append("<rect x=\"").append(x0).append("\" y=\"").append(y0)
                .append("\" width=\"").append(x1 - x0).append("\" height=\"").append(y1 - y0)
                .append("\" ").append(attributes).append(" />\n");
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

        sb.append("<rect x=\"").append(x0).append("\" y=\"").append(y0)
                .append("\" width=\"").append(x1 - x0).append("\" height=\"").append(y1 - y0)
                .append("\" ").append(attributes).append(" />\n");

        // Add text
        addXMLText(sb, style);
    }
}
