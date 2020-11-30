package org.atpfivt.jsyntrax.styles;

import java.awt.*;
import java.util.regex.*;

public class NodeBoxStyle extends NodeStyle {
    NodeBoxStyle() {
        super.name = "box";
        super.shape = "box";
        this.pattern = Pattern.compile("^/.");
        super.font = new Font("Times", Font.ITALIC, 14);
        super.fill = new Color(144, 164, 174);
    }

    public boolean match(String txt) {
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();
    }

    public String modify(String txt) {
        return txt.substring(1);
    }

    private final Pattern pattern;
}
