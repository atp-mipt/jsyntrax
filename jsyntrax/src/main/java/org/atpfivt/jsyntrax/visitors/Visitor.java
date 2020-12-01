package org.atpfivt.jsyntrax.visitors;

import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.units.nodes.Bullet;
import org.atpfivt.jsyntrax.units.nodes.Node;
import org.atpfivt.jsyntrax.units.nodes.NoneNode;
import org.atpfivt.jsyntrax.units.tracks.Choice;
import org.atpfivt.jsyntrax.units.tracks.Line;
import org.atpfivt.jsyntrax.units.tracks.loop.Loop;
import org.atpfivt.jsyntrax.units.tracks.loop.Toploop;
import org.atpfivt.jsyntrax.units.tracks.opt.Opt;
import org.atpfivt.jsyntrax.units.tracks.opt.Optx;
import org.atpfivt.jsyntrax.units.tracks.stack.Indentstack;
import org.atpfivt.jsyntrax.units.tracks.stack.Rightstack;
import org.atpfivt.jsyntrax.units.tracks.stack.Stack;

public interface Visitor {
  void visit(Line unit);
  void visit(Loop unit);
  void visit(Toploop unit);
  void visit(Choice unit);
  void visit(Opt unit);
  void visit(Optx unit);
  void visit(Stack unit);
  void visit(Rightstack unit);
  void visit(Indentstack unit);
  void visit(Bullet unit);
  void visit(Node unit);
  void visit(NoneNode unit);

  default void visit(Unit unit) { assert false; }
}
