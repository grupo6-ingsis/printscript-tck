package adapters.formatter;



import adapters.version.VersionAdapter;
import interpreter.PrintScriptFormatter;
import org.gudelker.DefaultFormatter;
import org.gudelker.DefaultFormatterFactory;
import org.gudelker.rules.FormatterRule;
import org.gudelker.rules.InputStreamFormatterConfigLoaderToMap;
import org.gudelker.sourcereader.InputStreamSourceReader;
import org.gudelker.statements.interfaces.Statement;
import org.gudelker.tokenstream.TokenStream;
import org.gudelker.utilities.Version;
import runners.lexer.LexerRunner;
import runners.lexer.LexerRunnerResult;
import runners.parser.ParserRunner;
import runners.parser.ParserRunnerResult;

import java.io.InputStream;
import java.io.Writer;
import java.util.Map;


public class FormatterAdapter implements PrintScriptFormatter {

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
        DefaultFormatter formatter = DefaultFormatterFactory.INSTANCE.createFormatter(v);
        InputStreamFormatterConfigLoaderToMap loader = new InputStreamFormatterConfigLoaderToMap(config);
        Map<String, FormatterRule> rules = loader.loadConfig();
        for (Statement statement : parserRunnerResult.getStatements()) {
            try {
                writer.write(formatter.format(statement, rules));
            } catch (Exception e) {
                //
            }
        }

    }
}
