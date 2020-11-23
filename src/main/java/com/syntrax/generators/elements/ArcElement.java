package com.syntrax.generators.elements;

import com.syntrax.styles.Style;
import com.syntrax.util.Algorithm;
import com.syntrax.util.Pair;

public class ArcElement extends Element {
    public ArcElement(Pair<Integer, Integer> start, Pair<Integer, Integer> end,
                      int width, int startAngle, int extentAngle, String tag) {
        super(tag);
        super.start = start;
        super.end = end;
        this.width = width;
        this.startAngle = startAngle;
        this.extentAngle = extentAngle;
    }

    public void toSVG(StringBuilder sb, Style style) {
        int x0 = super.start.f;
        int y0 = super.start.s;
        int x1 = super.end.f;
        int y1 = super.end.s;

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

        String attributes = "stroke=\"" + Algorithm.toHex(style.line_color) + "\" " +
                "stroke-width=\"" + this.width + "\" fill=\"none\"";

        int xs = (int) (xc + rad * Math.cos(startRad));
        int ys = (int) (yc - rad * Math.sin(startRad));
        int xe = (int) (xc + rad * Math.cos(stopRad));
        int ye = (int) (yc - rad * Math.sin(stopRad));

        sb.append("<path d=\"M").append(xs).append(",").append(ys)
                .append(" A").append(rad).append(",").append(rad)
                .append(" 0 0,0 ").append(xe).append(",").append(ye)
                .append("\" ").append(attributes).append("/>\n");
    }

    int width;
    int startAngle;
    int extentAngle;
}
