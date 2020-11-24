package org.atpfivt.jsyntrax;

import org.atpfivt.jsyntrax.units.tracks.Track;

import java.net.URL;
import java.util.HashMap;

public class Specification {
  Track track;
  HashMap<String, URL> url_map;

  public Specification(Track track) {
    this(track, new HashMap<String, URL>());
  }

  public Specification(Track track, HashMap<String, URL> url_map) {
    this.track = track;
    this.url_map = url_map;
  }

  public Track getTrack() {
    return track;
  }

  public HashMap<String, URL> getUrl_map() {
    return url_map;
  }
}
