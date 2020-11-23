package com.syntrax;

import com.syntrax.generators.SVGCanvas;
import com.syntrax.generators.SVGCanvasBuilder;
import com.syntrax.groovy_parser.Parser;
import com.syntrax.units.Unit;
import com.syntrax.units.nodes.Bullet;
import com.syntrax.units.tracks.Line;
import com.syntrax.units.tracks.Track;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String program_path = "syntrax_scripts";
        File file = new File(program_path, "test1.groovy");
        Track track = Parser.parse(file);
        System.out.println(track);

        // TODO: not in Main
        Line l = new Line(new ArrayList<Unit>(){{
            add(new Bullet());
            add(track);
            add(new Bullet());
        }});

        SVGCanvas c = new SVGCanvasBuilder().generateSVG(l);
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
