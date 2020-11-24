package org.atpfivt.jsyntrax.units.tracks.stack;

import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.visitors.Visitor;

import java.util.ArrayList;

public class Rightstack extends Stack {
  public Rightstack(ArrayList<Unit> units) {
    super(units);
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
