package org.atpfivt.jsyntrax.visitors;

import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.units.tracks.*;
import org.atpfivt.jsyntrax.units.tracks.loop.*;
import org.atpfivt.jsyntrax.units.tracks.opt.*;
import org.atpfivt.jsyntrax.units.tracks.stack.*;
import org.atpfivt.jsyntrax.units.nodes.*;

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
