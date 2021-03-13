package org.atpfivt.jsyntrax.units.tracks;

import org.atpfivt.jsyntrax.units.Unit;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Track implements Unit {
  private final List<Unit> units;

  public Track(List<Unit> units) {
    this.units = units;
  }

  @Override
  public List<Unit> getUnits() {
    return units;
  }

  @Override
  public String toString() {
    return "< " + this.getClass().getSimpleName()
            + "[ " + units.stream().map(Object::toString)
            .collect(Collectors.joining("\n"))
            + " ]" + " >";
  }

  @Override
  public Track getTrack() {
    return this;
  }
}
