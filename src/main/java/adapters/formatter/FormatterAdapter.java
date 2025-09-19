package adapters.formatter;


import adapters.version.VersionAdapter;
import interpreter.PrintScriptFormatter;
import org.gudelker.formatter.DefaultFormatter;
import org.gudelker.formatter.DefaultFormatterFactory;
import org.gudelker.lexer.DefaultLexer;
import org.gudelker.lexer.LexerFactory;
import org.gudelker.lexer.StreamingLexer;
import org.gudelker.lexer.StreamingLexerResult;
import org.gudelker.parser.DefaultParser;
import org.gudelker.parser.DefaultParserFactory;
import org.gudelker.parser.StreamingParser;
import org.gudelker.parser.StreamingParserResult;
import org.gudelker.rules.FormatterRule;
import org.gudelker.rules.InputStreamFormatterConfigLoaderToMap;
import org.gudelker.sourcereader.InputStreamSourceReader;
import org.gudelker.statements.interfaces.Statement;
import org.gudelker.utilities.Version;

import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FormatterAdapter implements PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        Version v = VersionAdapter.toVersion(version);
        DefaultLexer defaultLexer = LexerFactory.INSTANCE.createLexer(v);
        DefaultParser defaultParser = DefaultParserFactory.INSTANCE.createParser(v);
        StreamingLexer streamingLexer = new StreamingLexer(defaultLexer);
        StreamingParser streamingParser = new StreamingParser(defaultParser);
        InputStreamSourceReader sourceReader = new InputStreamSourceReader(src,8192);

        streamingLexer.initialize(sourceReader);

        List<Statement> statements = new ArrayList<>();
        while (streamingLexer.hasMore() || streamingParser.hasMore()) {
            if (streamingLexer.hasMore()) {
                StreamingLexerResult lexerResult = streamingLexer.nextBatch(10);
                if (lexerResult instanceof StreamingLexerResult.TokenBatch) {
                    StreamingLexerResult.TokenBatch tokenBatch = (StreamingLexerResult.TokenBatch) lexerResult;
                    streamingParser.addTokens(tokenBatch.getTokens());
                }
            }
            StreamingParserResult parseResult = streamingParser.nextStatement();
            if (parseResult instanceof StreamingParserResult.StatementParsed) {
                StreamingParserResult.StatementParsed statementParsed = (StreamingParserResult.StatementParsed) parseResult;
                statements.add(statementParsed.getStatement());
            } else if (parseResult instanceof StreamingParserResult.Error) {
                StreamingParserResult.Error error = (StreamingParserResult.Error) parseResult;
                if (error.getMessage().toLowerCase().contains("need more tokens")) {
                    continue;
                }
                break;
            } else if (parseResult == StreamingParserResult.Finished.INSTANCE) {
                break;
            }
        }

        // format
        InputStreamFormatterConfigLoaderToMap loader = new InputStreamFormatterConfigLoaderToMap(config);
        Map<String, FormatterRule> rules = loader.loadConfig();

        DefaultFormatter formatter = DefaultFormatterFactory.INSTANCE.createFormatter(v);

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

}
