package adapters.analyzer;

/*public class LinterAdapter implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        Version v = VersionAdapter.toVersion(version);
        // lexer
        LexerRunner lexer = new LexerRunner();
        InputStreamSourceReader inputStreamSourceReader = new InputStreamSourceReader(src,8192);
        FormatterErrorHandler errorHandler = new FormatterErrorHandler();
        LexerRunnerResult tokensResult = lexer.runLexer(inputStreamSourceReader, v, errorHandler);

        // parser
        TokenStream tokenStream = new TokenStream(tokensResult.getTokens());
        ParserRunner parserRunner = new ParserRunner();
        ParserRunnerResult parserRunnerResult = parserRunner.runParser(tokenStream, v, errorHandler);

        List<Statement> statements = parserRunnerResult.getStatements();
        // After getting statements and rules
        InputStreamLinterConfigLoaderToMap loader = new InputStreamLinterConfigLoaderToMap(config);
        Map<String, LinterConfig> rules = loader.loadConfig();
        StatementStream statementStream = new StatementStream(statements);
        DefaultLinter linter = DefaultLinterFactory.INSTANCE.createLinter(v);
        CompoundResult result = linter.lint(statementStream, rules);

        for (LintViolation violation : result.getResults()) {
            handler.reportError(violation.getMessage());
        }


    }
}*/
