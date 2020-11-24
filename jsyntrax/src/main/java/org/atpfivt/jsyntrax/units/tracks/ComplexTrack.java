package org.atpfivt.jsyntrax.units.tracks;

import org.atpfivt.jsyntrax.units.Unit;

import java.util.ArrayList;

public abstract class ComplexTrack extends Track {
  public ComplexTrack(ArrayList<Unit> units) {
    super(units);

    for (int i = 0; i < units.size(); i++) {
      if (units.get(i) != null && !(units.get(i) instanceof Track)) {
        units.set(i, new Line(units.get(i)));
      }
    }
  }
}
