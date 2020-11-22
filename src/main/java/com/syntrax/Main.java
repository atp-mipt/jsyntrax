package com.syntrax;

import com.syntrax.generators.Canvas;
import com.syntrax.generators.SVGCanvasBuilder;
import com.syntrax.groovy_parser.Parser;
import com.syntrax.units.tracks.Track;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        String program_path = "syntrax_scripts";
        File file = new File(program_path, "test1.groovy");
        Track track = Parser.parse(file);
        System.out.println(track);

        Canvas c = new SVGCanvasBuilder().generateSVG(track);
        String result = c.generateSVG();

        // write result to file
        try {
            OutputStream os = new FileOutputStream(new File("syntrax_scripts", "output.svg"));
            os.write(result.getBytes());
            os.close();
            System.out.println("Done!");
        }
        catch (IOException e) {
            System.out.println("Problems...");
            System.out.println(e.getMessage());
        }
    }
}
