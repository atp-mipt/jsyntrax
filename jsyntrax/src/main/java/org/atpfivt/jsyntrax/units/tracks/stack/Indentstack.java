package org.atpfivt.jsyntrax.units.tracks.stack;

import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.visitors.Visitor;

import java.util.List;
import java.util.stream.Collectors;

public final class Indentstack extends Stack {
  private final int indent;

  public int getIndent() {
    return indent;
  }

  public Indentstack(int indent, List<Unit> units) {
    super(units);
    this.indent = indent;
  }

  @Override
  public String toString() {
    return "< " + this.getClass().getSimpleName()
            + ", indent = " + indent + " [ "
            + getUnits().stream().map(Object::toString)
            .collect(Collectors.joining("\n"))
            + " ]" + " >";
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visitIndentstack(this);
  }
}
