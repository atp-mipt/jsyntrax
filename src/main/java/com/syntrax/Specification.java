package com.syntrax;

import com.syntrax.units.tracks.Track;

import java.net.URL;
import java.util.Map;

public class Specification {
  Track track;
  Map<String, URL> url_map;

  public Specification(Track track, Map<String, URL> url_map) {
    this.track = track;
    this.url_map = url_map;
  }

  public Track getTrack() {
    return track;
  }

  public Map<String, URL> getUrl_map() {
    return url_map;
  }
}
