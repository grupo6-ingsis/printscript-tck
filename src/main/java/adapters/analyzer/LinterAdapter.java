package adapters.analyzer;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;

import java.io.InputStream;

public class LinterAdapter implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {

    }
}
