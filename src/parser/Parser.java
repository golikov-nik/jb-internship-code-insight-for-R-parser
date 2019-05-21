package parser;

import exceptions.ParserException;
import exceptions.SyntaxException;
import expressions.*;

import java.util.*;
import java.util.function.*;

public class Parser {
    private static final Map<Character, BinaryOperator<Integer>> OPERATIONS = Map.of(
            '+', Integer::sum,
            '-', (x, y) -> x - y,
            '*', (x, y) -> x * y,
            '/', (x, y) -> x / y,
            '%', (x, y) -> x % y,
            '>', (x, y) -> x > y ? 1 : 0,
            '<', (x, y) -> x < y ? 1 : 0,
            '=', (x, y) -> x.equals(y) ? 1 : 0
    );
    private int ptr;
    private static final char END = '\0';
    private String expression;
    private int line;

    private char getChar() {
        return expression.charAt(ptr);
    }

    private char nextChar() {
        return expression.charAt(ptr++);
    }

    private boolean test(char ch) {
        return getChar() == ch;
    }

    private boolean test(Predicate<Character> p) {
        return p.test(getChar());
    }

    private boolean testNext(char ch) {
        if (test(ch)) {
            ptr++;
            return true;
        }
        return false;
    }

    private void expect(char ch) throws SyntaxException {
        if (!testNext(ch)) {
            throw new SyntaxException();
        }
    }

    private void expect(String s) throws SyntaxException {
        for (int i = 0; i < s.length(); i++) {
            expect(s.charAt(i));
        }
    }

    private Expression parseNumber() throws SyntaxException {
        var sb = new StringBuilder();
        if (testNext('-')) {
            sb.append('-');
        }
        while (test(Character::isDigit)) {
            sb.append(nextChar());
        }
        try {
            return new ConstantExpression(Integer.parseInt(sb.toString()));
        } catch (NumberFormatException e) {
            throw new SyntaxException();
        }
    }

    private boolean isCharacter(char ch) {
        ch = Character.toLowerCase(ch);
        return 'a' <= ch && ch <= 'z' || ch == '_';
    }

    private String parseIdentifier() throws SyntaxException {
        var sb = new StringBuilder();
        while (test(this::isCharacter)) {
            sb.append(nextChar());
        }
        String result = sb.toString();
        if (result.isEmpty()) {
            throw new SyntaxException();
        }
        return result;
    }

    private List<Expression> readArgumentList() throws SyntaxException {
        expect('(');
        List<Expression> result = new ArrayList<>();
        while (true) {
            result.add(parseExpression());
            if (testNext(')')) {
                break;
            }
            expect(',');
        }
        return result;
    }

    private Expression parseExpression() throws SyntaxException {
        if (test('[')) {
            return parseIfExpression();
        }
        if (test('(')) {
            return parseBinaryExpression();
        }
        if (test('-') || test(Character::isDigit)) {
            return parseNumber();
        }
        String identifier = parseIdentifier();
        return test('(') ? new CallExpression(identifier, readArgumentList(), line) :
                new Identifier(identifier, line);
    }

    /*
    в грамматике круглые скобки, в примерах несколько раз фигурные,
    так что я решил, что ошибка в грамматике
     */

    private Expression parseIfExpression() throws SyntaxException {
        expect('[');
        Expression condition = parseExpression();
        expect("]?{");
        Expression first = parseExpression();
        expect("}:{");
        Expression second = parseExpression();
        expect('}');
        return new IfExpression(condition, first, second);
    }

    /*
    для того, чтобы запоминать выражение и сохранять асимптотику линейной сделан
    класс Substring, который вместо копирования подстроки в каждое выражение
    сохраняет только ссылку на строку и индексы, а копию подстроки создает
    только один раз (если произойдёт ошибка)
     */

    private Expression parseBinaryExpression() throws SyntaxException {
        int start = ptr;
        expect('(');
        Expression left = parseExpression();
        char op = nextChar();
        var operation = OPERATIONS.get(op);
        if (operation == null) {
            throw new SyntaxException();
        }
        Expression right = parseExpression();
        expect(')');
        return new BinaryExpression(operation, left, right,
                new Substring(expression, start, ptr), line);
    }

    private List<String> readParameterList() throws SyntaxException {
        expect('(');
        List<String> result = new ArrayList<>();
        Set<String> parameterSet = new HashSet<>();
        while (true) {
            String parameter = parseIdentifier();
            if (parameterSet.contains(parameter)) {
                //  соответствует грамматике, но скорее синтаксическая ошибка
                throw new SyntaxException();
            }
            result.add(parameter);
            parameterSet.add(parameter);
            if (testNext(')')) {
                break;
            }
            expect(',');
        }
        return result;
    }

    private MyFunction parseFunction() throws SyntaxException {
        String name = parseIdentifier();
        List<String> parameters = readParameterList();
        expect("={");
        Expression rule = parseExpression();
        expect('}');
        return new MyFunction(name, parameters.toArray(String[]::new), rule);
    }

    private void setExpression(String s) {
        ptr = 0;
        expression = s + END;
    }

    public int parseProgram(String[] lines) throws ParserException {
        int nlines = lines.length;
        if (nlines == 0) {
            throw new SyntaxException();
        }
        Map<String, MyFunction> functions = new HashMap<>();
        for (line = 0; line < nlines - 1; line++) {
            setExpression(lines[line]);
            MyFunction f = parseFunction();
            expect(END);
            if (functions.put(f.name, f) != null) {
                //  соответствует грамматике, но скорее синтаксическая ошибка
                throw new SyntaxException();
            }
        }
        setExpression(lines[nlines - 1]);
        Expression result = parseExpression();
        expect(END);
        return result.evaluate(Collections.emptyMap(), functions);
    }

    public int parseProgram(String program) throws ParserException {
        return parseProgram(program.split("\n", -1));
    }
}
