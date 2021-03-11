package org.atpfivt.jsyntrax.styles;

import java.awt.*;
import java.util.regex.Pattern;

public class NodeTokenStyle extends NodeStyle {
    public NodeTokenStyle() {
        super();
        super.name = "token";
        super.shape = "bubble";
        this.pattern = Pattern.compile("(.)");
        super.font = new Font("Sans",Font.BOLD, 16);
        super.fill = new Color(179, 229, 252);
    }
}
