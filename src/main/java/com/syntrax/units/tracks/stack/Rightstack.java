package com.syntrax.units.tracks.stack;

import com.syntrax.units.Unit;
import com.syntrax.visitors.Visitor;

import java.util.ArrayList;

public class Rightstack extends Stack {
  public Rightstack(ArrayList<Unit> units) {
    super(units);
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
