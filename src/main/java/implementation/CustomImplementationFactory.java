package implementation;

import adapters.formatter.FormatterAdapter;
import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;
import org.gudelker.Formatter;


public class CustomImplementationFactory implements PrintScriptFactory {
    @Override
    public PrintScriptInterpreter interpreter() {
        // your PrintScript implementation should be returned here.
        // make sure to ADAPT your implementation to PrintScriptInterpreter interface.

        throw new NotImplementedException("Needs implementation"); // TODO: implement

        // Dummy impl: return (src, version, emitter, handler) -> { };
    }

    @Override
    public PrintScriptFormatter formatter() {
        return new FormatterAdapter();

    }

    @Override
    public PrintScriptLinter linter() {
        // your PrintScript linter should be returned here.
        // make sure to ADAPT your linter to PrintScriptLinter interface.
        throw new NotImplementedException("Needs implementation"); // TODO: implement
    }
}