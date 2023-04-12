package org.atpfivt.jsyntrax;

import org.approvaltests.Approvals;
import org.approvaltests.core.Options;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.styles.TitlePosition;
import org.atpfivt.jsyntrax.units.Unit;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.atpfivt.jsyntrax.groovy_parser.SyntraxScript.*;

public class JSyntraxTitleTest {
    private final SVGCanvasBuilder canvasBuilder;

    private final StyleConfig style;

    public JSyntraxTitleTest()
            throws IllegalAccessException, NoSuchFieldException, IOException {
        style = new StyleConfig(1, false);
        JSyntraxTestUtils.updateStyle(style);
        canvasBuilder = new SVGCanvasBuilder().withStyle(style);
    }

    private void setTitlePosition(TitlePosition pos) {
        style.setTitlePos(pos);
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


    @ParameterizedTest
    @MethodSource("titlePosProvider")
    void titleTest(TitlePosition pos) {
        setTitlePosition(pos);
        Unit root = getSample();
        String title = getTitle();
        SVGCanvas c = canvasBuilder.withTitle(title).generateSVG(root);
        String result = c.generateSVG();
        Approvals.verify(result, new Options()
                .forFile()
                .withName(JSyntraxTitleTest.class.getSimpleName() + '.' + pos.name(), ".svg")
        );
    }

    private static String getLongTitle() {
        return "12345678901234567890123456789012345678901234567890" +
               "12345678901234567890123456789012345678901234567890";
    }

    @ParameterizedTest
    @MethodSource("titlePosProvider")
    void longTitleTest(TitlePosition pos) {
        setTitlePosition(pos);
        Unit root = getSample();
        String title = getLongTitle();
        SVGCanvas c = canvasBuilder.withTitle(title).generateSVG(root);
        String result = c.generateSVG();
        Approvals.verify(result, new Options()
                .forFile()
                .withName("JSyntraxLongTitleTest" + '.' + pos.name(), ".svg")
        );
    }

    private static Unit getSampleExceedingDocumentBoundaries() {
        return line("for", '(',
                loop(opt(opt("Typ"),
                        "/Bezeichner", '=', "/Anweisung"), ","),
                ';', opt("/boolscher Ausdruck"), ';',
                opt(loop("/Anweisung", ',')), ')', "/Anweisung");
    }

    @ParameterizedTest
    @MethodSource("titlePosProvider")
    void exceedingDocumentBoundariesTest(TitlePosition pos) {
        setTitlePosition(pos);
        Unit root = getSampleExceedingDocumentBoundaries();
        String title = "klassische for-Loop (jsyntrax, cmd-2)";
        SVGCanvas c = canvasBuilder.withTitle(title).generateSVG(root);
        String result = c.generateSVG();
        Approvals.verify(result, new Options()
                .forFile()
                .withName("ExceedingDocumentBoundariesTest" + '.' + pos.name(), ".svg")
        );
    }

    static Stream<TitlePosition> titlePosProvider() {
        return Arrays.stream(TitlePosition.values());
    }

}
