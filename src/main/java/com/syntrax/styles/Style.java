package com.syntrax.styles;

import java.awt.*;
import java.util.ArrayList;

public class Style {
    public Style() {
        // default styles
        this.line_width = 2;
        this.line_color = new Color(0, 0, 0);
        this.outline_width = 2;
        this.padding = 5;
        this.max_radius = 9;
        this.h_sep = 17;
        this.v_sep = 9;
        this.arrows = true;
        this.title_pos = new String("tl");
        this.bullet_fill = new Color(255, 255, 255);
        this.text_color = new Color(0, 0, 0);
        this.shadow = true;
        this.shadow_fill = new Color(0, 0, 0, 127);
        this.title_font = new Font("Sans",Font.BOLD, 22);
        this.transparent = false;

        // TODO: add custom Node styles
        nodeStyles = new ArrayList<>();
        nodeStyles.add(new NodeBubbleStyle());
        nodeStyles.add(new NodeBoxStyle());
        nodeStyles.add(new NodeTokenStyle());
    }

    public void addStyle(NodeStyle ns) {
        nodeStyles.add(ns);
    }

    public NodeStyle getNodeStyle(String txt) {
        for (NodeStyle ns : nodeStyles) {
            if (ns.match(txt)) {
                return ns;
            }
        }
        return nodeStyles.get(0);
    }

    public int line_width;
    public Color line_color;
    public int outline_width;
    public int padding;
    public int max_radius;
    public int h_sep;
    public int v_sep;
    public boolean arrows;
    public String title_pos;
    public Color bullet_fill;
    public Color text_color;
    public boolean shadow;
    public Color shadow_fill;
    public Font title_font;
    public boolean transparent;

    public final ArrayList<NodeStyle> nodeStyles;
}
