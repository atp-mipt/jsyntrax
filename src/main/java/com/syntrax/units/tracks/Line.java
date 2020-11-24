package com.syntrax.units.tracks;

import com.syntrax.units.Unit;
import com.syntrax.visitors.Visitor;

import java.util.ArrayList;

public class Line extends Track {
  public Line(ArrayList<Unit> units) {
    super(units);
  }

  public Line(Unit unit) {
    super(new ArrayList<>());
    units.add(unit);
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
