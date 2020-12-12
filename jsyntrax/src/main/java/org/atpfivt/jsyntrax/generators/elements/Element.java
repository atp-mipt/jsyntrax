package org.atpfivt.jsyntrax.generators.elements;

import org.atpfivt.jsyntrax.styles.Style;
import org.atpfivt.jsyntrax.util.Pair;

import java.util.HashSet;
import java.util.Optional;

public class Element {
    public Element(String tag) {
        addTag(tag);
    }

    public Pair<Integer, Integer> getStart() { return this.start; }
    public void setStart(Pair<Integer, Integer> start) {
        this.start = start;
    }

    public Pair<Integer, Integer> getEnd() {return this.end;}
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
        if (tag.equals("all")) {
            return true;
        }
        return tags.contains(tag);
    }

    public void addShadow(StringBuilder sb, Style style) {}

    public void toSVG(StringBuilder sb, Style style) {}

    public void scale(double scale) {
        start.f = (int)(start.f * scale);
        start.s = (int)(start.s * scale);
        end.f = (int)(end.f * scale);
        end.s = (int)(end.s * scale);
    }

    public Optional<String> getAnyTag() {
        return tags.stream().findAny();
    }

    public Pair<Integer, Integer> start = null;
    public Pair<Integer, Integer> end = null;
    HashSet<String> tags = new HashSet<>();
}
