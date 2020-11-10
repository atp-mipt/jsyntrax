package java_syntrax.parse_syntrax.units.tracks.loop;

import java_syntrax.parse_syntrax.exceptions.LoopNotTwoArgsException;
import java_syntrax.parse_syntrax.units.Unit;

import java.util.ArrayList;

public class Toploop extends Loop {
  public Toploop(ArrayList<Unit> units) throws LoopNotTwoArgsException {
    super(units);
  }
}
