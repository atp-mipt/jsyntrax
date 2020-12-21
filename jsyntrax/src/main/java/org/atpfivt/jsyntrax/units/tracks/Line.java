package org.atpfivt.jsyntrax.units.tracks;

import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.visitors.Visitor;

import java.util.Collections;
import java.util.List;

public class Line extends Track {
  public Line(List<Unit> units) {
    super(units);
  }

  public Line(Unit unit) {
    super(Collections.singletonList(unit));
  }

  public void accept(Visitor visitor) {
    visitor.visitLine(this);
  }
}
