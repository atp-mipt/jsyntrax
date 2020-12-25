package org.atpfivt.jsyntrax.units.tracks;

import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.visitors.Visitor;

import java.util.List;

public class Choice extends ComplexTrack {
  public Choice(List<Unit> units) {
    super(units);
  }

  public void accept(Visitor visitor) {
    visitor.visitChoice(this);
  }
}
