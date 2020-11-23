package com.syntrax.units.tracks.opt;

import com.syntrax.units.Unit;
import com.syntrax.units.tracks.ComplexTrack;

import java.util.ArrayList;
import java.util.stream.Collectors;

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

  @Override
  public String toString() {
    return "< " + this.getClass().getSimpleName() + ", stacked = " + stacked +
        " [ " +
        units.stream().map(Object::toString)
            .collect(Collectors.joining("\n"))
        + " ]" + " >";
  }
}
