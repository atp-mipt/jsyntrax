package org.atpfivt.jsyntrax.styles;

import org.ini4j.Profile.Section;
import org.ini4j.Wini;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class StyleConfig {
    public StyleConfig() {
        nodeStyles = new ArrayList<>(List.of(
                new NodeBubbleStyle(),
                new NodeBoxStyle(),
                new NodeTokenStyle(),
                new NodeHexStyle()));
    }

    public StyleConfig(Path style)
            throws IllegalAccessException, NoSuchFieldException, IOException {
        this();
        Wini ini = new Wini(style.toFile());
        if (ini.containsKey("style")) {
            parseStyleArgs(ini.get("style"));
        }

        nodeStyles.clear();

        for (Section s : ini.values()) {
            if (s.getName().equals("style")) {
                continue;
            }
            // this is custom node style
            NodeStyle ns = new NodeStyle();
            ns.name = s.getName();
            parseNodeStyle(s, ns);
            nodeStyles.add(ns);
        }
    }

    private void parseStyleArgs(Section s)
            throws NoSuchFieldException, IllegalAccessException {
        parseField(s, this, "line_width", Integer::parseInt);
        parseField(s, this, "outline_width", Integer::parseInt);
        parseField(s, this, "padding", Integer::parseInt);
        parseField(s, this, "line_color", StyleConfig::colorFromString);
        parseField(s, this, "max_radius", Integer::parseInt);
        parseField(s, this, "h_sep", Integer::parseInt);
        parseField(s, this, "v_sep", Integer::parseInt);
        parseField(s, this, "arrows", Boolean::parseBoolean);
        parseField(s, this, "title_pos", (String v) -> {
            return TitlePosition.valueOf(v.substring(1, v.length() - 1));
        });
        parseField(s, this, "bullet_fill", StyleConfig::colorFromString);
        parseField(s, this, "text_color", StyleConfig::colorFromString);
        parseField(s, this, "shadow", Boolean::parseBoolean);
        parseField(s, this, "shadow_fill", StyleConfig::colorFromString);
        parseField(s, this, "title_font", StyleConfig::fontFromString);
    }

    private void parseNodeStyle(Section s, NodeStyle ns)
            throws NoSuchFieldException, IllegalAccessException {
        Function<String, Object> patFunc = (String v) -> {
            return Pattern.compile(v.substring(1, v.length() - 1));
        };
        parseField(s, ns, "pattern", patFunc);
        parseField(s, ns, "shape", (String v) -> {
            return v.substring(1, v.length() - 1);
        });
        parseField(s, ns, "font", StyleConfig::fontFromString);
        parseField(s, ns, "text_color", StyleConfig::colorFromString);
        parseField(s, ns, "fill", StyleConfig::colorFromString);
    }

    private void parseField(Section s, Object obj,
                                   String name, Function<String, Object> parser)
            throws NoSuchFieldException, IllegalAccessException {
        if (s.containsKey(name)) {
            obj.getClass().getDeclaredField(name).set(obj,
                    parser.apply(s.get(name)));
        }
    }

    public static Font fontFromString(String txt) {
        Pattern fontPattern = Pattern.compile(
                "\\(\\s*'([a-zA-Z]+)'\\s*,\\s*(\\d+)\\s*,\\s*'([a-zA-Z ]+)'\\s*\\)");
        Matcher matcher = fontPattern.matcher(txt.trim());

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid font style in config");
        }
        String name = matcher.group(1);

        int style = Font.PLAIN;
        String styleText = matcher.group(3).toLowerCase();
        if (styleText.contains("bold")) {
            style |= Font.BOLD;
        }
        if (styleText.contains("italic")) {
            style |= Font.ITALIC;
        }
        int size = Integer.parseInt(matcher.group(2));
        return new Font(name, style, size);
    }

    public static Color colorFromString(String txt) {
        Pattern colorPattern = Pattern.compile(
                "\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*(,\\s*(\\d+)\\s*)?\\)");
        Matcher matcher = colorPattern.matcher(txt.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid color style in config");
        }
        int r = Integer.parseInt(matcher.group(1));
        int g = Integer.parseInt(matcher.group(2));
        int b = Integer.parseInt(matcher.group(3));
        int a;
        if (matcher.group(5) == null) {
            a = 255;
        } else {
            a = Integer.parseInt(matcher.group(5));
        }
        return new Color(r, g, b, a);
    }


    private final int lineWidth = 2;
    private final int outlineWidth = 2;
    private final int padding = 5;
    private final Color lineColor = new Color(0, 0, 0);
    private final int maxRadius = 9;
    private final int hSep = 17;
    private final int vSep = 9;
    private final boolean arrows = true;
    private final TitlePosition titlePos = TitlePosition.tl;
    private final Color bulletFill = new Color(255, 255, 255);
    private final Color textColor = new Color(0, 0, 0);
    private final boolean shadow = true;
    private final Color shadowFill = new Color(0, 0, 0, 127);
    private final Font titleFont = new Font("Sans", Font.BOLD, 22);
    private final List<NodeStyle> nodeStyles;

    public boolean getArrows() {
        return arrows;
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
        return hSep;
    }

    public int getVSep() {
        return vSep;
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
}

