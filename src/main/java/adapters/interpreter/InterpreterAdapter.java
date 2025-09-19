package adapters.interpreter;

import adapters.version.VersionAdapter;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import kotlin.Unit;
import org.gudelker.interpreter.*;
import org.gudelker.lexer.DefaultLexer;
import org.gudelker.lexer.LexerFactory;
import org.gudelker.lexer.StreamingLexer;
import org.gudelker.parser.DefaultParser;
import org.gudelker.parser.DefaultParserFactory;
import org.gudelker.parser.StreamingParser;
import org.gudelker.pipeline.StreamingPipeline;

import org.gudelker.sourcereader.InputStreamSourceReader;
import org.gudelker.utilities.Version;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class InterpreterAdapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        Version v = VersionAdapter.toVersion(version);
        DefaultLexer lexer = LexerFactory.INSTANCE.createLexer(v);
        StreamingLexer streamingLexer = new StreamingLexer(lexer);
        DefaultParser parser = DefaultParserFactory.INSTANCE.createParser(v);
        StreamingParser streamingParser = new StreamingParser(parser);
        ChunkBaseInterpreter interpreter = ChunkBaseFactory.INSTANCE.createInterpreter(v, new InputProviderAdapter(provider, emitter));
        StreamingInterpreter streamingInterpreter = new StreamingInterpreter(interpreter.getEvaluators());
        StreamingPipeline pipeline = new StreamingPipeline(streamingLexer, streamingParser, streamingInterpreter);
        List<String> processedResults = new ArrayList<>();
        InputStreamSourceReader reader = new InputStreamSourceReader(src,8192);
        try{pipeline.initialize(reader);
            boolean success = pipeline.processAll(result -> {
                if(!(result instanceof Unit || result == null)) {
                    processedResults.add(result.toString());
                }
                return true;
            });
            if (!success) {
                handler.reportError("ERROR");
            }
            else{
                for(String result : processedResults){
                    emitter.print(result);
                }
            }
        }
        catch(OutOfMemoryError e){
            handler.reportError("Java heap space");
        }

    }
}

