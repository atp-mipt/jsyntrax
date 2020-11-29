package org.atpfivt.jsyntrax;

import org.approvaltests.Approvals;
import org.atpfivt.jsyntrax.generators.SVGCanvas;
import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder;
import org.atpfivt.jsyntrax.groovy_parser.Parser;
import org.atpfivt.jsyntrax.units.Unit;
import org.atpfivt.jsyntrax.units.nodes.Bullet;
import org.atpfivt.jsyntrax.units.tracks.Line;
import org.atpfivt.jsyntrax.units.tracks.Track;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;


public class JSyntraxTest {
    @Test
    void stringIsNotAPrimitiveType() {
        File file = new File("/Users/ekaterina/Desktop/квантмех/jsyntrax/syntrax_scripts/test1.groovy");
        Track track = Parser.parse(file);
        Line l = new Line(new ArrayList<Unit>(){{
            add(new Bullet());
            add(track);
            add(new Bullet());
        }});

        SVGCanvas c = new SVGCanvasBuilder().generateSVG(l);
        String result = c.generateSVG();
        Approvals.verify(result);
    }
}