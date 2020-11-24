package org.atpfivt.jsyntrax.util;

public class Pair<F, S> {
    public Pair(F f, S s) {
        this.f = f;
        this.s = s;
    }

    public Pair(Pair<F, S> o) {
        this.f = o.f;
        this.s = o.s;
    }

    public F f;
    public S s;
}
