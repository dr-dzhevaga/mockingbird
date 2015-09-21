package org.mb.scripting;

/**
 * Created by Dmitriy Dzhevaga on 21.09.2015.
 */
public interface Syntax {
    char[] openPrint();
    char[] closePrint();
    char[] openLiteral();
    char[] closeLiteral();
    char[] escapeLiteral(char ch);
    char[] openScript();
    char[] closeScript();
}