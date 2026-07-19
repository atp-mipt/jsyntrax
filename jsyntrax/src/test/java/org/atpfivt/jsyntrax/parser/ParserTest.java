package org.atpfivt.jsyntrax.parser;

import org.atpfivt.jsyntrax.Configuration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParserTest {

    @Test
    void lineCommentsAreIgnored() {
        Configuration configuration = new Parser(
                "// leading comment\n"
                        + "line('a', 'b') // trailing comment\n"
                        + "// closing comment").getNode();
        assertEquals(2, configuration.getTrack().getUnits().size());
    }

    @Test
    void blockCommentsAreIgnored() {
        Configuration configuration = new Parser(
                "/* a comment\n"
                        + " * spanning several lines\n"
                        + " */\n"
                        + "line(/* before */ 'a' /* between */, 'b') /* after */").getNode();
        assertEquals(2, configuration.getTrack().getUnits().size());
    }

    @Test
    void commentsOnlyScriptIsRejected() {
        assertThrows(SyntraxParseException.class,
                () -> new Parser("// nothing here\n/* still nothing */"));
    }

    @Test
    void emptyScriptIsRejected() {
        assertThrows(SyntraxParseException.class, () -> new Parser(""));
    }

    @Test
    void unknownFunctionListsAlternatives() {
        SyntraxParseException e = assertThrows(SyntraxParseException.class,
                () -> new Parser("lien('a', 'b')"));
        assertTrue(e.getMessage().contains("\"line\""));
        assertTrue(e.getMessage().contains("\"loop\""));
    }

    @Test
    void semicolonsBetweenStatementsAreAllowed() {
        Parser parser = new Parser("title('t'); line('a');");
        assertEquals("t", parser.getTitle());
        assertEquals(1, parser.getNode().getTrack().getUnits().size());
    }

    @Test
    void arbitraryCodeIsRejected() {
        assertThrows(SyntraxParseException.class,
                () -> new Parser("System.exit(42)"));
    }
}
