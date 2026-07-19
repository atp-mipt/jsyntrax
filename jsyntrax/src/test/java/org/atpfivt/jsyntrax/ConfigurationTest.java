package org.atpfivt.jsyntrax;

import org.atpfivt.jsyntrax.parser.Parser;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationTest {
    @Test
    void urlMapIsParsed(){
        Configuration configuration = new Parser("jsyntrax(line('attribute'), ['a':'b'])").getNode();
        Map<String, String> urlMap = configuration.getUrlMap();
        assertThat(urlMap).isEqualTo(Map.of("a", "b"));
    }

    @Test
    void noUrlMap(){
        Configuration configuration = new Parser("jsyntrax(line('attribute'))").getNode();
        Map<String, String> urlMap = configuration.getUrlMap();
        assertThat(urlMap).isEmpty();
    }

    @Test
    void noSuperFunction(){
        Configuration configuration = new Parser("line('attribute')").getNode();
        Map<String, String> urlMap = configuration.getUrlMap();
        assertThat(urlMap).isEmpty();
    }
}
