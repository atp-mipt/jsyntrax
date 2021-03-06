package org.atpfivt.jsyntrax.units;

import org.atpfivt.jsyntrax.Configuration;
import org.atpfivt.jsyntrax.units.tracks.Line;
import org.atpfivt.jsyntrax.units.tracks.Track;
import org.atpfivt.jsyntrax.visitors.Visitor;

import java.util.Collections;
import java.util.List;

public interface Unit {
    void accept(Visitor visitor);

    default List<Unit> getUnits() {
      return Collections.emptyList();
    }

    default Configuration getConfiguration() {
        return new Configuration(this.getTrack());
    }

    default Track getTrack() {
      return new Line(this);
    }
}
