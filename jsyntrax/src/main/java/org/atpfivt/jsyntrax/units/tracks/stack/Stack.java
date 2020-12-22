package org.atpfivt.jsyntrax.units.tracks.stack;

import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.units.tracks.ComplexTrack;
import org.atpfivt.jsyntrax.visitors.Visitor;

import java.util.List;

public class Stack extends ComplexTrack {
  public Stack(List<Unit> units) {
    super(units);
  }

  public void accept(Visitor visitor) {
    visitor.visitStack(this);
  }
}
