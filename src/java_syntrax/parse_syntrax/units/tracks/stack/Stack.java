package java_syntrax.parse_syntrax.units.tracks.stack;

import java_syntrax.parse_syntrax.units.Unit;
import java_syntrax.parse_syntrax.units.tracks.ComplexTrack;
import java_syntrax.parse_syntrax.units.tracks.opt.Opt;

import java.util.ArrayList;

public class Stack extends ComplexTrack {
  public Stack(ArrayList<Unit> units) {
    super(units);

    for (int i = 0; i < units.size(); i++) {
      if (units.get(i) instanceof Opt) {
        var unit = (Opt) units.get(i);
        units.set(i, unit.toOpt());
        unit.setStacked();
      }
    }
  }
}
