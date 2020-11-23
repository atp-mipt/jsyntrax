package com.syntrax.styles;

import java.awt.*;
import java.util.regex.*;

public class NodeTokenStyle extends NodeStyle {
    NodeTokenStyle() {
        super();
        super.name = "token";
        super.shape = "bubble";
        this.pattern = Pattern.compile(".");
        super.font = new Font("Sans",Font.BOLD, 16);
        super.fill = new Color(179, 229, 252);
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
