package parser;

import exceptions.ParserException;
import org.junit.Before;
import org.junit.Test;
import parser.TestGenerator.StringAndInt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ParserTest {
    private static final int TESTS = 25;
    private static final int DEPTH = 5;
    private static final int SEED = 566239;
    private Parser parser;
    private TestGenerator generator;

    @Before
    public void setUp() {
        parser = new Parser();
        generator = new TestGenerator(SEED);
    }

    private void check(String program, int expected) {
        try {
            assertEquals(parser.parseProgram(program), expected);
        } catch (Exception e) {
            fail();
        }
    }

    private void check(StringAndInt expression) {
        check(expression.expression, expression.result);
    }

    private void assertFails(String program) {
        try {
            parser.parseProgram(program);
            fail();
        } catch (ParserException e) {
            //  pass
            System.out.println(program);
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    @Test
    public void statementTests() {
        check("(2+2)", 4);
        check("(2+((3*4)/5))", 4);
        check("[((10+20)>(20+10))]?{1}:{0}", 0);
        check("g(x)={(f(x)+f((x/2)))}\n" +
                "f(x)={[(x>1)]?{(f((x-1))+f((x-2)))}:{x}}\n" +
                "g(10)", 60);
        assertFails("1 + 2 + 3 + 4 + 5");
        assertFails("f(x)={y}\n" +
                "f(10)");
        assertFails("g(x)={f(x)}\n" +
                "g(10)");
        assertFails("g(x)={(x+1)}\n" +
                "g(10,20)");
        assertFails("g(a,b)={(a/b)}\n" +
                "g(10,0)");
    }

    @Test
    public void testNumber() {
        check("566", 566);
        check("0", 0);
        check("000", 0);
        check("00239", 239);
        check("-1", -1);
        check("-7813", -7813);
        check("-088181", -88181);
        check(String.valueOf(Integer.MAX_VALUE), Integer.MAX_VALUE);
        check(String.valueOf(Integer.MIN_VALUE), Integer.MIN_VALUE);
        for (int i = 0; i < TESTS; i++) {
            check(generator.randomNumber());
        }
        assertFails("-");
        assertFails("97a8");
        assertFails("1.1");
        assertFails("23-1");
        assertFails("0-1");
        assertFails(String.valueOf(Long.MAX_VALUE));
        assertFails(String.valueOf(Long.MIN_VALUE));
        assertFails(String.valueOf(-(long) (Integer.MIN_VALUE)));
        assertFails("2 3");
    }

    @Test
    public void testCalculator() {
        check("(5+7)", 12);
        check("(5-7)", -2);
        check("(5*7)", 35);
        check("(55/7)", 7);
        check("(7%5)", 2);
        check("(7>5)", 1);
        check("(7<5)", 0);
        check("(5<7)", 1);
        check("(7=5)", 0);
        check("(7=7)", 1);
        check("(-3/2)", -1);
        check("(-3%2)", -1);
        check("((((7%4)+7)*5)-5)", 45);
        check("((7>(1-4))*((6%4)*((33/2)/3)))", 10);
        for (int i = 0; i < TESTS; i++) {
            check(generator.randomBinary(DEPTH));
        }
        assertFails("()");
        assertFails("(5/((7%7)-1)))");
        assertFails("(5%3");
        assertFails("(7>(3+1)))");
        assertFails(";)");
        assertFails("((2+3)%((2/2)-1))");
        assertFails("((2*3)/(2<1))");
        assertFails("5+9");
        assertFails("(3*(4/5))*3");
        assertFails("2*(1-3)");
        assertFails("(2+2)4");
        assertFails("(2 + 2)");
    }
}