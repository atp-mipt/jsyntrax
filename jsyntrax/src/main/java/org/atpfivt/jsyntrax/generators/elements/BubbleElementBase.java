package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.util.Algorithm;
import org.atpfivt.jsyntrax.util.Pair;

import java.awt.*;
import java.net.URL;

public class BubbleElementBase extends Element {

    public BubbleElementBase(Pair<Integer, Integer> start, Pair<Integer, Integer> end, URL href,
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

    void addXMLText(StringBuilder sb) {
        int x0 = super.start.f;
        int y0 = super.start.s;
        int x1 = super.end.f;
        int y1 = super.end.s;

        // TODO: bad centering
        int th = Math.abs(textPos.s);
        int x = (x0 + x1) / 2;
        int y = (y0 + y1) / 2 + th / 2;

        String txt = Algorithm.escapeXML(text);
        if (this.href == null) {
            sb.append("<text class=\"").append(fontName).append("\" x=\"").append(x)
                    .append("\" y=\"").append(y).append("\">").append(txt).append("</text>\n");
        }
        else {
            String link = Algorithm.escapeXML(this.href.toExternalForm());
            sb.append("<a xlink:href=\"").append(link).append("\" target=\"_parent\">")
                    .append("<text class=\"").append(fontName).append(" link\" x=\"").append(x)
                    .append("\" y=\"").append(y).append("\">").append(txt).append("</text></a>\n");
        }
    }

    URL href;
    String text;
    Pair<Integer, Integer> textPos;
    Font font;
    String fontName;
    Color textColor;
    int width;
    Color fill;
}
