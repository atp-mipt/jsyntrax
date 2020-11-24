package com.syntrax.units.nodes;

import com.syntrax.styles.Style;
import com.syntrax.units.Unit;
import com.syntrax.visitors.Visitor;

import java.net.URL;

public class Node implements Unit {
  Style style;
  String text;
  URL link;

  public Node(String text) {
    this.text = text;
  }

  public Node(Style style, String text, URL link) {
    this.style = style;
    this.text = text;
    this.link = link;
  }

  public URL getLink() {
    return link;
  }

  @Override
  public String toString() {
    return text;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
