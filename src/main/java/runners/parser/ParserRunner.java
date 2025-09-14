package runners.parser;

import interpreter.ErrorHandler;
import org.gudelker.parser.DefaultParser;
import org.gudelker.parser.DefaultParserFactory;
import org.gudelker.parser.result.ParserResult;
import org.gudelker.parser.result.ParserSyntaxError;
import org.gudelker.parser.result.Valid;
import org.gudelker.parser.tokenstream.TokenStream;
import org.gudelker.result.*;
import org.gudelker.parser.*;
import org.gudelker.utilities.Version;
import runners.lexer.LexerRunnerResult;

import java.util.Collections;

public class ParserRunner {
    public ParserRunnerResult runParser(TokenStream iterator, Version version, ErrorHandler errorHandler) {
        DefaultParser parser = DefaultParserFactory.INSTANCE.createParser(version);
        ParserResult parserResult = parser.parse(iterator);
        if (parserResult instanceof ParserSyntaxError syntaxError) {
            errorHandler.reportError(syntaxError.getError());
            return new ParserRunnerResult(Collections.emptyList(), syntaxError.getError());
        }
        if (parserResult instanceof Valid validStatements) {
            return new ParserRunnerResult(validStatements.getStatements(), null);
        }
        return new ParserRunnerResult(Collections.emptyList(), null);
    }
}
