package org.atpfivt.jsyntrax;

import org.approvaltests.Approvals;
import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.atpfivt.jsyntrax.JSyntraxTestUtils.OPTIONS;

public class JSyntraxTitleKeywordTest {

    private StyleConfig s;

    @BeforeEach
    void setUp() throws IOException {
        s = new StyleConfig(1, false);
        JSyntraxTestUtils.updateStyle(s);
    }


    @Test
    void titleAboveDiagram() throws IOException {
        final String svg = Main.generateSVG(null, s, "title('defined above')\nline ('a', 'b', 'c')");
        Approvals.verify(svg, OPTIONS);
    }

    @Test
    void titleBelowDiagram() throws IOException {
        final String svg = Main.generateSVG(null, s, "line ('a', 'b', 'c')\ntitle('defined below')");
        Approvals.verify(svg, OPTIONS);
    }

    @Test
    void noTitle() throws IOException {
        final String svg = Main.generateSVG(null, s, "line ('a', 'b', 'c')");
        Approvals.verify(svg, OPTIONS);
    }

    @Test
    void titleFromArguments() throws IOException {
        final String svg = Main.generateSVG("arguments", s, "line ('a', 'b', 'c')");
        Approvals.verify(svg, OPTIONS);
    }

    @Test
    void titleFromArgumentsOverridesTheDiagramTitle() throws IOException {
        final String svg = Main.generateSVG("arguments", s, "title('inline')\nline ('a', 'b', 'c')");
        Approvals.verify(svg, OPTIONS);
    }
}
