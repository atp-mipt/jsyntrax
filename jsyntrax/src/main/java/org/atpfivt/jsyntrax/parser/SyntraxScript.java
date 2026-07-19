package org.atpfivt.jsyntrax.parser;

import org.atpfivt.jsyntrax.Configuration;
import org.atpfivt.jsyntrax.exceptions.LoopNotTwoArgsException;
import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.units.nodes.Node;
import org.atpfivt.jsyntrax.units.nodes.NoneNode;
import org.atpfivt.jsyntrax.units.tracks.Choice;
import org.atpfivt.jsyntrax.units.tracks.Line;
import org.atpfivt.jsyntrax.units.tracks.Track;
import org.atpfivt.jsyntrax.units.tracks.loop.Loop;
import org.atpfivt.jsyntrax.units.tracks.loop.Toploop;
import org.atpfivt.jsyntrax.units.tracks.opt.Opt;
import org.atpfivt.jsyntrax.units.tracks.opt.Optx;
import org.atpfivt.jsyntrax.units.tracks.stack.Indentstack;
import org.atpfivt.jsyntrax.units.tracks.stack.Rightstack;
import org.atpfivt.jsyntrax.units.tracks.stack.Stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Builder for the JSyntrax specification language.
 *
 * <p>Each method mirrors a function of the diagram DSL and returns the unit it
 * creates, so nested calls compose exactly as they did in the former Groovy
 * script. The most recently created unit is remembered so that a trailing
 * {@code title(...)} call can return it as the script result.
 */
public class SyntraxScript {
    private Unit node;
    private String title;

    /** The diagram title set via {@code title(...)}, or {@code null}. */
    public String getTitle() {
        return title;
    }

    /**
     * Coerces raw arguments to units: {@code null} becomes an empty node, an
     * existing track is kept as-is, anything else becomes a text node.
     */
    static List<Unit> toUnits(Object... units) {
        List<Unit> result = new ArrayList<>(units.length);
        for (Object unit : units) {
            if (unit == null) {
                result.add(new NoneNode());
            } else if (unit instanceof Track) {
                result.add((Track) unit);
            } else {
                result.add(new Node(unit.toString()));
            }
        }
        return result;
    }

    public Unit title(String titleText) {
        this.title = titleText;
        return node;
    }

    public Line line(Object... units) {
        Line line = new Line(toUnits(units));
        node = line;
        return line;
    }

    public Loop loop(Object... units) {
        Loop loop;
        try {
            loop = new Loop(toUnits(units));
        } catch (LoopNotTwoArgsException e) {
            throw new SyntraxParseException("loop() expects exactly two arguments", e);
        }
        node = loop;
        return loop;
    }

    public Toploop toploop(Object... units) {
        Toploop toploop;
        try {
            toploop = new Toploop(toUnits(units));
        } catch (LoopNotTwoArgsException e) {
            throw new SyntraxParseException("toploop() expects exactly two arguments", e);
        }
        node = toploop;
        return toploop;
    }

    public Choice choice(Object... units) {
        Choice choice = new Choice(toUnits(units));
        node = choice;
        return choice;
    }

    public Opt opt(Object... units) {
        List<Unit> wrapped = new ArrayList<>(1);
        wrapped.add(line(units));
        Opt opt = new Opt(wrapped);
        node = opt;
        return opt;
    }

    public Optx optx(Object... units) {
        List<Unit> wrapped = new ArrayList<>(1);
        wrapped.add(line(units));
        Optx optx = new Optx(wrapped);
        node = optx;
        return optx;
    }

    public Stack stack(Object... units) {
        Stack stack = new Stack(toUnits(units));
        node = stack;
        return stack;
    }

    public Indentstack indentstack(int indent, Object... units) {
        Indentstack indentstack = new Indentstack(indent, toUnits(units));
        node = indentstack;
        return indentstack;
    }

    public Rightstack rightstack(Object... units) {
        Rightstack rightstack = new Rightstack(toUnits(units));
        node = rightstack;
        return rightstack;
    }

    public Configuration jsyntrax(Track track) {
        return new Configuration(track);
    }

    public Configuration jsyntrax(Track track, Map<String, String> urlMap) {
        return new Configuration(track, urlMap);
    }
}
