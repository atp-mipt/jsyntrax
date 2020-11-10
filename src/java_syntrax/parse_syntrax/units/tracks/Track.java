package java_syntrax.parse_syntrax.units.tracks;

import java_syntrax.parse_syntrax.units.Unit;

import java.util.ArrayList;

public class Track implements Unit {
  protected final ArrayList<Unit> units;

  public Track(ArrayList<Unit> units) {
    this.units = units;
  }

  @Override
  public ArrayList<Unit> getUnits() {
    return units;
  }
}
