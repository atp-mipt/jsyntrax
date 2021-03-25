package org.atpfivt.jsyntrax.styles;

import java.awt.Color;
import java.awt.Font;
import java.util.regex.Pattern;

public class NodeBoxStyle extends NodeStyle {
    public NodeBoxStyle() {
        super.setName("box");
        super.setShape("box");
        super.setPattern(Pattern.compile("^/(.*)"));
        super.setFont(new Font("Times", Font.ITALIC, 14));
        super.setFill(new Color(144, 164, 174));
    }
}
