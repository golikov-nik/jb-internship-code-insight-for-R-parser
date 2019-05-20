package parser;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

class TestGenerator {
    private static final Map<Character, BinaryOperator<Integer>> CHECKED_OPERATIONS = Map.of(
            '+', Math::addExact,
            '-', Math::subtractExact,
            '*', Math::multiplyExact,
            '/', TestGenerator::div,
            '%', (x, y) -> x % y,
            '>', (x, y) -> x > y ? 1 : 0,
            '<', (x, y) -> x < y ? 1 : 0,
            '=', (x, y) -> x.equals(y) ? 1 : 0
    );
    private static final String OPS = CHECKED_OPERATIONS.keySet()
            .stream().map(ch -> ch + "").collect(Collectors.joining());

    final private Random random;

    private static int div(int x, int y) {
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new ArithmeticException();
        }
        return x / y;
    }

    TestGenerator(int seed) {
        random = new Random(seed);
    }

    StringAndInt randomNumber() {
        int result = random.nextInt();
        return new StringAndInt(result + "", result);
    }

    StringAndInt randomBinary(int depth) {
        if (depth == 0) {
            return randomNumber();
        }
        char operation = OPS.charAt(random.nextInt(OPS.length()));
        StringAndInt left = randomBinary(depth - 1);
        StringAndInt right = randomBinary(depth - 1);
        try {
            return new StringAndInt(String.format("(%s%c%s)", left.expression, operation,
                    right.expression), CHECKED_OPERATIONS.get(operation).apply(
                    left.result, right.result));
        } catch (ArithmeticException e) {
            return randomBinary(depth);
        }
    }

    class StringAndInt {
        String expression;
        int result;

        StringAndInt(String expression, int result) {
            this.expression = expression;
            this.result = result;
        }
    }
}
