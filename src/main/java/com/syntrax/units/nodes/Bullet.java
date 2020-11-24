package com.syntrax.units.nodes;

import com.syntrax.units.Unit;
import com.syntrax.visitors.Visitor;

public class Bullet implements Unit {
    public Bullet() {}

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
