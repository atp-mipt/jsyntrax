package org.atpfivt.jsyntrax;

import org.approvaltests.Approvals;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.styles.Style;
import org.atpfivt.jsyntrax.styles.TitlePosition;
import org.atpfivt.jsyntrax.units.Unit;
import org.junit.jupiter.api.Test;

import static org.atpfivt.jsyntrax.JSyntraxTestUtils.OPTIONS;
import static org.atpfivt.jsyntrax.groovy_parser.SyntraxScript.*;
import static org.atpfivt.jsyntrax.groovy_parser.SyntraxScript.choice;

public class JSyntraxTitleTest {
    private final SVGCanvasBuilder canvasBuilder;

    private final Style style;

    public JSyntraxTitleTest() {
        style = new Style(1, false);
        JSyntraxTestUtils.updateStyle(style);
        canvasBuilder = new SVGCanvasBuilder().withStyle(style);
    }

    private void setTitlePosition(TitlePosition pos) {
        style.title_pos = pos;
        JSyntraxTestUtils.updateStyle(style);
        canvasBuilder.withStyle(style);
    }

    private static Unit getSample() {
        return line("/create_grain", loop(";",
                choice("/create_sequence",
                        "/create_table",
                        "/add_foreign_key",
                        "/create_index",
                        "/create_view",
                        "/create_materialized_view",
                        "/create_function"))
        );
    }

    private static String getTitle() {
        return "<TestTitle>";
    }

    @Test
    void titleTLTest() {
        setTitlePosition(TitlePosition.tl);
        Unit root = getSample();
        String title = getTitle();

        SVGCanvas c = canvasBuilder.withTitle(title).generateSVG(root);
        String result = c.generateSVG();

        Approvals.verify(result, OPTIONS);
    }

    @Test
    void titleTMTest() {
        setTitlePosition(TitlePosition.tm);
        Unit root = getSample();
        String title = getTitle();

        SVGCanvas c = canvasBuilder.withTitle(title).generateSVG(root);
        String result = c.generateSVG();

        Approvals.verify(result, OPTIONS);
    }

    @Test
    void titleTRTest() {
        setTitlePosition(TitlePosition.tr);
        Unit root = getSample();
        String title = getTitle();

        SVGCanvas c = canvasBuilder.withTitle(title).generateSVG(root);
        String result = c.generateSVG();

        Approvals.verify(result, OPTIONS);
    }

    @Test
    void titleBLTest() {
        setTitlePosition(TitlePosition.bl);
        Unit root = getSample();
        String title = getTitle();

        SVGCanvas c = canvasBuilder.withTitle(title).generateSVG(root);
        String result = c.generateSVG();

        Approvals.verify(result, OPTIONS);
    }

    @Test
    void titleBMTest() {
        setTitlePosition(TitlePosition.bm);
        Unit root = getSample();
        String title = getTitle();

        SVGCanvas c = canvasBuilder.withTitle(title).generateSVG(root);
        String result = c.generateSVG();

        Approvals.verify(result, OPTIONS);
    }

    @Test
    void titleBRTest() {
        setTitlePosition(TitlePosition.br);
        Unit root = getSample();
        String title = getTitle();

        SVGCanvas c = canvasBuilder.withTitle(title).generateSVG(root);
        String result = c.generateSVG();

        Approvals.verify(result, OPTIONS);
    }
}
