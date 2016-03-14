package org.mb.settings.parsing;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.Reader;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public final class YAMLParser implements Parser {

    public YAMLParser() { }

    @Override
    public Object parse(Reader inputFile) throws ParsingException {
        YamlReader parser = new YamlReader(inputFile);
        try {
            return parser.read();
        } catch (YamlException e) {
            throw new ParsingException(e);
        }
    }
}