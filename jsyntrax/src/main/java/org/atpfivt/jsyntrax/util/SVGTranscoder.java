package org.atpfivt.jsyntrax.util;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public final class SVGTranscoder {
    private static final int INIT_STREAM_SIZE = 1024;

    private SVGTranscoder() {

    }


    private static Collection<String> getResourceFilesWithExt(String ext) throws IOException {
        File jarFile = new File(SVGTranscoder
                .class.getProtectionDomain()
                .getCodeSource().getLocation().getPath());

        JarFile jar = new JarFile(jarFile);
        Enumeration<JarEntry> entries = jar.entries();

        List<String> result = new ArrayList<>();
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.endsWith(ext)) {
                result.add(name);
            }
        }
        jar.close();
        return result;
    }


    private static InputStream getResourceAsStream(String resource) {
        return Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resource);
    }


    private static void setupPNGFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

            Collection<String> fontNames = getResourceFilesWithExt(".ttf");
            for (String i : fontNames) {
                InputStream fontStream = getResourceAsStream(i);
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontStream));
            }
        } catch (IOException | FontFormatException | NullPointerException e) {
            System.out.println("Warning: SVGTranscoder: " + e.getMessage());
        }
    }


    public static byte[] svg2Png(String svgString) throws TranscoderException {
        setupPNGFonts();
        PNGTranscoder transcoder = new PNGTranscoder();
        ByteArrayInputStream svgStream = new ByteArrayInputStream(svgString.getBytes());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(INIT_STREAM_SIZE);
        transcoder.transcode(new TranscoderInput(svgStream),
                new TranscoderOutput(outStream));
        return outStream.toByteArray();
    }
}
