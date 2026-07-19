package org.atpfivt.jsyntrax.parser;

import org.atpfivt.jsyntrax.Configuration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParserTest {

    @Test
    void lineCommentsAreIgnored() {
        Configuration configuration = new Parser(
                "// leading comment\n"
                        + "line('a', 'b') // trailing comment\n"
                        + "// closing comment").getNode();
        assertThat(configuration.getTrack().getUnits()).hasSize(2);
    }

    @Test
    void blockCommentsAreIgnored() {
        Configuration configuration = new Parser(
                "/* a comment\n"
                        + " * spanning several lines\n"
                        + " */\n"
                        + "line(/* before */ 'a' /* between */, 'b') /* after */").getNode();
        assertThat(configuration.getTrack().getUnits()).hasSize(2);
    }

    @Test
    void commentsOnlyScriptIsRejected() {
        assertThatExceptionOfType(SyntraxParseException.class)
                .isThrownBy(() -> new Parser("// nothing here\n/* still nothing */"));
    }

    @Test
    void emptyScriptIsRejected() {
        assertThatExceptionOfType(SyntraxParseException.class)
                .isThrownBy(() -> new Parser(""));
    }

    @Test
    void unknownFunctionListsAlternatives() {
        assertThatThrownBy(() -> new Parser("lien('a', 'b')"))
                .isInstanceOf(SyntraxParseException.class)
                .hasMessageContaining("\"line\"")
                .hasMessageContaining("\"loop\"");
    }

    @Test
    void semicolonsBetweenStatementsAreAllowed() {
        Parser parser = new Parser("title('t'); line('a');");
        assertThat(parser.getTitle()).isEqualTo("t");
        assertThat(parser.getNode().getTrack().getUnits()).hasSize(1);
    }

    @Test
    void arbitraryCodeIsRejected() {
        assertThatExceptionOfType(SyntraxParseException.class)
                .isThrownBy(() -> new Parser("System.exit(42)"));
    }
}
