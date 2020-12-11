package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.styles.Style;
import org.atpfivt.jsyntrax.util.Pair;

import java.awt.*;

public class TitleElement extends BubbleElementBase {
    public TitleElement(String text, Font textFont, String fontName, String tag) {

        super(new Pair<>(0, 0), SVGCanvasBuilder.getTextSize(text,textFont),
                null, text, new Pair<>(0, 0), null, fontName, new Color(0, 0, 0),
                0, new Color(255, 255, 255), tag);
    }

    @Override
    public void toSVG(StringBuilder sb, Style style) {
        // Add text
        addXMLText(sb, style);
    }
}
