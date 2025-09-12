// src/main/java/runners/LexerRunner.java
package runners.lexer;

import interpreter.ErrorHandler;
import org.gudelker.DefaultLexer;
import org.gudelker.LexerFactory;
import org.gudelker.result.LexerResult;
import org.gudelker.result.LexerSyntaxError;
import org.gudelker.result.ValidTokens;
import org.gudelker.sourcereader.SourceReader;
import org.gudelker.utilities.Version;

import java.util.Collections;

public class LexerRunner {
    public LexerRunnerResult runLexer(SourceReader sourceReader, Version version, ErrorHandler errorHandler)  {
        DefaultLexer lexer = LexerFactory.INSTANCE.createLexer(version);
        LexerResult resultTokens = lexer.lex(sourceReader);

        if (resultTokens instanceof LexerSyntaxError syntaxError) {
            errorHandler.reportError(syntaxError.getMessageError());
            return new LexerRunnerResult(Collections.emptyList(), syntaxError.getMessageError());
        }

        if (resultTokens instanceof ValidTokens validTokens) {
            return new LexerRunnerResult(validTokens.getList(), null);
        }

        return new LexerRunnerResult(Collections.emptyList(), "Unknown lexer result");
    }
}
