package com.syntrax.units.tracks.loop;

import com.syntrax.exceptions.LoopNotTwoArgsException;
import com.syntrax.units.Unit;

import java.util.ArrayList;

public class Toploop extends Loop {
  public Toploop(ArrayList<Unit> units) throws LoopNotTwoArgsException {
    super(units);
  }
}
