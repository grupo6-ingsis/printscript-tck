package adapters.formatter;

import adapters.version.VersionAdapter;
import interpreter.PrintScriptFormatter;
import org.gudelker.formatter.DefaultFormatter;
import org.gudelker.formatter.DefaultFormatterFactory;
import org.gudelker.parser.tokenstream.TokenStream;
import org.gudelker.rules.FormatterRule;
import org.gudelker.rules.InputStreamFormatterConfigLoaderToMap;
import org.gudelker.sourcereader.InputStreamSourceReader;
import org.gudelker.statements.interfaces.Statement;

import org.gudelker.utilities.Version;
import runners.lexer.LexerRunnerResult;
import runners.parser.ParserRunner;
import runners.parser.ParserRunnerResult;

import java.io.InputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;


/* public class FormatterAdapter implements PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        Version v = VersionAdapter.toVersion(version);
        // lexer
        LexerRunner lexer = new LexerRunner();
        InputStreamSourceReader inputStreamSourceReader = new InputStreamSourceReader(src,8192);
        FormatterErrorHandler errorHandler = new FormatterErrorHandler();
        LexerRunnerResult tokensResult = lexer.runLexer(inputStreamSourceReader, v, errorHandler);

        // parser
        TokenStream tokenStream = new TokenStream(tokensResult.getTokens());
        ParserRunner parserRunner = new ParserRunner();
        ParserRunnerResult parserRunnerResult = parserRunner.runParser(tokenStream, v, errorHandler);

        // format
        InputStreamFormatterConfigLoaderToMap loader = new InputStreamFormatterConfigLoaderToMap(config);
        Map<String, FormatterRule> rules = loader.loadConfig();

        DefaultFormatter formatter = DefaultFormatterFactory.INSTANCE.createFormatter(v);

        List<Statement> statements = parserRunnerResult.getStatements();
        for (int i = 0; i < statements.size(); i++) {
            try {
                Statement statement = statements.get(i);
                if (i == statements.size() - 1) {
                    String result = formatter.format(statement, rules);
                    writer.write(removeTrailingNewline(result));
                } else {
                    writer.write(formatter.format(statement, rules));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }




    }
    public static String removeTrailingNewline(String str) {
        if (str.endsWith("\n")) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

}*/
