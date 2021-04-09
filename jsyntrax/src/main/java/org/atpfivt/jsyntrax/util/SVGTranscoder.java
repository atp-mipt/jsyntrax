package org.atpfivt.jsyntrax.util;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public final class SVGTranscoder {
    private static final int INIT_STREAM_SIZE = 1024;

    private SVGTranscoder() {

    }

    public static byte[] svg2Png(String svgString) throws TranscoderException {
        PNGTranscoder transcoder = new PNGTranscoder();
        ByteArrayInputStream svgStream = new ByteArrayInputStream(svgString.getBytes());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(INIT_STREAM_SIZE);
        transcoder.transcode(new TranscoderInput(svgStream),
                new TranscoderOutput(outStream));
        return outStream.toByteArray();
    }
}
