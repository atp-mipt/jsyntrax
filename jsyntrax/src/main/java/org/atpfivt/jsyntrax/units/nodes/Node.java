package org.atpfivt.jsyntrax.units.nodes;

import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.units.tracks.Line;
import org.atpfivt.jsyntrax.units.tracks.Track;
import org.atpfivt.jsyntrax.visitors.Visitor;

public class Node implements Unit {
  private StyleConfig style;
  private final String text;
  boolean is_link = false;

  public Node(String text) {
    this.text = text;
  }

//  public Node(StyleConfig style, String text) {
//    this.style = style;
//    this.text = text;
//    this.is_link = false;  // FIXME: replace with slash checker
//  }

  public boolean isLink() {
    return is_link;
  }

  public void setLink() {
    is_link = true;
  }

  public void unsetLink() {
    is_link = false;
  }

  @Override
  public String toString() {
    return text;
  }

  public void accept(Visitor visitor) {
    visitor.visitNode(this);
  }

  public Track getTrack() {
    return new Line(this);
  }

  public StyleConfig getStyle() {
    return style;
  }

  public void setStyle(StyleConfig style) {
    this.style = style;
  }
}
