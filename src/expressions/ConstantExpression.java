package expressions;

import java.util.*;

public class ConstantExpression implements Expression {
    private final int value;

    public ConstantExpression(int value) {
        this.value = value;
    }

    @Override
    public int evaluate(Map<String, Integer> scope,
                        Map<String, MyFunction> functions) {
        return value;
    }
}
