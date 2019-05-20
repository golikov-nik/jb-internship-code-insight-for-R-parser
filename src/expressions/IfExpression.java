package expressions;

import exceptions.ParserException;

import java.util.*;

public class IfExpression implements Expression {
    private final Expression condition;
    private final Expression first;
    private final Expression second;

    public IfExpression(Expression condition, Expression first, Expression second) {
        this.condition = condition;
        this.first = first;
        this.second = second;
    }

    @Override
    public int evaluate(Map<String, Integer> scope,
                        Map<String, MyFunction> functions) throws ParserException {
        return (condition.evaluate(scope, functions) == 0 ? second : first)
                .evaluate(scope, functions);
    }
}
