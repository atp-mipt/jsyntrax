package com.syntrax.styles;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NodeBubbleStyle extends NodeStyle {

    NodeBubbleStyle() {
        super.name = "bubble";
        super.shape = "bubble";
        this.pattern = Pattern.compile("^\\w");
        this.font = new Font("Sans",Font.BOLD, 14);
        super.text_color = new Color(0,0,0);
        super.fill = new Color(179,229,252);
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
