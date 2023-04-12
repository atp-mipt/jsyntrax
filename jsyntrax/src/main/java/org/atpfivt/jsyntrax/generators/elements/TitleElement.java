package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.util.Pair;

import java.awt.Color;
import java.awt.Font;

public class TitleElement extends BubbleElementBase {
    public TitleElement(String text, Font textFont, String fontName, String tag) {

        super(new Pair<>(0, 0), SVGCanvasBuilder.getTextSize(text, textFont),
                null, text, new Pair<>(0, 0), null, fontName, new Color(0, 0, 0),
                0, new Color(255, 255, 255), tag);
    }

    @Override
    int getX(StyleConfig style) {
        int x0 = getStart().f;
        int x1 = getEnd().f;
        int x = (x0 + x1) / 2;
        switch (style.getTitlePos()) {
            case bl:
            case tl:
                x = x0;
                break;
            case bm:
            case tm:
                x = (x0 + x1) / 2;
                break;
            case br:
            case tr:
                x = x1;
                break;
        }
        return x;
    }

    @Override
    public void addShadow(StringBuilder sb, StyleConfig style) {

    }

    @Override
    public void toSVG(StringBuilder sb, StyleConfig style) {
        // Add text
        addXMLText(sb, style);
    }
}
