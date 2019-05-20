package exceptions;

public class ArgumentMismatchException extends ParserException {
    public final String name;
    public final int line;

    public ArgumentMismatchException(String name, int line) {
        super("ARGUMENT NUMBER MISMATCH " + name + ":" + (line + 1));
        this.name = name;
        this.line = line;
    }
}
