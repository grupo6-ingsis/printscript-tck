package adapters.analyzer;

import adapters.version.VersionAdapter;
import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import org.gudelker.lexer.*;
import org.gudelker.linter.DefaultLinter;
import org.gudelker.linter.DefaultLinterFactory;
import org.gudelker.linter.LinterConfig;
import org.gudelker.linterloader.InputStreamLinterConfigLoaderToMap;
import org.gudelker.parser.DefaultParser;
import org.gudelker.parser.DefaultParserFactory;
import org.gudelker.parser.StreamingParser;
import org.gudelker.parser.StreamingParserResult;
import org.gudelker.result.CompoundResult;
import org.gudelker.result.LintViolation;
import org.gudelker.sourcereader.InputStreamSourceReader;
import org.gudelker.statements.interfaces.Statement;
import org.gudelker.stmtposition.StatementStream;
import org.gudelker.utilities.Version;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LinterAdapter implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
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

        // After getting statements and rules
        InputStreamLinterConfigLoaderToMap loader = new InputStreamLinterConfigLoaderToMap(config);
        Map<String, LinterConfig> rules = loader.loadConfig();
        StatementStream statementStream = new StatementStream(statements);
        DefaultLinter linter = DefaultLinterFactory.INSTANCE.createLinter(v);
        CompoundResult result = linter.lint(statementStream, rules);

        for (LintViolation violation : result.getResults()) {
            handler.reportError(violation.getMessage());
        }
    }
}
