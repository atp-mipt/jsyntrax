package java_syntrax.parse_syntrax;

import java_syntrax.parse_syntrax.groovy_parser.Parser;

public class Main {
  public static void main(String[] args) {
    var program = "";
    var track = Parser.parse(program);
    System.out.println();
  }
}
