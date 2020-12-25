package org.atpfivt.jsyntrax.styles;

import java.awt.Color;
import java.awt.Font;
import java.nio.file.Path;
import java.util.List;

public class Style {
    public Style(double scale, boolean transparent) {
        updateByConfig(new StyleConfig());
        this.scale = scale;
        this.transparent = transparent;
    }

    public NodeStyle getNodeStyle(String txt) {
        for (NodeStyle ns : nodeStyles) {
            if (ns.match(txt)) {
                return ns;
            }
        }
        return defNodeStyle;
    }

    public boolean updateByFile(Path style) {
        StyleConfig cfg;
        try {
           cfg = new StyleConfig(style);
        } catch (Exception e) {
            System.out.println("Got Exception: " + e.getMessage());
            return false;
        }
        updateByConfig(cfg);
        return true;
    }

    private void updateByConfig(StyleConfig cfg) {
        line_width = cfg.line_width;
        line_color = cfg.line_color;
        outline_width = cfg.outline_width;
        padding = cfg.padding;
        max_radius = cfg.max_radius;
        h_sep = cfg.h_sep;
        v_sep = cfg.v_sep;
        arrows = cfg.arrows;
        title_pos = cfg.title_pos;
        bullet_fill = cfg.bullet_fill;
        text_color = cfg.text_color;
        shadow = cfg.shadow;
        shadow_fill = cfg.shadow_fill;
        title_font = cfg.title_font;

        nodeStyles = cfg.nodeStyles;
    }

    public int line_width;
    public Color line_color;
    public int outline_width;
    public int padding;
    public int max_radius;
    public int h_sep;
    public int v_sep;
    public boolean arrows;
    public TitlePosition title_pos;
    public Color bullet_fill;
    public Color text_color;
    public boolean shadow;
    public Color shadow_fill;
    public Font title_font;

    private final double scale;
    private final boolean transparent;

    public List<NodeStyle> nodeStyles;

    private static final NodeStyle defNodeStyle = new NodeStyle();

    public double getScale() {
        return scale;
    }

    public boolean isTransparent() {
        return transparent;
    }
}
