package org.atpfivt.jsyntrax;

import org.atpfivt.jsyntrax.groovy_parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

class ConfigurationTest {
    @Test
    void urlMapIsParsed(){
        Configuration configuration = Parser.getNode("jsyntrax(line('attribute'), ['a':'b'])");
        Map<String, String> urlMap = configuration.getUrlMap();
        Assertions.assertEquals(Map.of("a", "b"), urlMap);
    }

    @Test
    void noUrlMap(){
        Configuration configuration = Parser.getNode("jsyntrax(line('attribute'))");
        Map<String, String> urlMap = configuration.getUrlMap();
        Assertions.assertEquals(Collections.emptyMap(), urlMap);
    }

    @Test
    void noSuperFunction(){
        Configuration configuration = Parser.getNode("line('attribute')");
        Map<String, String> urlMap = configuration.getUrlMap();
        Assertions.assertEquals(Collections.emptyMap(), urlMap);
    }
}