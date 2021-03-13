package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.util.Pair;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Element {
    public Element(String tag) {
        tags.add(tag);
    }

    public void setStart(Pair<Integer, Integer> start) {
        this.start = start;
    }

    public Pair<Integer, Integer> getStart() {
        return start;
    }

    public Pair<Integer, Integer> getEnd() {
        return this.end;
    }

    public void setEnd(Pair<Integer, Integer> end) {
        this.end = end;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void delTag(String tag) {
        tags.remove(tag);
    }

    public boolean isTagged(String tag) {
        if ("all".equals(tag)) {
            return true;
        }
        return tags.contains(tag);
    }

    public void addShadow(StringBuilder sb, StyleConfig style) { }

    public void toSVG(StringBuilder sb, StyleConfig style) { }

    public void scale(double scale) {
        getStart().f = (int) (getStart().f * scale);
        getStart().s = (int) (getStart().s * scale);
        getEnd().f = (int) (getEnd().f * scale);
        getEnd().s = (int) (getEnd().s * scale);
    }

    public Optional<String> getAnyTag() {
        return tags.stream().findAny();
    }

    private Pair<Integer, Integer> start = null;
    private Pair<Integer, Integer> end = null;
    private final Set<String> tags = new HashSet<>();
}
