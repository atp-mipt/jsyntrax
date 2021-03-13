package org.atpfivt.jsyntrax.styles;

import java.awt.Color;
import java.awt.Font;
import java.util.regex.Pattern;

public class NodeHexStyle extends NodeStyle {
    public NodeHexStyle() {
        super.setName("hex");
        super.setShape("hex");
        super.setPattern(Pattern.compile("\\w+"));
        super.setFont(new Font("Sans", Font.BOLD, 14));
        super.setFill(new Color(255, 0, 0, 127));
    }
}
