package org.atpfivt.jsyntrax.units;

import java.util.ArrayList;

public interface Unit {
  default ArrayList<Unit> getUnits() {
    return new ArrayList<>();
  }
}