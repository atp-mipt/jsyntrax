package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.util.StringUtils;
import org.atpfivt.jsyntrax.util.Pair;
import java.awt.Color;
import java.awt.Font;

public abstract class BubbleElementBase extends Element {

    public BubbleElementBase(Pair<Integer, Integer> start, Pair<Integer, Integer> end, String href,
                             String text, Pair<Integer, Integer> textPos, Font font, String fontName,
                             Color textColor, int width, Color fill, String tag) {
        super(tag);
        super.setStart(start);
        super.setEnd(end);
        this.setHref(href);
        this.setText(text);
        this.setTextPos(textPos);
        this.setFont(font);
        this.setFontName(fontName);
        this.setTextColor(textColor);
        this.setWidth(width);
        this.setFill(fill);
    }

    int getX(StyleConfig style) {
        int x0 = getStart().f;
        int x1 = getEnd().f;
        return  (x0 + x1) / 2;
    }

    void addXMLText(StringBuilder sb, StyleConfig style) {
        int y0 = getStart().s;
        int y1 = getEnd().s;

        int x = getX(style);

        int y = (y0 + y1) / 2 + (int) (Math.abs(getTextPos().s) * 0.25 + style.getScale() * 2);

        String txt = StringUtils.escapeXML(getText());
        if (this.getHref() == null) {
            sb.append("<text class=\"").append(getFontName()).append("\" x=\"").append(x)
                    .append("\" y=\"").append(y).append("\">").append(txt).append("</text>\n");
        } else {
            String link = StringUtils.escapeXML(this.getHref());
            sb.append("<a xlink:href=\"").append(link).append("\" target=\"_parent\">")
                    .append("<text class=\"").append(getFontName()).append(" link\" x=\"").append(x)
                    .append("\" y=\"").append(y).append("\">").append(txt).append("</text></a>\n");
        }
    }

    @Override
    public void scale(double scale) {
        super.scale(scale);
        setWidth((int) (getWidth() * scale));
        getTextPos().f = (int) (getTextPos().f * scale);
        getTextPos().s = (int) (getTextPos().s * scale);
        if (getFont() != null) {
            setFont(getFont().deriveFont((float) (getFont().getSize() * scale)));
        }
    }
    private String href;
    private String text;
    private Pair<Integer, Integer> textPos;
    private Font font;
    private String fontName;
    private Color textColor;
    private int width;
    private Color fill;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Pair<Integer, Integer> getTextPos() {
        return textPos;
    }

    public void setTextPos(Pair<Integer, Integer> textPos) {
        this.textPos = textPos;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

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
