package org.atpfivt.jsyntrax.units.tracks.stack;

import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.visitors.Visitor;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Indentstack extends Stack {
  public final int indent;

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

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
