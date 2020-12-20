package org.atpfivt.jsyntrax.styles;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NodeStyle {

    public boolean match(String txt) {
        if (pattern == null) {
            return true;
        }
        return pattern.matcher(txt).find();
    }

    public void setCapture_group(String capture_group) {
        this.capture_group = capture_group;
    }

    public String extractCaptureGroupText(String txt){
        if (capture_group == null){
            return txt;
        }
        String patternString = pattern.toString();
        if(!patternString.contains("?<" + capture_group + ">") || !match(txt))
            throw new IllegalStateException("Entered named group does not match with entered pattern!");
        Matcher matcher = pattern.matcher(txt);
        if(matcher.find())
            return matcher.group(capture_group);
        else
            throw new IllegalStateException("Entered named group does not match with entered pattern!");
    }

    public String modify(String txt) { return txt; }

    public String name = "unknown";
    public String shape = "bubble";
    public Font font = new Font("Sans",Font.BOLD, 14);
    public Color text_color = new Color(0,0,0);
    public Color fill = new Color(144,164,174);
    public Pattern pattern = null;
    public String capture_group = null;
}
