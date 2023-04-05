package org.atpfivt.jsyntrax.styles;

import org.atpfivt.jsyntrax.Main;
import org.atpfivt.jsyntrax.util.StringUtils;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;


public final class StyleConfig {
    private int lineWidth = 2;
    private int outlineWidth = 2;
    private int padding = 5;
    private Color lineColor = new Color(0, 0, 0);
    private int maxRadius = 9;
    private int hSep = 17;
    private int vSep = 9;
    private boolean arrows = true;
    private TitlePosition titlePos = TitlePosition.tl;
    private Color bulletFill = new Color(255, 255, 255);
    private Color textColor = new Color(0, 0, 0);
    private boolean shadow = true;
    private Color shadowFill = new Color(0, 0, 0, 127);
    private Font titleFont = new Font("Sans", Font.BOLD, 22);
    private double scale;
    private boolean transparency;
    private NodeStyle defNodeStyle = new NodeStyle();
    private List<NodeStyle> nodeStyles = new ArrayList<>(List.of(
            new NodeBubbleStyle(),
            new NodeBoxStyle(),
            new NodeTokenStyle(),
            new NodeHexStyle()
    ));


    private void parseStyleArgs(Section s) throws IOException {
        try {
            parseField(s, this, "line_width", Integer::parseInt);
            parseField(s, this, "outline_width", Integer::parseInt);
            parseField(s, this, "padding", Integer::parseInt);
            parseField(s, this, "line_color", StringUtils::colorFromString);
            parseField(s, this, "max_radius", Integer::parseInt);
            parseField(s, this, "h_sep", Integer::parseInt);
            parseField(s, this, "v_sep", Integer::parseInt);
            parseField(s, this, "arrows", Boolean::parseBoolean);
            parseField(s, this, "title_pos", (String v) -> TitlePosition.valueOf(v.substring(1, v.length() - 1)));
            parseField(s, this, "bullet_fill", StringUtils::colorFromString);
            parseField(s, this, "text_color", StringUtils::colorFromString);
            parseField(s, this, "shadow", Boolean::parseBoolean);
            parseField(s, this, "shadow_fill", StringUtils::colorFromString);
            parseField(s, this, "title_font", StringUtils::fontFromString);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IOException(e);
        }
    }

    private void parseNodeStyle(Section s, NodeStyle ns) throws IOException {
        try {
            Function<String, Object> patFunc = (String v) -> Pattern.compile(v.substring(1, v.length() - 1));
            parseField(s, ns, "pattern", patFunc);
            parseField(s, ns, "shape", (String v) -> v.substring(1, v.length() - 1));
            parseField(s, ns, "font", StringUtils::fontFromString);
            parseField(s, ns, "text_color", StringUtils::colorFromString);
            parseField(s, ns, "fill", StringUtils::colorFromString);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IOException(e);
        }
    }


    private void parseField(Section s, Object obj,
                            String propertyName, Function<String, Object> parser)
            throws NoSuchFieldException, IllegalAccessException {
        if (s.containsKey(propertyName)) {
            Field field = obj.getClass()
                    .getDeclaredField(StringUtils.snakeToCamelCase(propertyName));
            field.setAccessible(true);
            field.set(obj, parser.apply(s.get(propertyName)));
        } else {
            System.out.println(
                    "[StyleConfigParser] Warning: section ["
                            + s.getName() + "]"
                            + " does not contain property ["
                            + propertyName + "]");
        }
    }


    public StyleConfig(double scale, boolean transparency) throws IOException {
        this.setTransparency(transparency);
        this.setScale(scale);

        Wini config = new Wini(Main.class
                .getResourceAsStream("/jsyntrax.ini")
        );
        if (config.containsKey("style")) {
            parseStyleArgs(config.get("style"));
        }
    }


    public StyleConfig(double scale, boolean transparency, Path style) throws IOException {
        this.setTransparency(transparency);
        this.setScale(scale);

        Wini ini = new Wini(style.toFile());
        if (ini.containsKey("style")) {
            parseStyleArgs(ini.get("style"));
        }
        getNodeStyles().clear();
        for (Section s : ini.values()) {
            if (s.getName().equals("style")) {
                continue;
            }
            // this is custom node style
            NodeStyle ns = new NodeStyle();
            ns.setName(s.getName());
            parseNodeStyle(s, ns);
            getNodeStyles().add(ns);
        }
    }


    public NodeStyle getNodeStyle(String txt) {
        for (NodeStyle ns : getNodeStyles()) {
            if (ns.match(txt)) {
                return ns;
            }
        }
        return getDefNodeStyle();
    }


    public boolean getArrows() {
        return isArrows();
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public int getOutlineWidth() {
        return outlineWidth;
    }

    public int getPadding() {
        return padding;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public int getHSep() {
        return gethSep();
    }

    public int getVSep() {
        return getvSep();
    }

    public TitlePosition getTitlePos() {
        return titlePos;
    }

    public Color getBulletFill() {
        return bulletFill;
    }

    public Color getTextColor() {
        return textColor;
    }

    public boolean isShadow() {
        return shadow;
    }

    public Color getShadowFill() {
        return shadowFill;
    }

    public Font getTitleFont() {
        return titleFont;
    }

    public List<NodeStyle> getNodeStyles() {
        return nodeStyles;
    }

    public double getScale() {
        return scale;
    }

    public boolean isTransparent() {
        return isTransparency();
    }

    public int gethSep() {
        return hSep;
    }

    public int getvSep() {
        return vSep;
    }

    public boolean isArrows() {
        return arrows;
    }

    public boolean isTransparency() {
        return transparency;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setOutlineWidth(int outlineWidth) {
        this.outlineWidth = outlineWidth;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }

    public void sethSep(int hSep) {
        this.hSep = hSep;
    }

    public void setvSep(int vSep) {
        this.vSep = vSep;
    }

    public void setArrows(boolean arrows) {
        this.arrows = arrows;
    }

    public void setTitlePos(TitlePosition titlePos) {
        this.titlePos = titlePos;
    }

    public void setBulletFill(Color bulletFill) {
        this.bulletFill = bulletFill;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public void setShadowFill(Color shadowFill) {
        this.shadowFill = shadowFill;
    }

    public void setTitleFont(Font titleFont) {
        this.titleFont = titleFont;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setTransparency(boolean transparency) {
        this.transparency = transparency;
    }

    public NodeStyle getDefNodeStyle() {
        return defNodeStyle;
    }

    public void setDefNodeStyle(NodeStyle defNodeStyle) {
        this.defNodeStyle = defNodeStyle;
    }

    public void setNodeStyles(List<NodeStyle> nodeStyles) {
        this.nodeStyles = nodeStyles;
    }
}

