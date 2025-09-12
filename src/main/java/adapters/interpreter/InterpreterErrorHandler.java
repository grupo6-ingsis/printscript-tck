package adapters.interpreter;

import interpreter.ErrorHandler;

import java.util.ArrayList;
import java.util.List;

public class InterpreterErrorHandler implements ErrorHandler {
    private final List<String> errors = new ArrayList<>();

    @Override
    public void reportError(String message) {
        errors.add(message);
    }

    public void addErrors(List<String> moreErrors) {
        errors.addAll(moreErrors);
    }

    public List<String> getErrors() {
        return errors;
    }
}
