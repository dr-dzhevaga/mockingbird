package org.mb.scripting;

import java.util.List;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 21.09.2015.
 */
public interface EngineRules {
    char[] openPrint();
    char[] closePrint();
    char[] openLiteral();
    char[] closeLiteral();
    char[] escapeLiteral(char ch);
    char[] openScript();
    char[] closeScript();
    Object toNativeObject(Map<String, ?> map);
    Object toNativeArray(List<String> list);
}