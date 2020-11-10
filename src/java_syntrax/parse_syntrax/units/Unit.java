package java_syntrax.parse_syntrax.units;

import java.util.ArrayList;

public interface Unit {
  default ArrayList<Unit> getUnits() {
    return new ArrayList<>();
  }
}
