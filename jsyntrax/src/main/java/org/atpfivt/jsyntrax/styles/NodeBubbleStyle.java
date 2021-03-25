package org.atpfivt.jsyntrax.styles;

import java.awt.Color;
import java.awt.Font;
import java.util.regex.Pattern;

public class NodeBubbleStyle extends NodeStyle {

    public NodeBubbleStyle() {
        super.setName("bubble");
        super.setShape("bubble");
        super.setPattern(Pattern.compile("^(\\w.*)"));
        super.setFont(new Font("Sans", Font.BOLD,  14));
        super.setTextColor(new Color(0, 0, 0));
        super.setFill(new Color(179, 229, 252));
    }
}
