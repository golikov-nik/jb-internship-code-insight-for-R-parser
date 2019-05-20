package exceptions;

public class UnknownFunctionException extends ParserException {
    public final String name;
    public final int line;

    public UnknownFunctionException(String name, int line) {
        super("FUNCTION NOT FOUND " + name + ":" + (line + 1));
        this.name = name;
        this.line = line;
    }
}
