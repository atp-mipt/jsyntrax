package java_syntrax.parse_syntrax.units.tracks.stack;

import java_syntrax.parse_syntrax.units.Unit;
import java_syntrax.parse_syntrax.units.tracks.Track;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Indentstack extends Stack {
  final int indent;

  public Indentstack(int indent, ArrayList<Unit> units) {
    super(units);
    this.indent = indent;
  }

  @Override
  public String toString() {
    return "< " + this.getClass().getSimpleName() + ", indent = " + indent +
        " [ " +
        units.stream().map(Object::toString)
            .collect(Collectors.joining("\n"))
        + " ]" + " >";
  }
}
