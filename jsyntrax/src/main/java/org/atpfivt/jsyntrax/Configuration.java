package org.atpfivt.jsyntrax;

import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.visitors.Visitor;

import java.util.HashMap;
import java.util.Map;

public class Configuration implements Unit {
    private final Unit track;
    private final Map<String, String> urlMap;

    public Configuration(Unit track, Map<String, String> urlMap) {
        this.track = track;
        this.urlMap = urlMap;
    }

    public Configuration(Unit track) {
        this.track = track;
        this.urlMap = new HashMap<>();
    }

    public Unit getTrack() {
        return track;
    }

    public Map<String, String> getUrlMap() {
        return urlMap;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitConfiguration(this);
    }

    @Override
    public Configuration getConfiguration() {
        return this;
    }
}
