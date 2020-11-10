package java_syntrax.parse_syntrax.units.tracks.opt;

import java_syntrax.parse_syntrax.units.Unit;
import java_syntrax.parse_syntrax.units.tracks.ComplexTrack;

import java.util.ArrayList;

public class Opt extends ComplexTrack {
  boolean stacked = false;

  public Opt(ArrayList<Unit> units) {
    super(units);
  }

  public void setStacked() {
    stacked = true;
  }

  public boolean isStacked() {
    return stacked;
  }

  public Opt toOpt() {
    return this;
  }
}
