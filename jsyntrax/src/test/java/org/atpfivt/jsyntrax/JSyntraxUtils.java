package org.atpfivt.jsyntrax;

import org.approvaltests.core.Options;
import org.atpfivt.jsyntrax.styles.NodeStyle;
import org.atpfivt.jsyntrax.styles.Style;

import java.awt.*;
import java.util.ArrayList;

public class JSyntraxUtils {

    private static JSyntraxUtils instance = new JSyntraxUtils();

    public final Options options;

    private final Font[] testFonts = new Font[4];

    private JSyntraxUtils() {
        try {
            options = new Options().forFile().withExtension(".svg");
            String[] names = {"PTSans-Regular.ttf", "PTSans-Bold.ttf",
                    "PTSans-Italic.ttf", "PTSans-BoldItalic.ttf"};
            for (int i = 0; i < 4; ++i) {
                testFonts[i] = Font.createFont(Font.TRUETYPE_FONT,
                        JSyntraxUtils.class.getResourceAsStream(names[i]))
                        .deriveFont(12.f);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static JSyntraxUtils getInstance() {
        return instance;
    }

    public void updateStyle(Style s) {
        s.title_font = testFonts[s.title_font.getStyle()];

        for (NodeStyle ns : s.nodeStyles) {
            ns.font = testFonts[ns.font.getStyle()];
        }
    }
}
