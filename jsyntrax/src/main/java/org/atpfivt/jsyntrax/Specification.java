package org.atpfivt.jsyntrax;

import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.units.nodes.Bullet;
import org.atpfivt.jsyntrax.units.tracks.Line;
import org.atpfivt.jsyntrax.units.tracks.Track;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Specification {
  Unit root;
  HashMap<String, URL> url_map;

  public Specification(Track track) {
    this(track, new HashMap<>());
  }

  public Specification(Track track, HashMap<String, URL> url_map) {
    this.root = new Line(new ArrayList<Unit>(){{
      add(new Bullet());
      add(track);
      add(new Bullet());
    }});
    this.url_map = url_map;
  }

  public Unit getRoot() {
    return root;
  }

  public URL getUrl(String s) {
    // TODO: remove
    try {
      if (s.equals("entity_class")) {
        return new URL("https://www.google.com/#q=vhdl+entity+class");
      }
      if (s.equals("(attribute) identifier")) {
        return new URL("http://en.wikipedia.com/wiki/VHDL");
      }
    } catch (MalformedURLException e) {
      return null;
    }

    return url_map.get(s);
  }
}
