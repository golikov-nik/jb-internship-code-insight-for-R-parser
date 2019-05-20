package expressions;

import exceptions.ArgumentMismatchException;
import exceptions.ParserException;
import exceptions.UnknownFunctionException;

import java.util.*;

public class CallExpression implements Expression {
    private final String name;
    private final List<Expression> arguments;
    private final int line;

    public CallExpression(String name, List<Expression> arguments, int line) {
        this.name = name;
        this.arguments = arguments;
        this.line = line;
    }

    @Override
    public int evaluate(Map<String, Integer> scope,
                        Map<String, MyFunction> functions) throws ParserException {
        MyFunction f = functions.get(name);
        if (f == null) {
            throw new UnknownFunctionException(name, line);
        }
        if (arguments.size() != f.numberOfParameters) {
            throw new ArgumentMismatchException(name, line);
        }
        int[] parameters = new int[arguments.size()];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = arguments.get(i).evaluate(scope, functions);
        }
        return f.evaluate(parameters, functions);
    }
}
