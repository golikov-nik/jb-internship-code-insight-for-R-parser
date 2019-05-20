package expressions;

import exceptions.ParserException;
import exceptions.UnknownParameterException;

import java.util.*;

public class Identifier implements Expression {
    private final String name;
    private final int line;

    public Identifier(String name, int line) {
        this.name = name;
        this.line = line;
    }

    @Override
    public int evaluate(Map<String, Integer> scope,
                        Map<String, MyFunction> functions) throws ParserException {
        Integer value = scope.get(name);
        if (value == null) {
            throw new UnknownParameterException(name, line);
        }
        return value;
    }
}
