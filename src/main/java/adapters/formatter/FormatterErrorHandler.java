package adapters.formatter;

import interpreter.ErrorHandler;

import java.util.ArrayList;
import java.util.List;

public class FormatterErrorHandler implements ErrorHandler {
    private final List<String> errors = new ArrayList<>();

    @Override
    public void reportError(String message) {
        errors.add(message);
    }

    public List<String> getErrors() {
        return errors;
    }
}
