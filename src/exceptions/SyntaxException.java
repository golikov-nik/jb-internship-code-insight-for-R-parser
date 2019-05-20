package exceptions;

public class SyntaxException extends ParserException {
    public SyntaxException() {
        super("SYNTAX ERROR");
    }
}
