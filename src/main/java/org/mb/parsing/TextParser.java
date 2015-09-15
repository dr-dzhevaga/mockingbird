package org.mb.parsing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dmitriy Dzhevaga on 15.09.2015.
 */
public class TextParser extends AbstractParser {
    public TextParser(String text) {
        super(text);
    }

    @Override
    public String parse(String path) throws ParsingException {
        Pattern p = Pattern.compile(path);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(m.groupCount() > 0 ? 1 : 0);
        } else {
            return "";
        }
    }
}
