package org.atpfivt.jsyntrax;

import org.atpfivt.jsyntrax.units.Unit;

import java.util.HashMap;

public class Configuration {
  Unit track;
  HashMap<String, String>  urlMap;

  public Configuration(Unit track, HashMap<String, String> urlMap) {
    this.track = track;
    this.urlMap = urlMap;
  }

  public Configuration(Unit track) {
    this.track = track;
    this.urlMap = new HashMap<>();
  }

  public Unit getTrack() {
    return track;
  }

  public void setTrack(Unit track) {
    this.track = track;
  }

  public HashMap<String, String> getUrlMap() {
    return urlMap;
  }

  public void setUrlMap(HashMap<String, String> urlMap) {
    this.urlMap = urlMap;
  }
}
