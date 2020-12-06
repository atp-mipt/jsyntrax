package org.atpfivt.jsyntrax.units;

import org.atpfivt.jsyntrax.visitors.Visitor;

import java.util.ArrayList;

public interface Unit {
  default ArrayList<Unit> getUnits() {
    return new ArrayList<>();
  }

  void accept(Visitor visitor);
}