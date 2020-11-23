package com.syntrax.units.tracks.stack;

import com.syntrax.units.Unit;
import com.syntrax.units.tracks.ComplexTrack;
import com.syntrax.units.tracks.opt.Opt;

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
