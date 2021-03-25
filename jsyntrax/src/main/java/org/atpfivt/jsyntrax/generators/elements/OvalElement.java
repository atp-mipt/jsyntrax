package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.util.StringUtils;
import org.atpfivt.jsyntrax.util.Pair;
import java.awt.Color;

/**
 * @details Need only for bullets
 */
public class OvalElement extends Element {
    public OvalElement(Pair<Integer, Integer> start, Pair<Integer, Integer> end, int width, Color fill, String tag) {
        super(tag);
        super.setStart(start);
        super.setEnd(end);
        this.setWidth(width);
        this.setFill(fill);
    }

    @Override
    public void addShadow(StringBuilder sb, StyleConfig style) {

    }

    @Override
    public void toSVG(StringBuilder sb, StyleConfig style) {
        int x0 = super.getStart().f;
        int y0 = super.getStart().s;
        int x1 = super.getEnd().f;
        int y1 = super.getEnd().s;

        String attributes = "stroke=\"" + StringUtils.toHex(style.getLineColor()) + "\" "
                + "stroke-width=\"" + this.getWidth() + "\" "
                + "fill=\"" + StringUtils.toHex(this.getFill()) + "\"";

        int xc = (x0 + x1) / 2;
        int yc = (y0 + y1) / 2;
        int rad = (x1 - x0) / 2;

        sb.append("<circle cx=\"").append(xc).append("\" cy=\"").append(yc)
                .append("\" r=\"").append(rad).append("\" ")
                .append(attributes).append("/>\n");
    }

    @Override
    public void scale(double scale) {
        super.scale(scale);
        setWidth((int) (getWidth() * scale));
    }

    private int width;
    private Color fill;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Color getFill() {
        return fill;
    }

    public void setFill(Color fill) {
        this.fill = fill;
    }
}
