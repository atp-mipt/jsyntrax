package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.util.StringUtils;
import org.atpfivt.jsyntrax.util.Pair;

public class ArcElement extends Element {
    public ArcElement(Pair<Integer, Integer> start, Pair<Integer, Integer> end,
                      int width, int startAngle, int extentAngle, String tag) {
        super(tag);
        super.setStart(start);
        super.setEnd(end);
        this.width = width;
        this.startAngle = startAngle;
        this.extentAngle = extentAngle;
    }

    @Override
    public void addShadow(StringBuilder sb, StyleConfig style) { }

    @Override
    public void toSVG(StringBuilder sb, StyleConfig style) {
        int x0 = super.getStart().f;
        int y0 = super.getStart().s;
        int x1 = super.getEnd().f;
        int y1 = super.getEnd().s;

        int xc = (x0 + x1) / 2;
        int yc = (y0 + y1) / 2;
        int rad = (x1 - x0) / 2;

        this.startAngle %= 360;
        int stop = (this.startAngle + this.extentAngle) % 360;

        if (this.extentAngle < 0) {
            int tmp = this.startAngle;
            this.startAngle = stop;
            stop = tmp;
        }

        double startRad = Math.toRadians(this.startAngle);
        double stopRad = Math.toRadians(stop);

        String attributes = "stroke=\"" + StringUtils.toHex(style.getLineColor()) + "\" "
                + "stroke-width=\"" + this.width + "\" fill=\"none\"";

        int xs = (int) (xc + rad * Math.cos(startRad));
        int ys = (int) (yc - rad * Math.sin(startRad));
        int xe = (int) (xc + rad * Math.cos(stopRad));
        int ye = (int) (yc - rad * Math.sin(stopRad));

        sb.append("<path d=\"M").append(xs).append(",").append(ys)
                .append(" A").append(rad).append(",").append(rad)
                .append(" 0 0,0 ").append(xe).append(",").append(ye)
                .append("\" ").append(attributes).append("/>\n");
    }

    @Override
    public void scale(double scale) {
        super.scale(scale);
        width *= scale;
    }

    private int width;
    private int startAngle;
    private final int extentAngle;
}
