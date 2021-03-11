package org.atpfivt.jsyntrax.styles;

import java.awt.*;
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
        line_width = cfg.getLineWidth();
        line_color = cfg.getLineColor();
        outline_width = cfg.getOutlineWidth();
        padding = cfg.getPadding();
        max_radius = cfg.getMaxRadius();
        h_sep = cfg.getHSep();
        v_sep = cfg.getVSep();
        arrows = cfg.getArrows();
        title_pos = cfg.getTitlePos();
        bullet_fill = cfg.getBulletFill();
        text_color = cfg.getTextColor();
        shadow = cfg.isShadow();
        shadow_fill = cfg.getShadowFill();
        title_font = cfg.getTitleFont();

        nodeStyles = cfg.getNodeStyles();
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
