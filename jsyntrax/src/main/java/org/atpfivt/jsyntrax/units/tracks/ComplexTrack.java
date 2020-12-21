package org.atpfivt.jsyntrax.units.tracks;

import org.atpfivt.jsyntrax.units.Unit;

import java.util.List;

public abstract class ComplexTrack extends Track {
  public ComplexTrack(List<Unit> units) {
    super(units);

    for (int i = 0; i < units.size(); i++) {
      if (!(units.get(i) instanceof Track)) {
        units.set(i, new Line(units.get(i)));
      }
    }
  }
}
