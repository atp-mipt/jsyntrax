package com.syntrax.units.tracks.opt;

import com.syntrax.units.Unit;
import com.syntrax.visitors.Visitor;

import java.util.ArrayList;

public class Optx extends Opt {
  public Optx(ArrayList<Unit> units) {
    super(units);
  }

  @Override
  public Opt toOpt() {
    return new Opt(units);
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
