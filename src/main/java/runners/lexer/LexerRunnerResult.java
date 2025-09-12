package runners.lexer;
import org.gudelker.Token;
import java.util.List;

public class LexerRunnerResult {
    private final List<Token> tokens;
    private final String errorMessage;

    public LexerRunnerResult(List<Token> tokens, String errorMessage) {
        this.tokens = tokens;
        this.errorMessage = errorMessage;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }
}

