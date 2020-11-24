package org.atpfivt.jsyntrax.units.tracks.stack;

import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.units.tracks.ComplexTrack;
import org.atpfivt.jsyntrax.units.tracks.opt.Opt;

import java.util.ArrayList;

public class Stack extends ComplexTrack {
  public Stack(ArrayList<Unit> units) {
    super(units);

    for (int i = 0; i < units.size(); i++) {
      if (units.get(i) instanceof Opt) {
        Opt unit = (Opt) units.get(i);
        units.set(i, unit.toOpt());
        unit.setStacked();
      }
    }
  }
}
