package org.atpfivt.jsyntrax.units.tracks.loop;

import org.atpfivt.jsyntrax.exceptions.LoopNotTwoArgsException;
import org.atpfivt.jsyntrax.units.Unit;

import java.util.ArrayList;

public class Toploop extends Loop {
  public Toploop(ArrayList<Unit> units) throws LoopNotTwoArgsException {
    super(units);
  }
}
