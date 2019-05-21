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

    TestGenerator() {
        random = new Random();
    }

    TestGenerator(long seed) {
        random = new Random(seed);
    }

    ProgramWithResult randomNumber() {
        int result = random.nextInt();
        return new ProgramWithResult(result + "", result);
    }

    ProgramWithResult randomBinary(int depth) {
        if (depth == 0) {
            return randomNumber();
        }
        char operation = OPS.charAt(random.nextInt(OPS.length()));
        ProgramWithResult left = randomBinary(depth - 1);
        ProgramWithResult right = randomBinary(depth - 1);
        try {
            return new ProgramWithResult(String.format("(%s%c%s)", left.program, operation,
                    right.program), CHECKED_OPERATIONS.get(operation).apply(
                    left.result, right.result));
        } catch (ArithmeticException e) {
            return randomBinary(depth);
        }
    }

    class ProgramWithResult {
        String program;
        int result;

        ProgramWithResult(String program, int result) {
            this.program = program;
            this.result = result;
        }
    }
}
