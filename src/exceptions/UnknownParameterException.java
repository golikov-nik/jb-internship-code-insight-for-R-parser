package exceptions;

public class UnknownParameterException extends ParserException {
    public final String name;
    public final int line;

    public UnknownParameterException(String name, int line) {
        super("PARAMETER NOT FOUND " + name + ":" + (line + 1));
        this.name = name;
        this.line = line;
    }
}
