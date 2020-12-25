package org.atpfivt.jsyntrax.units.tracks.loop;

import org.atpfivt.jsyntrax.exceptions.LoopNotTwoArgsException;
import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.visitors.Visitor;

import java.util.List;

public class Toploop extends Loop {
  public Toploop(List<Unit> units) throws LoopNotTwoArgsException {
    super(units);
  }

  public void accept(Visitor visitor) {
    visitor.visitToploop(this);
  }
}
