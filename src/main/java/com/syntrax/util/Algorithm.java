package com.syntrax.util;

import java.awt.*;

public class Algorithm {
    public static String escapeXML(String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    public static String toHex(Color c) {
        return "#" + Hex(c.getRed() / 16) + Hex(c.getRed() % 16)
                + Hex(c.getGreen() / 16) + Hex(c.getGreen() % 16)
                + Hex(c.getBlue() / 16) + Hex(c.getBlue() % 16);
    }

    public static Double fillOpacity(Color c) {
        return c.getAlpha() / 255.0;
    }

    private static char Hex(int val) {
        if (val < 10) {
            return (char)(val + (int)('0'));
        }
        if (val < 16) {
            return (char)(val - 10 + (int)('a'));
        }
        return '@';
    }
}
