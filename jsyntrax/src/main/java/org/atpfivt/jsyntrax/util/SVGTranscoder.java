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
        var transcoder = new PNGTranscoder();
        var svgStream = new ByteArrayInputStream(svgString.getBytes());
        var input = new TranscoderInput(svgStream);

        ByteArrayOutputStream ostream = new ByteArrayOutputStream(INIT_STREAM_SIZE);
        TranscoderOutput output = new TranscoderOutput(ostream);

        transcoder.transcode(input, output);
        return ostream.toByteArray();
    }
}
