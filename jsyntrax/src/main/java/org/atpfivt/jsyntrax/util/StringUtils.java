package org.atpfivt.jsyntrax.util;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class StringUtils {
    private StringUtils() { }

    private static char hex(int val) {
        if (val < 10) {
            return (char) (val + (int) ('0'));
        }
        if (val < 16) {
            return (char) (val - 10 + (int) ('a'));
        }
        return '@';
    }

    public static Font fontFromString(String txt) {
        Pattern fontPattern = Pattern.compile(
                "\\(\\s*'([a-zA-Z ]+)'\\s*,\\s*(\\d+)\\s*,\\s*'([a-zA-Z ]+)'\\s*\\)");
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

    public static String escapeXML(String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    public static String snakeToCamelCase(String snakeCase) {
        String[] parts = snakeCase.split("_");
        StringBuilder camelCaseString = new StringBuilder(parts[0]);
        for (String part : Arrays.copyOfRange(parts, 1, parts.length)) {
            camelCaseString
                    .append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1));
        }
        return camelCaseString.toString();
    }

    public static String toHex(Color c) {
        return "#" + hex(c.getRed() / 16) + hex(c.getRed() % 16)
                + hex(c.getGreen() / 16) + hex(c.getGreen() % 16)
                + hex(c.getBlue() / 16) + hex(c.getBlue() % 16);
    }

    public static Double fillOpacity(Color c) {
        return c.getAlpha() / 255.0;
    }


}
