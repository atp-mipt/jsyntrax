package org.atpfivt.jsyntrax.parser;

import org.atpfivt.jsyntrax.Configuration;
import org.atpfivt.jsyntrax.units.Unit;

import java.io.StringReader;

/**
 * Entry point for parsing a diagram specification. Wraps the generated
 * {@link SyntraxParser} and exposes the resulting configuration and title.
 */
public final class Parser {
    private final Configuration node;
    private final String title;

    public Parser(String scriptText) {
        Unit result;
        String parsedTitle;
        try {
            SyntraxParser parser = new SyntraxParser(new StringReader(scriptText));
            result = parser.Script();
            parsedTitle = parser.getScript().getTitle();
        } catch (ParseException | TokenMgrError | SyntraxParseException e) {
            throw new SyntraxParseException(e.getMessage(), e);
        }
        if (result == null) {
            throw new SyntraxParseException("Script does not define a diagram");
        }
        this.node = result.getConfiguration();
        this.title = parsedTitle;
    }

    public Configuration getNode() {
        return node;
    }

    public String getTitle() {
        return title;
    }
}
