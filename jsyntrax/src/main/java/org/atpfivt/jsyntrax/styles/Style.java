package org.atpfivt.jsyntrax.styles;

import java.awt.Color;
import java.awt.Font;
import java.nio.file.Path;
import java.util.List;

public final class Style {
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
        lineWidth = cfg.getLineWidth();
        lineColor = cfg.getLineColor();
        outlineWidth = cfg.getOutlineWidth();
        padding = cfg.getPadding();
        maxRadius = cfg.getMaxRadius();
        hSep = cfg.getHSep();
        vSep = cfg.getVSep();
        arrows = cfg.getArrows();
        titlePos = cfg.getTitlePos();
        bulletFill = cfg.getBulletFill();
        textColor = cfg.getTextColor();
        shadow = cfg.isShadow();
        shadowFill = cfg.getShadowFill();
        titleFont = cfg.getTitleFont();

        nodeStyles = cfg.getNodeStyles();
    }

    private int lineWidth;
    private Color lineColor;
    private int outlineWidth;
    private int padding;
    private int maxRadius;
    private int hSep;
    private int vSep;
    private boolean arrows;
    private TitlePosition titlePos;
    private Color bulletFill;
    private Color textColor;
    private boolean shadow;
    private Color shadowFill;
    private Font titleFont;

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

    public int getLineWidth() {
        return lineWidth;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public int getOutlineWidth() {
        return outlineWidth;
    }

    public int getPadding() {
        return padding;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.getMaxRadius() = maxRadius;
    }

    public int gethSep() {
        return hSep;
    }
}
