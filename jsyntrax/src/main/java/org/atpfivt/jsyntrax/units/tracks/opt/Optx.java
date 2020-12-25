package org.atpfivt.jsyntrax.units.tracks.opt;

import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.visitors.Visitor;

import java.util.List;

public class Optx extends Opt {
  public Optx(List<Unit> units) {
    super(units);
  }

  public void accept(Visitor visitor) {
    visitor.visitOptx(this);
  }
}
