package expressions;

import exceptions.ParserException;

import java.util.*;
import java.util.stream.*;

public class MyFunction {
    private final String[] parameterNames;
    private final Expression rule;
    final int numberOfParameters;
    final public String name;

    public MyFunction(String name, String[] parameterNames, Expression rule) {
        this.name = name;
        this.parameterNames = parameterNames;
        this.rule = rule;
        numberOfParameters = parameterNames.length;
    }

    int evaluate(int[] parameters,
                 Map<String, MyFunction> functions) throws ParserException {
        assert (parameters.length == parameterNames.length);
        return rule.evaluate(IntStream.range(0, parameters.length)
                .boxed()
                .collect(Collectors.toMap(
                        i -> parameterNames[i],
                        i -> parameters[i])), functions);
    }
}
