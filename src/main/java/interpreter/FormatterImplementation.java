package interpreter;


import org.gudelker.DefaultFormatterFactory;
import org.gudelker.DefaultLexer;
import org.gudelker.LexerFactory;
import org.gudelker.utilities.Version;

import java.io.InputStream;
import java.io.Writer;

public class FormatterImplementation implements PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        Version v;
        switch (version) {
            case "1.0" -> v = Version.V1;
            case "1.1" -> v = Version.V2;
            default -> throw new IllegalArgumentException("Unsupported version: " + version);
        }

        DefaultLexer lexer = LexerFactory.INSTANCE.createLexer(v);



        var formatter = DefaultFormatterFactory.INSTANCE.createFormatter(v);
    }
}
