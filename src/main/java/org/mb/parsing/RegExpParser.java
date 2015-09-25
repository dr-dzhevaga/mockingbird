package org.mb.parsing;

import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Dmitriy Dzhevaga on 15.09.2015.
 */
public class RegExpParser extends AbstractParser {
    private static final Logger Log = Logger.getLogger(RegExpParser.class);

    public RegExpParser(String text) {
        super(text);
    }

    @Override
    public String parse(String path) throws ParsingException {
        Pattern p;
        try {
            p = Pattern.compile(path);
        } catch (PatternSyntaxException e) {
            Log.error(e);
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
