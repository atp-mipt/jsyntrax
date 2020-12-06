package org.atpfivt.jsyntrax.units.tracks;

import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.visitors.Visitor;

import java.util.ArrayList;

public class Choice extends ComplexTrack {
  public Choice(ArrayList<Unit> units) {
    super(units);
  }

  public void accept(Visitor visitor) {
    visitor.visitChoice(this);
  }
}
