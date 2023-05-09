package org.atpfivt.jsyntrax;

import org.approvaltests.Approvals;
import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.atpfivt.jsyntrax.JSyntraxTestUtils.OPTIONS;

public class JSyntraxTitleKeywordTest {

    @Test
    void titleAboveDiagram() throws IOException {
        StyleConfig styleConfig = new StyleConfig(1.0, true);
        final String svg = Main.generateSVG(null, styleConfig, "title('defined above')\nline ('a', 'b', 'c')");
        Approvals.verify(svg, OPTIONS);
    }

    @Test
    void titleBelowDiagram() throws IOException {
        StyleConfig styleConfig = new StyleConfig(1.0, true);
        final String svg = Main.generateSVG(null, styleConfig, "line ('a', 'b', 'c')\ntitle('defined below')");
        Approvals.verify(svg, OPTIONS);
    }

    @Test
    void noTitle() throws IOException {
        StyleConfig styleConfig = new StyleConfig(1.0, true);
        final String svg = Main.generateSVG(null, styleConfig, "line ('a', 'b', 'c')");
        Approvals.verify(svg, OPTIONS);
    }

    @Test
    void titleFromArguments() throws IOException {
        StyleConfig styleConfig = new StyleConfig(1.0, true);
        final String svg = Main.generateSVG("arguments", styleConfig, "line ('a', 'b', 'c')");
        Approvals.verify(svg, OPTIONS);
    }

    @Test
    void titleFromArgumentsOverridesTheDiagramTitle() throws IOException {
        StyleConfig styleConfig = new StyleConfig(1.0, true);
        final String svg = Main.generateSVG("arguments", styleConfig, "title('inline')\nline ('a', 'b', 'c')");
        Approvals.verify(svg, OPTIONS);
    }
}
