package com.syntrax.visitors;

import com.syntrax.generators.SVGCanvasBuilder;
import com.syntrax.units.Unit;
import com.syntrax.units.tracks.*;
import com.syntrax.units.tracks.loop.*;
import com.syntrax.units.tracks.opt.*;
import com.syntrax.units.tracks.stack.*;
import com.syntrax.units.nodes.*;

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
