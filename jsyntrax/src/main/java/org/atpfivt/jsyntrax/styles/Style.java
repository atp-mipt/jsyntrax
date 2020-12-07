package org.atpfivt.jsyntrax.styles;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class Style {
    public Style(double scale, boolean transparent) {
        // default styles
        this.line_width = 2;
        this.line_color = new Color(0, 0, 0);
        this.outline_width = 2;
        this.padding = 5;
        this.max_radius = 9;
        this.h_sep = 17;
        this.v_sep = 9;
        this.arrows = true;
        this.title_pos = "tl";
        this.bullet_fill = new Color(255, 255, 255);
        this.text_color = new Color(0, 0, 0);
        this.shadow = true;
        this.shadow_fill = new Color(0, 0, 0, 127);
        this.title_font = new Font("Sans",Font.BOLD, 22);
        this.scale = scale;
        this.transparent = transparent;

        nodeStyles = new ArrayList<>(List.of(
            new NodeBubbleStyle(),
            new NodeBoxStyle(),
            new NodeTokenStyle(),
            new NodeHexStyle()));
    }

    public void addNodeStyle(NodeStyle ns) {
        nodeStyles.add(ns);
    }

    // for making custom node styles
    public void clearNodeStyles() {
        nodeStyles.clear();
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
    public double scale;
    public boolean transparent;

    public final List<NodeStyle> nodeStyles;
}
