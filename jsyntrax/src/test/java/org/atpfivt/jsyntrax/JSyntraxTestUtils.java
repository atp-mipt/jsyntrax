package org.atpfivt.jsyntrax;

import org.approvaltests.core.Options;
import org.atpfivt.jsyntrax.styles.NodeStyle;
import org.atpfivt.jsyntrax.styles.Style;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JSyntraxTestUtils {

    public final static Options OPTIONS = new Options().forFile().withExtension(".svg");

    private final static List<Font> testFonts = Stream.of(
            "PTSans-Regular.ttf", "PTSans-Bold.ttf", "PTSans-Italic.ttf", "PTSans-BoldItalic.ttf")
            .map(name -> {
                try {
                    return Font.createFont(Font.TRUETYPE_FONT,
                            JSyntraxTestUtils.class.getResourceAsStream(name));
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }).collect(Collectors.toList());


    public static Font transformFont(Font f) {
        return testFonts.get(f.getStyle()).deriveFont(f.getStyle(), f.getSize());
    }

    public static void updateStyle(Style s) {
        s.title_font = transformFont(s.title_font);
        for (NodeStyle ns : s.nodeStyles) {
            ns.font = transformFont(ns.font);
        }
    }
}
