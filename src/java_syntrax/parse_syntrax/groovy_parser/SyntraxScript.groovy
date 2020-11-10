package java_syntrax.parse_syntrax.groovy_parser

import java_syntrax.parse_syntrax.units.Unit
import java_syntrax.parse_syntrax.units.tracks.*
import java_syntrax.parse_syntrax.units.tracks.loop.*
import java_syntrax.parse_syntrax.units.tracks.opt.*
import java_syntrax.parse_syntrax.units.tracks.stack.*

abstract class SyntraxScript extends Script {
    static getNone() {
        return null
    }

    static Line line(Unit... units) {
        return new Line(units as ArrayList<Unit>)
    }

    static Loop loop(Unit... units) {
        return new Loop(units as ArrayList<Unit>)
    }

    static Toploop toploop(Unit... units) {
        return new Toploop(units as ArrayList<Unit>)
    }

    static Choice choice(Unit... units) {
        return new Choice(units as ArrayList<Unit>)
    }

    static Opt opt(Unit... units) {
        return new Opt(units as ArrayList<Unit>)
    }

    static Optx optx(Unit... units) {
        return new Optx(units as ArrayList<Unit>)
    }

    static Stack stack(Unit... units) {
        return new Stack(units as ArrayList<Unit>)
    }

    static Indentstack indentstack(int indent, Unit... units) {
        return new Indentstack(indent, units as ArrayList<Unit>)
    }

    static Rightstack rightstack(Unit... units) {
        return new Rightstack(units as ArrayList<Unit>)
    }
}
