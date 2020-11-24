package com.syntrax.units.tracks;

import com.syntrax.units.Unit;
import com.syntrax.visitors.Visitor;

import java.util.ArrayList;

public class Choice extends ComplexTrack {
  public Choice(ArrayList<Unit> units) {
    super(units);
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
