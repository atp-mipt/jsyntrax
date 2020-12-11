package org.atpfivt.jsyntrax.styles;

import org.ini4j.Profile.Section;
import org.ini4j.Wini;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StyleConfig {
    public StyleConfig() {
        nodeStyles = new ArrayList<>(List.of(
                new NodeBubbleStyle(),
                new NodeBoxStyle(),
                new NodeTokenStyle(),
                new NodeHexStyle()));
    }

    public StyleConfig(Path style) throws IllegalAccessException, NoSuchFieldException, IOException {
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

    private void parseStyleArgs(Section s) throws NoSuchFieldException, IllegalAccessException {
        parseField(s, this, "line_width", Integer::parseInt);
        parseField(s, this, "outline_width", Integer::parseInt);
        parseField(s, this, "padding", Integer::parseInt);
        parseField(s, this, "line_color", StyleConfig::colorFromString);
        parseField(s, this, "max_radius", Integer::parseInt);
        parseField(s, this, "h_sep", Integer::parseInt);
        parseField(s, this, "v_sep", Integer::parseInt);
        parseField(s, this, "arrows", Boolean::parseBoolean);
        parseField(s, this, "title_pos", (String v) ->
                TitlePosition.valueOf(v.substring(1, v.length() - 1)));
        parseField(s, this, "bullet_fill", StyleConfig::colorFromString);
        parseField(s, this, "text_color", StyleConfig::colorFromString);
        parseField(s, this, "shadow", Boolean::parseBoolean);
        parseField(s, this, "shadow_fill", StyleConfig::colorFromString);
        parseField(s, this, "title_font", StyleConfig::fontFromString);
    }

    private void parseNodeStyle(Section s, NodeStyle ns)
            throws NoSuchFieldException, IllegalAccessException {
        Function<String, Object> patFunc = (String v) -> Pattern.compile(v.substring(1, v.length() - 1));
        parseField(s, ns, "pattern", patFunc);
        parseField(s, ns, "shape", (String v) -> v.substring(1, v.length() - 1));
        parseField(s, ns, "font", StyleConfig::fontFromString);
        parseField(s, ns, "text_color", StyleConfig::colorFromString);
        parseField(s, ns, "fill", StyleConfig::colorFromString);
    }

    private static void parseField(Section s, Object obj,
                                   String name, Function<String, Object> parser)
            throws NoSuchFieldException, IllegalAccessException {
        if (s.containsKey(name)) {
            obj.getClass().getDeclaredField(name).set(obj,
                    parser.apply(s.get(name)));
        }
    }

    final static Pattern fontPattern = Pattern.compile(
            "\\(\\s*'([a-zA-Z]+)'\\s*,\\s*(\\d+)\\s*,\\s*'([a-zA-Z ]+)'\\s*\\)");

    static Font fontFromString(String txt) {
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

    final static Pattern colorPattern = Pattern.compile(
            "\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*(,\\s*(\\d+)\\s*)?\\)");

    static Color colorFromString(String txt) {
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


    public int line_width = 2;
    public int outline_width = 2;
    public int padding = 5;
    public Color line_color = new Color(0, 0, 0);
    public int max_radius = 9;
    public int h_sep = 17;
    public int v_sep = 9;
    public boolean arrows = true;
    public TitlePosition title_pos = TitlePosition.tl;
    public Color bullet_fill = new Color(255, 255, 255);
    public Color text_color = new Color(0, 0, 0);
    public boolean shadow = true;
    public Color shadow_fill = new Color(0, 0, 0, 127);
    public Font title_font = new Font("Sans", Font.BOLD, 22);

    public final List<NodeStyle> nodeStyles;
}
