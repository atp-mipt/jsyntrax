package java_syntrax.parse_syntrax.units.tracks;

import java_syntrax.parse_syntrax.units.Unit;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Track implements Unit {
  protected final ArrayList<Unit> units;

  public Track(ArrayList<Unit> units) {
    this.units = units;
  }

  @Override
  public ArrayList<Unit> getUnits() {
    return units;
  }

  @Override
  public String toString() {
    return "< " + this.getClass().getSimpleName() +
        "[ " +
        units.stream().map(Object::toString)
            .collect(Collectors.joining("\n"))
        + " ]" + " >";
  }
}
