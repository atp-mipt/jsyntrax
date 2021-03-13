package org.atpfivt.jsyntrax.units.nodes;

import org.atpfivt.jsyntrax.styles.StyleConfig;
import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.units.tracks.Line;
import org.atpfivt.jsyntrax.units.tracks.Track;
import org.atpfivt.jsyntrax.visitors.Visitor;

public class Node implements Unit {
  private StyleConfig style;
  private final String text;
  private boolean isLink = false;

  public Node(String text) {
    this.text = text;
  }

//  public Node(StyleConfig style, String text) {
//    this.style = style;
//    this.text = text;
//    this.is_link = false;  // FIXME: replace with slash checker
//  }

  public boolean isLink() {
    return isLink;
  }

  public void setLink() {
    isLink = true;
  }

  public void unsetLink() {
    isLink = false;
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
