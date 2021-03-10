package org.atpfivt.jsyntrax.styles;

import java.awt.*;
import java.util.regex.*;

public class NodeBubbleStyle extends NodeStyle {

    public NodeBubbleStyle() {
        super.name = "bubble";
        super.shape = "bubble";
        super.pattern = Pattern.compile("^(\\w.*)");
        super.font = new Font("Sans",Font.BOLD, 14);
        super.text_color = new Color(0,0,0);
        super.fill = new Color(179,229,252);
    }
}
