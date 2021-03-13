package org.atpfivt.jsyntrax.styles;

import java.awt.Color;
import java.awt.Font;
import java.util.regex.Pattern;

public class NodeTokenStyle extends NodeStyle {
    public NodeTokenStyle() {
        super();
        super.setName("token");
        super.setShape("bubble");
        this.setPattern(Pattern.compile("(.)"));
        super.setFont(new Font("Sans", Font.BOLD, 16));
        super.setFill(new Color(179, 229, 252));
    }
}
