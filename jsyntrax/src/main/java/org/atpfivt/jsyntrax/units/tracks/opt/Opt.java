package org.atpfivt.jsyntrax.units.tracks.opt;

import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.units.tracks.ComplexTrack;
import org.atpfivt.jsyntrax.visitors.Visitor;

import java.util.List;
import java.util.stream.Collectors;

public class Opt extends ComplexTrack {
  public Opt(List<Unit> units) {
    super(units);
  }

  @Override
  public String toString() {
    return "< " + this.getClass().getSimpleName() +
        " [ " +
        units.stream().map(Object::toString)
            .collect(Collectors.joining("\n"))
        + " ]" + " >";
  }

  public void accept(Visitor visitor) {
    visitor.visitOpt(this);
  }
}
