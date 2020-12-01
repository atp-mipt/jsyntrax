package org.atpfivt.jsyntrax.units.nodes;

import org.atpfivt.jsyntrax.styles.Style;
import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.visitors.Visitor;

public class Node implements Unit {
  Style style;
  String text;
  boolean is_link = false;

  public Node(String text) {
    this.text = text;
  }

  public Node(Style style, String text) {
    this.style = style;
    this.text = text;
    this.is_link = false;  // FIXME: replace with slash checker
  }

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
    visitor.visit(this);
  }
}
