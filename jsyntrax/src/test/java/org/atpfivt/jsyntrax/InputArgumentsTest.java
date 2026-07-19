package org.atpfivt.jsyntrax;

import org.apache.commons.cli.ParseException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class InputArgumentsTest {
    @Test
    void testHelp() throws ParseException {
        InputArguments inputArguments = new InputArguments("--help");
        assertThat(inputArguments.isHelp()).isTrue();
    }

    @Test
    void testInputOutput() throws ParseException {
        InputArguments inputArguments = new InputArguments("-i", "foo", "-o", "bar");
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(inputArguments.getInput())
                    .isEqualTo(Paths.get("foo"));
            softly.assertThat(inputArguments.getOutput())
                    .isEqualTo(Paths.get("bar"));
            softly.assertThat(inputArguments.isTransparent()).isFalse();
        });
    }

    @Test
    void testTitleParam() throws ParseException {
        InputArguments inputArguments = new InputArguments("-i", "foo", "-o", "bar", "--title", "test title");
        assertThat(inputArguments.getTitle()).isEqualTo("test title");
    }

    @Test
    void testScaleParam() throws ParseException {
        InputArguments inputArguments = new InputArguments("-i", "foo", "-o", "bar", "--scale", "2");
        assertThat(inputArguments.getScale()).isCloseTo(2.0, within(0.00001));
    }

    @Test
    void testStyleParam() throws ParseException {
        InputArguments inputArguments = new InputArguments("-i", "foo", "-o", "bar", "-s", "baz.ini");
        assertThat(inputArguments.getStyle()).isEqualTo(Paths.get("baz.ini"));
    }

    @Test
    void testTransparentParam() throws ParseException {
        InputArguments inputArguments = new InputArguments("-t");
        assertThat(inputArguments.isTransparent()).isTrue();
    }

    @Test
    void testDefaultInputParam() throws ParseException {
        InputArguments inputArguments = new InputArguments("-o", "test.svg", "test.spec");
        assertThat(inputArguments.getInput()).isEqualTo(Paths.get("test.spec"));

    }

    @Test
    void changeExtension() {
        assertThat(InputArguments.changeExtension(Path.of("foo.bar"), "baz")).hasToString("foo.baz");
    }

    @Test
    void changeExtensionNoName() {
        assertThat(InputArguments.changeExtension(Path.of(".bar"), "baz")).hasToString(".baz");
    }

    @Test
    void addExtension() {
        assertThat(InputArguments.changeExtension(Path.of("foo"), "baz")).hasToString("foo.baz");
    }
}
