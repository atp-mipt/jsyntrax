package java_syntrax.parse_syntrax;

import java_syntrax.parse_syntrax.groovy_parser.Parser;

import java.io.File;

public class Main {
  public static void main(String[] args) {
    var program_path = "syntrax_scripts";
    var file = new File(program_path, "test1.groovy");
    var track = Parser.parse(file);
    System.out.println(track);
  }
}