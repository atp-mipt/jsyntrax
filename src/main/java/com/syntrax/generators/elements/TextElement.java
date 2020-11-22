package com.syntrax.generators.elements;

import com.syntrax.styles.Style;
import com.syntrax.util.Algorithm;
import com.syntrax.util.Pair;
import sun.font.FontDesignMetrics;

import java.awt.*;

public class TextElement extends Element {

    // anchor is a align (left or center)
    public TextElement(Pair<Integer, Integer> start, char anchor, String text,
                       Font font, String fontName, Color textColor, String tag) {
        super(tag);
        this.text = text;
        this.font = font;
        this.fontName = fontName;
        this.textColor = textColor;

        Pair<Integer, Integer> hw = getTextSize(text, font);

        int h = hw.f;
        int w = hw.s;

        if (anchor == 'c') {
            start.f -= w / 2;
            start.s -= h / 2;
        }

        super.start = start;
        super.end = new Pair<>(start.f + w, start.s + h);
    }

    public static Pair<Integer, Integer> getTextSize(String text, Font font) {
        FontMetrics metrics = FontDesignMetrics.getMetrics(font);

        // TODO: dirty...
        return new Pair<>(metrics.stringWidth(text) + 10, metrics.getHeight() + 10);
    }

    public void toSVG(StringBuilder sb, Style style) {
        // replace text to xml format
        String txt = Algorithm.escapeXML(this.text);

        // TODO: may be need centering

        sb.append("<text ")
            .append("class=\"").append(fontName).append("\" ")
            .append("x=\"").append(super.start.f).append("\" ")
            .append("y=\"").append(super.start.s).append("\">");
        sb.append(txt);
        sb.append(">\n");
    }

    String text;
    Font font;
    String fontName;
    Color textColor;
}
