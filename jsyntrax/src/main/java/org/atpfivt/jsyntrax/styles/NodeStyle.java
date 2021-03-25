package org.atpfivt.jsyntrax.styles;

import java.awt.Color;
import java.awt.Font;
import java.util.regex.Pattern;

public class NodeStyle {
    public final boolean match(String txt) {
        if (getPattern() == null) {
            return true;
        }
        return getPattern().matcher(txt).matches();
    }

    // gets plain text from regex groups, defined in pattern by user
    // returns text "as is" if no groups found or pattern is not defined
    public final String unwrapTextContent(String txt) {
        if (getPattern() == null) {
            return txt;
        }
        var matcher = getPattern().matcher(txt);
        StringBuilder unwrappedTxt = new StringBuilder();

        if (matcher.groupCount() == 0 || !matcher.find()) {
            return txt;
        }
        for (int i = 1; i <= matcher.groupCount(); i++) {
            unwrappedTxt.append(matcher.group(i));
        }
        return unwrappedTxt.toString();
    }

    private String name = "unknown";
    private String shape = "bubble";
    private Font font = new Font("Sans", Font.BOLD,  14);
    private Color textColor = new Color(0, 0, 0);
    private Color fill = new Color(144, 164, 174);
    private Pattern pattern = null;

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getShape() {
        return shape;
    }

    public final void setShape(String shape) {
        this.shape = shape;
    }

    public final Font getFont() {
        return font;
    }

    public final void setFont(Font font) {
        this.font = font;
    }

    public final Color getTextColor() {
        return textColor;
    }

    public final void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public final Color getFill() {
        return fill;
    }

    public final void setFill(Color fill) {
        this.fill = fill;
    }

    public final Pattern getPattern() {
        return pattern;
    }

    public final void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
