package com.syntrax.visitors;

import com.syntrax.generators.SVGCanvasBuilder;
import com.syntrax.units.Unit;
import com.syntrax.units.tracks.*;
import com.syntrax.units.tracks.loop.*;
import com.syntrax.units.tracks.opt.*;
import com.syntrax.units.tracks.stack.*;
import com.syntrax.units.nodes.*;

public abstract class Visitor {
  public abstract void visit(Line unit);
  public abstract void visit(Loop unit);
  public abstract void visit(Toploop unit);
  public abstract void visit(Choice unit);
  public abstract void visit(Opt unit);
  public abstract void visit(Optx unit);
  public abstract void visit(Stack unit);
  public abstract void visit(Rightstack unit);
  public abstract void visit(Indentstack unit);

  public abstract void visit(Bullet unit);
  public abstract void visit(Node unit);
  public abstract void visit(NoneNode unit);

  public Object getTosValue() {
    return tos_value_;
  }

  public void setTosValue(Object tos_value) {
    tos_value_ = tos_value;
  }

  Object tos_value_;

  public void visit(Unit unit) { assert false; }
}
