package java_syntrax.parse_syntrax.units.nodes;

import java_syntrax.parse_syntrax.styles.Style;
import java_syntrax.parse_syntrax.units.Unit;

public class Node implements Unit {
  Style style;
  String text;

  public Node(String text) {
    this.text = text;
  }

  public Node(Style style, String text) {
    this.style = style;
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
