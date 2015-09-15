package org.mb.parsing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Dmitriy Dzhevaga on 15.09.2015.
 */
public class RegExpParser extends AbstractParser {
    public RegExpParser(String text) {
        super(text);
    }

    @Override
    public String parse(String path) throws ParsingException {
        Pattern p;
        try {
            p = Pattern.compile(path);
        } catch (PatternSyntaxException e) {
            throw  new ParsingException(e);
        }
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(m.groupCount() > 0 ? 1 : 0);
        } else {
            return "";
        }
    }
}
