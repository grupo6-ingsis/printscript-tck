package runners.parser;

import org.gudelker.statements.interfaces.Statement;

import java.util.List;

public class ParserRunnerResult {
    List<Statement> statements;
    private final String errorMessage;
    public ParserRunnerResult(List<Statement> statements, String errorMessage) {
        this.statements = statements;
        this.errorMessage = errorMessage;
    }

    public List<Statement> getStatements() {
        return statements;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }
}
