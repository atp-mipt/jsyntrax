package org.atpfivt.jsyntrax.nodes;

import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.visitors.Visitor;

public class Bullet implements Unit {
    public Bullet() {}

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
