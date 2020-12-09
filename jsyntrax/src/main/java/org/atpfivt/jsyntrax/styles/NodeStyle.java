package org.atpfivt.jsyntrax.styles;

import java.awt.*;
import java.util.regex.Pattern;

public class NodeStyle {
    public boolean match(String txt) {
        if (pattern == null) {
            return true;
        }
        return pattern.matcher(txt).find();
    }

    public String modify(String txt) { return txt; }

    public String name = "unknown";
    public String shape = "bubble";
    public Font font = new Font("Sans",Font.BOLD, 14);
    public Color text_color = new Color(0,0,0);
    public Color fill = new Color(144,164,174);
    public Pattern pattern = null;
}
