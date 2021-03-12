package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.util.StringUtils;
import org.atpfivt.jsyntrax.util.Pair;

import java.awt.*;

public abstract class BubbleElementBase extends Element {

    public BubbleElementBase(Pair<Integer, Integer> start, Pair<Integer, Integer> end, String href,
                             String text, Pair<Integer, Integer> textPos, Font font, String fontName,
                             Color textColor, int width, Color fill, String tag) {
        super(tag);
        super.start = start;
        super.end = end;
        this.href = href;
        this.text = text;
        this.textPos = textPos;
        this.font = font;
        this.fontName = fontName;
        this.textColor = textColor;
        this.width = width;
        this.fill = fill;
    }

    void addXMLText(StringBuilder sb, StyleConfig style) {
        int x0 = super.getStart().f;
        int y0 = super.getStart().s;
        int x1 = super.end.f;
        int y1 = super.end.s;

        int x = (x0 + x1) / 2;
        int y = (y0 + y1) / 2 + (int)(Math.abs(textPos.s) * 0.25 + style.getScale() * 2);

        String txt = StringUtils.escapeXML(text);
        if (this.href == null) {
            sb.append("<text class=\"").append(fontName).append("\" x=\"").append(x)
                    .append("\" y=\"").append(y).append("\">").append(txt).append("</text>\n");
        }
        else {
            String link = StringUtils.escapeXML(this.href);
            sb.append("<a xlink:href=\"").append(link).append("\" target=\"_parent\">")
                    .append("<text class=\"").append(fontName).append(" link\" x=\"").append(x)
                    .append("\" y=\"").append(y).append("\">").append(txt).append("</text></a>\n");
        }
    }

    @Override
    public void scale(double scale) {
        super.scale(scale);
        width *= scale;
        textPos.f = (int)(textPos.f * scale);
        textPos.s = (int)(textPos.s * scale);
        if (font != null) {
            font = font.deriveFont((float) (font.getSize() * scale));
        }
    }

    String href;
    String text;
    Pair<Integer, Integer> textPos;
    Font font;
    String fontName;
    Color textColor;
    int width;
    Color fill;
}
