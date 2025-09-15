package adapters.interpreter;

import adapters.formatter.FormatterErrorHandler;
import adapters.version.VersionAdapter;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import org.gudelker.DefaultInterpreter;
import org.gudelker.InterpreterFactory;
import org.gudelker.parser.tokenstream.TokenStream;
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

        // Parser
        TokenStream tokenStream = new TokenStream(tokensResult.getTokens());
        ParserRunner parserRunner = new ParserRunner();
        ParserRunnerResult parserRunnerResult = parserRunner.runParser(tokenStream, v, errorHandler);

        List<Statement> statements = parserRunnerResult.getStatements();

        // Interpreter
        DefaultInterpreter interpreter = InterpreterFactory.INSTANCE.createInterpreter(v);


    }
}

