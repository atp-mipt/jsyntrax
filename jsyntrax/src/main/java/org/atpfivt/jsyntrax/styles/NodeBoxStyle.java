package org.atpfivt.jsyntrax.styles;

import java.awt.*;
import java.util.regex.*;

public class NodeBoxStyle extends NodeStyle {
    public NodeBoxStyle() {
        super.name = "box";
        super.shape = "box";
        super.pattern = Pattern.compile("^/.");
        super.font = new Font("Times", Font.ITALIC, 14);
        super.fill = new Color(144, 164, 174);
    }

    @Override
    public String modify(String txt) {
        return txt.substring(1);
    }

}
