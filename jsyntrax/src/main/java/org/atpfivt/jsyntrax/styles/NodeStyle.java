package org.atpfivt.jsyntrax.styles;

import java.awt.*;

public class NodeStyle {
    public boolean match(String txt) { return false; }

    public String modify(String txt) { return ""; }

    public String name = "unknown";
    public String shape = "bubble";
    public Font font = new Font("Sans",Font.BOLD, 14);
    public Color text_color = new Color(0,0,0);
    public Color fill = new Color(144,164,174);
}
