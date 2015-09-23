package org.mb.jspl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Dmitriy Dzhevaga on 23.09.2015.
 */
@RunWith(Parameterized.class)
public class JSPLikeProcessorTest {

    private final String source;
    private final String ER;

    public JSPLikeProcessorTest(String source, String ER) {
        this.source = source;
        this.ER = ER;
    }

    @Parameterized.Parameters
    public static Collection testData() {
        return Arrays.asList(new Object[][]{
                {"test", "test"},
                {"test\ntest", "test\ntest"},
                {"test\r\ntest", "test\r\ntest"},
                {"' '' \" \"\" \\ \\\\", "' '' \" \"\" \\ \\\\"},
                {"тест", "тест"},

                {"<%if(true)%>test", "test"},
                {"te<%if(true)%>st", "test"},
                {"test<%if(true){}%>", "test"},
                {"<%if(true){%>test<%}%>", "test"},

                {"<%='test'%>", "test"},
                {"te<%='st'%>", "test"},
                {"t<%='es'%>t", "test"},
                {"<%='te'%>st", "test"},
                {"<%='te'%><%='st'%>", "test"},

                {"<%s = 'tes';%><%=s%>t", "test"},
                {"tes<%s = 't';%><%=s%>", "test"},
                {"t<%s = 'es';%><%=s%>t", "test"},
                {"<%s='test'%><%for(i in s){%><%=s[i]%><%}%>", "test"}
        });
    }

    @Test
    public void print_runWithDataSet() throws IOException {
        try (
                Reader reader = new StringReader(source);
                Writer writer = new StringWriter();
        ) {
            JSPLikeProcessor.from(reader).print(writer);
            String AR = writer.toString();

            Assert.assertEquals(ER, AR);
        }
    }
}
