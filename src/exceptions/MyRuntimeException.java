package exceptions;

public class MyRuntimeException extends ParserException {
    public final String expression;
    public final int line;

    public MyRuntimeException(String expression, int line) {
        super("RUNTIME ERROR " + expression + ":" + (line + 1));
        this.expression = expression;
        this.line = line;
    }
}
