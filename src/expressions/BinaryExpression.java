package expressions;

import exceptions.MyRuntimeException;
import exceptions.ParserException;
import parser.Substring;

import java.util.*;
import java.util.function.*;

public class BinaryExpression implements Expression {
    private final BinaryOperator<Integer> f;
    private final Expression a;
    private final Expression b;
    private final Substring expression;
    private final int line;

    public BinaryExpression(BinaryOperator<Integer> f, Expression a, Expression b,
                            Substring expression, int line) {
        this.f = f;
        this.a = a;
        this.b = b;
        this.expression = expression;
        this.line = line;
    }

    @Override
    public int evaluate(Map<String, Integer> scope,
                        Map<String, MyFunction> functions) throws ParserException {
        int resultA = a.evaluate(scope, functions);
        int resultB = b.evaluate(scope, functions);
        try {
            return f.apply(resultA, resultB);
        } catch (Exception e) {
            throw new MyRuntimeException(expression.get(), line);
        }
    }
}
