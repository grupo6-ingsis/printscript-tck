package implementation;
import adapters.interpreter.InterpreterAdapter;
import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;


public class CustomImplementationFactory implements PrintScriptFactory {
    @Override
    public PrintScriptInterpreter interpreter() {
        return new InterpreterAdapter();
    }

    @Override
    public PrintScriptFormatter formatter() {
        throw new UnsupportedOperationException("Not implemented yet");

    }

    @Override
    public PrintScriptLinter linter() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}