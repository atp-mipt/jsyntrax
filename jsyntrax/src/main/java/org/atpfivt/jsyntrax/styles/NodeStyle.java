package org.atpfivt.jsyntrax.styles;

import java.awt.*;
import java.util.regex.Pattern;

public class NodeStyle {
    public boolean match(String txt) {
        if (pattern == null) {
            return true;
        }
        return pattern.matcher(txt).matches();
    }

    // gets plain text from regex groups, defined in pattern by user
    // returns text "as is" if no groups found or pattern is not defined
    public String unwrapTextContent(String txt) {
        if (pattern == null) {
            return txt;
        }

        var matcher = pattern.matcher(txt);
        StringBuilder unwrappedTxt = new StringBuilder();

        if (matcher.groupCount() == 0 || !matcher.find()) {
            return txt;
        }
        for (int i = 1; i <= matcher.groupCount(); i++) {
            unwrappedTxt.append(matcher.group(i));
        }
        return unwrappedTxt.toString();
    }

    public String name = "unknown";
    public String shape = "bubble";
    public Font font = new Font("Sans",Font.BOLD, 14);
    public Color text_color = new Color(0,0,0);
    public Color fill = new Color(144,164,174);
    public Pattern pattern = null;
}
