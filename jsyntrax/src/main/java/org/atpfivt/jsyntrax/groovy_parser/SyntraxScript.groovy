package org.atpfivt.jsyntrax.groovy_parser

import org.atpfivt.jsyntrax.units.Unit
import org.atpfivt.jsyntrax.units.nodes.Node
import org.atpfivt.jsyntrax.units.tracks.Choice
import org.atpfivt.jsyntrax.units.tracks.Line
import org.atpfivt.jsyntrax.units.tracks.Track
import org.atpfivt.jsyntrax.units.tracks.loop.Loop
import org.atpfivt.jsyntrax.units.tracks.loop.Toploop
import org.atpfivt.jsyntrax.units.tracks.opt.Opt
import org.atpfivt.jsyntrax.units.tracks.opt.Optx
import org.atpfivt.jsyntrax.units.tracks.stack.Indentstack
import org.atpfivt.jsyntrax.units.tracks.stack.Rightstack
import org.atpfivt.jsyntrax.units.tracks.stack.Stack

class SyntraxScript extends Script {
  static ArrayList<Unit> unitsToString(Object... units) {
    for (i in 0..<units.length) {
      if (units[i] == null) {
        units[i] = new NoneNode()
      } else if (!(units[i] instanceof Track)) {
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
