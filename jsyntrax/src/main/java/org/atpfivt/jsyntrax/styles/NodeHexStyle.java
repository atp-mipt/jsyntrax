package org.atpfivt.jsyntrax.styles;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NodeHexStyle extends NodeStyle {
    NodeHexStyle() {
        super.name = "hex";
        super.shape = "hex";
        this.pattern = Pattern.compile("^\\w");
        super.font = new Font("Sans", Font.BOLD, 14);
        super.fill = new Color(255, 0, 0, 127);
    }

    public boolean match(String txt) {
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();
    }

    public String modify(String txt) {
        return txt;
    }

    private final Pattern pattern;
}
