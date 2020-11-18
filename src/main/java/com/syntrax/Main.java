package com.syntrax;

import com.syntrax.groovy_parser.Parser;
import com.syntrax.units.tracks.Track;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String program_path = "syntrax_scripts";
        File file = new File(program_path, "test1.groovy");
        Track track = Parser.parse(file);
        System.out.println(track);
    }
}
