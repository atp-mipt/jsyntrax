package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.util.StringUtils;
import org.atpfivt.jsyntrax.util.Pair;

public class LineElement extends Element {

    public LineElement(Pair<Integer, Integer> start, Pair<Integer, Integer> end,
                       String arrow, int width, String tag) {
        super(tag);
        super.setStart(start);
        super.setEnd(end);
        this.arrow = arrow;
        this.width = width;
    }

    @Override
    public void addShadow(StringBuilder sb, StyleConfig style) {

    }

    @Override
    public void toSVG(StringBuilder sb, StyleConfig style) {
        String attributes = "stroke=\"" + StringUtils.toHex(style.getLineColor())
                + "\" " + "stroke-width=\"" + this.width + "\"";

        if (arrow != null) {
            attributes += " marker-end=\"url(#arrow)\"";
            if ("first".equals(arrow)) {
                // swap
                Pair<Integer, Integer> tmp = super.getStart();
                super.setStart(super.getEnd());
                super.setEnd(tmp);
            }
            double len = Math.sqrt(
                    (super.getEnd().f - super.getStart().f) * (super.getEnd().f - super.getStart().f)
                            + (super.getEnd().s - super.getStart().s) * (super.getEnd().s - super.getStart().s)
            );
            len -= 4;
            double angle = Math.atan2(super.getEnd().s - super.getStart().s, super.getEnd().f - super.getStart().f);

            super.getEnd().f = (int) (super.getStart().f + len * Math.cos(angle));
            super.getEnd().s = (int) (super.getStart().s + len * Math.sin(angle));
        }
        sb.append("<line ")
                .append("x1=\"").append(super.getStart().f).append("\" ")
                .append("y1=\"").append(super.getStart().s).append("\" ")
                .append("x2=\"").append(super.getEnd().f).append("\" ")
                .append("y2=\"").append(super.getEnd().s).append("\" ")
                .append(attributes).append(" />\n");
    }

    @Override
    public void scale(double scale) {
        super.scale(scale);
        width *= scale;
    }

    private final String arrow;
    private int width;
}
