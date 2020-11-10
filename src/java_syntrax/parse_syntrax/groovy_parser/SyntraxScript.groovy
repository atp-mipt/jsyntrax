package java_syntrax.parse_syntrax.groovy_parser

import java_syntrax.parse_syntrax.units.Unit
import java_syntrax.parse_syntrax.units.nodes.*
import java_syntrax.parse_syntrax.units.tracks.*
import java_syntrax.parse_syntrax.units.tracks.loop.*
import java_syntrax.parse_syntrax.units.tracks.opt.*
import java_syntrax.parse_syntrax.units.tracks.stack.*

class SyntraxScript extends Script {
  static getNone() {
    return null
  }

  static ArrayList<Unit> unitsToString(Object... units) {
    for (i in 0..<units.length) {
      if (!(units[i] instanceof Track)) {
        units[i] = new Node(units[i].toString())
      }
    }

    return units as ArrayList<Unit>
  }

  static Line line(Object... units) {
    return new Line(unitsToString(units) as ArrayList<Unit>)
  }

  static Loop loop(Object... units) {
    return new Loop(unitsToString(units) as ArrayList<Unit>)
  }

  static Toploop toploop(Object... units) {
    return new Toploop(unitsToString(units) as ArrayList<Unit>)
  }

  static Choice choice(Object... units) {
    return new Choice(unitsToString(units) as ArrayList<Unit>)
  }

  static Opt opt(Object... units) {
    return new Opt([line(units)] as ArrayList<Unit>)
  }

  static Optx optx(Object... units) {
    return new Optx([line(units)] as ArrayList<Unit>)
  }

  static Stack stack(Object... units) {
    return new Stack(unitsToString(units) as ArrayList<Unit>)
  }

  static Indentstack indentstack(int indent, Object... units) {
    return new Indentstack(indent, unitsToString(units) as ArrayList<Unit>)
  }

  static Rightstack rightstack(Object... units) {
    return new Rightstack(unitsToString(units) as ArrayList<Unit>)
  }

  @Override
  Object run() {
    return null
  }
}
