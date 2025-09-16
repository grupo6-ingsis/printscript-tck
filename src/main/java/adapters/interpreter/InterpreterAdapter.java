package adapters.interpreter;

import adapters.formatter.FormatterErrorHandler;
import adapters.version.VersionAdapter;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import org.gudelker.interpreter.ChunkBaseFactory;
import org.gudelker.interpreter.ChunkBaseInterpreter;
import org.gudelker.interpreter.DefaultInterpreter;
import org.gudelker.interpreter.InterpreterFactory;
import org.gudelker.parser.tokenstream.TokenStream;
import org.gudelker.result.InterpreterResult;
import org.gudelker.result.InvalidInterpreterResult;
import org.gudelker.result.ValidInterpretResult;
import org.gudelker.sourcereader.InputStreamSourceReader;
import org.gudelker.statements.interfaces.Statement;

import org.gudelker.utilities.Version;
import runners.lexer.LexerRunner;
import runners.lexer.LexerRunnerResult;
import runners.parser.ParserRunner;
import runners.parser.ParserRunnerResult;
import java.io.InputStream;
import java.util.List;

public class InterpreterAdapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        Version v = VersionAdapter.toVersion(version);

        // Lexer
        LexerRunner lexer = new LexerRunner();
        InputStreamSourceReader inputStreamSourceReader = new InputStreamSourceReader(src, 8192);
        FormatterErrorHandler errorHandler = new FormatterErrorHandler();
        LexerRunnerResult tokensResult = lexer.runLexer(inputStreamSourceReader, v, errorHandler);
        if(tokensResult.hasError()){
            handler.reportError(tokensResult.getErrorMessage());
        }

        // Parser
        TokenStream tokenStream = new TokenStream(tokensResult.getTokens());
        ParserRunner parserRunner = new ParserRunner();
        ParserRunnerResult parserRunnerResult = parserRunner.runParser(tokenStream, v, errorHandler);
        List<Statement> statements = parserRunnerResult.getStatements();
        if(parserRunnerResult.hasError()){
            handler.reportError(parserRunnerResult.getErrorMessage());
        }

        ChunkBaseInterpreter interpreter = ChunkBaseFactory.INSTANCE.createInterpreter(v);
        InterpreterResult result = interpreter.interpret(statements);
        if (result instanceof InvalidInterpreterResult) {
            Throwable exception = ((InvalidInterpreterResult) result).getException();
            handler.reportError(exception.getMessage());
        }

        if(result instanceof ValidInterpretResult) {
            List<?> values = ((ValidInterpretResult) result).getValue();
            for (Object value : values) {
                if (value != null && !"kotlin.Unit".equals(value.toString())) {
                    emitter.print(value.toString());
                }
            }
        }




    }
}

