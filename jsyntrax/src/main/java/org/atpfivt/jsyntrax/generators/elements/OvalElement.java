package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.styles.Style;
import org.atpfivt.jsyntrax.util.Algorithm;
import org.atpfivt.jsyntrax.util.Pair;

import java.awt.*;

/**
 * @details Need only for bullets
 */
public class OvalElement extends Element {
    public OvalElement(Pair<Integer, Integer> start, Pair<Integer, Integer> end, int width, Color fill, String tag) {
        super(tag);
        super.start = start;
        super.end = end;
        this.width = width;
        this.fill = fill;
    }

    @Override
    public void toSVG(StringBuilder sb, Style style) {
        int x0 = super.start.f;
        int y0 = super.start.s;
        int x1 = super.end.f;
        int y1 = super.end.s;

        String attributes = "stroke=\"" + Algorithm.toHex(style.line_color) + "\" " +
                "stroke-width=\"" + this.width + "\" " +
                "fill=\"" + Algorithm.toHex(this.fill) + "\"";

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
        width *= scale;
    }

    int width;
    Color fill;
}
