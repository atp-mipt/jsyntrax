package org.atpfivt.jsyntrax.units.tracks.opt;

import org.atpfivt.jsyntrax.units.Unit;

import java.util.ArrayList;

public class Optx extends Opt {
  public Optx(ArrayList<Unit> units) {
    super(units);
  }

  @Override
  public Opt toOpt() {
    return new Opt(units);
  }
}
