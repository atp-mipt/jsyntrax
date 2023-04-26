package org.atpfivt.jsyntrax;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class InputArgumentsTest {
    @Test
    void testHelp() throws ParseException {
        InputArguments inputArguments = new InputArguments("--help");
        assertTrue(inputArguments.isHelp());
    }

    @Test
    void testInputOutput() throws ParseException {
        InputArguments inputArguments = new InputArguments("-i", "foo", "-o", "bar");
        assertAll(
                () -> assertEquals(Paths.get("foo"),
                        inputArguments.getInput()),
                () -> assertEquals(Paths.get("bar"),
                        inputArguments.getOutput()),
                () -> assertFalse(inputArguments.isTransparent()));
    }

    @Test
    void testTitleParam() throws ParseException {
        InputArguments inputArguments = new InputArguments("-i", "foo", "-o", "bar", "--title", "test title");
        assertEquals("test title", inputArguments.getTitle());
    }

    @Test
    void testScaleParam() throws ParseException {
        InputArguments inputArguments = new InputArguments("-i", "foo", "-o", "bar", "--scale", "2");
        assertEquals(2.0, inputArguments.getScale(), 0.00001);
    }

    @Test
    void testStyleParam() throws ParseException {
        InputArguments inputArguments = new InputArguments("-i", "foo", "-o", "bar", "-s", "baz.ini");
        assertEquals(Paths.get("baz.ini"), inputArguments.getStyle());
    }

    @Test
    void testTransparentParam() throws ParseException {
        InputArguments inputArguments = new InputArguments("-t");
        assertTrue(inputArguments.isTransparent());
    }

    @Test
    void testDefaultInputParam() throws ParseException {
        InputArguments inputArguments = new InputArguments("-o", "test.svg", "test.spec");
        assertEquals(Paths.get("test.spec"), inputArguments.getInput());

    }

    @Test
    void changeExtension() {
        assertEquals("foo.baz", InputArguments.changeExtension(Paths.get("foo.bar"), "baz").toString());
    }

    @Test
    void changeExtensionNoName() {
        assertEquals(".baz", InputArguments.changeExtension(Paths.get(".bar"), "baz").toString());
    }

    @Test
    void addExtension() {
        assertEquals("foo.baz", InputArguments.changeExtension(Paths.get("foo"), "baz").toString());
    }
}
