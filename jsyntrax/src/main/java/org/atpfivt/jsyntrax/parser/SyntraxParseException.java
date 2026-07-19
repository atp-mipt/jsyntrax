package org.atpfivt.jsyntrax.parser;

/**
 * Thrown when a diagram specification cannot be parsed. This is an unchecked
 * exception so that callers may treat a malformed script uniformly, the same
 * way the former Groovy parser surfaced {@code CompilationFailedException}.
 */
public class SyntraxParseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SyntraxParseException(String message) {
        super(message);
    }

    public SyntraxParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
