package org.atpfivt.jsyntrax.styles;

import org.ini4j.Profile.Section;
import org.ini4j.Wini;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class StyleConfig {
    public StyleConfig() {
        nodeStyles = new ArrayList<>(List.of(
                new NodeBubbleStyle(),
                new NodeBoxStyle(),
                new NodeTokenStyle(),
                new NodeHexStyle()));
    }

    public StyleConfig(Path style) throws IOException, NoSuchFieldException, IllegalAccessException {
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
        parseField(s, this, "title_pos", (String v) -> v.substring(1, v.length() - 1));
        parseField(s, this, "bullet_fill", StyleConfig::colorFromString);
        parseField(s, this, "text_color", StyleConfig::colorFromString);
        parseField(s, this, "shadow", Boolean::parseBoolean);
        parseField(s, this, "shadow_fill", StyleConfig::colorFromString);
        parseField(s, this, "title_font", StyleConfig::fontFromString);
        parseField(s, this, "scale", Double::parseDouble);
        parseField(s, this, "transparent", Boolean::parseBoolean);
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

    private static Font fontFromString(String txt) {
        String[] fields = txt.replace("(", "")
                .replace(")", "")
                .replace(" ", "")
                .replace("'", "")
                .split(",");

        if (fields.length != 3) {
            throw new IllegalArgumentException("Invalid count of font fields in config");
        }

        String name = fields[0];
        int style = 0;
        if (fields[2].toLowerCase().equals("bold")) {
            style |= Font.BOLD;
        }
        if (fields[2].toLowerCase().equals("italic")) {
            style |= Font.ITALIC;
        }
        int size = Integer.parseInt(fields[1]);

        return new Font(name, style, size);
    }

    private static Color colorFromString(String txt) {
        String[] fields = txt.replace("(", "")
                .replace(")", "")
                .replace(" ", "")
                .replace("'", "")
                .split(",");

        if (fields.length < 3 || fields.length > 4) {
            throw new IllegalArgumentException("Invalid count of color fields in config");
        }

        int r = Integer.parseInt(fields[0]);
        int g = Integer.parseInt(fields[1]);
        int b = Integer.parseInt(fields[2]);
        int a = 255;

        if (fields.length == 4) {
            a = Integer.parseInt(fields[3]);
        }

        return new Color(r, g, b, a);
    }


    public int line_width = 2;
    public int outline_width = 2;
    public int padding = 5;
    public Color line_color = colorFromString("(0, 0, 0)");
    public int max_radius = 9;
    public int h_sep = 17;
    public int v_sep = 9;
    public boolean arrows = true;
    public String title_pos = "tl";
    public Color bullet_fill = colorFromString("(255, 255, 255)");
    public Color text_color = colorFromString("(0, 0, 0)");
    public boolean shadow = true;
    public Color shadow_fill = colorFromString("(0, 0, 0, 127)");
    public Font title_font = fontFromString("('Sans', 22, 'bold')");
    public double scale = 1;
    public boolean transparent = false;

    public List<NodeStyle> nodeStyles;
}
