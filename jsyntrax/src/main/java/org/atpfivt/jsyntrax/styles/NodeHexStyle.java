package org.atpfivt.jsyntrax.styles;

import java.awt.*;
import java.util.regex.Pattern;

public class NodeHexStyle extends NodeStyle {
    public NodeHexStyle() {
        super.name = "hex";
        super.shape = "hex";
        super.pattern = Pattern.compile("\\w+");
        super.font = new Font("Sans", Font.BOLD, 14);
        super.fill = new Color(255, 0, 0, 127);
    }
}
