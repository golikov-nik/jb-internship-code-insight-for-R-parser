package expressions;

import exceptions.ParserException;

import java.util.*;

public interface Expression {
    int evaluate(Map<String, Integer> scope,
                 Map<String, MyFunction> functions) throws ParserException;
}
