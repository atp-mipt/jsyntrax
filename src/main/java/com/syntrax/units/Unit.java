package com.syntrax.units;

import com.syntrax.util.Pair;
import com.syntrax.visitors.Visitor;

import java.util.ArrayList;
import java.util.HashSet;

public interface Unit {
  default ArrayList<Unit> getUnits() {
    return new ArrayList<>();
  }
}