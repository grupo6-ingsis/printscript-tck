package adapters.interpreter;

import interpreter.InputProvider;
import interpreter.PrintEmitter;
import org.jetbrains.annotations.NotNull;

public class InputProviderAdapter implements org.gudelker.inputprovider.InputProvider {

    private final InputProvider delegate;
    private final PrintEmitter emitter;

    public InputProviderAdapter(InputProvider delegate, PrintEmitter emitter) {
        this.delegate = delegate;
        this.emitter = emitter;
    }

    @Override
    public @NotNull String nextInput(@NotNull String prompt) {
        emitter.print(prompt);
        return delegate.input(prompt);
    }
}
