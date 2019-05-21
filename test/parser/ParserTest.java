package parser;

import exceptions.ParserException;
import org.junit.Before;
import org.junit.Test;
import parser.TestGenerator.ProgramWithResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ParserTest {
    private static final int TESTS = 25;
    private static final int DEPTH = 5;
    private static final long SEED = 566239;
    private Parser parser;
    private TestGenerator generator;

    @Before
    public void setUp() {
        parser = new Parser();
        generator = new TestGenerator(SEED);
    }

    private void check(String program, int expected) throws ParserException {
        assertEquals(parser.parseProgram(program), expected);
    }

    private void check(ProgramWithResult program) throws ParserException {
        check(program.program, program.result);
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
    public void statementTests() throws ParserException {
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
    public void testNumber() throws ParserException {
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
        assertFails("1\n2");
        assertFails("");
        assertFails("2\n");
        assertFails("2\n\n");
    }

    @Test
    public void testCalculator() throws ParserException {
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
        assertFails("(2<=2)");
        assertFails("(3*(4/5))*3");
        assertFails("2*(1-3)");
        assertFails("(2+2)4");
        assertFails("(2 + 2)");
    }

    @Test
    public void testIfExpression() throws ParserException {
        check("[5]?{1}:{2}", 1);
        check("[0]?{(1+3)}:{45}", 45);
        check("[(1+(2*2))]?{(23-(29*2))}:{0}", -35);
        check("[[(5>2)]?{(2-2)}:{3}]?{(3+5)}:{[(2-1)]?{(3*3)}:{(5+5)}}", 9);
        check("[(5=5)]?{23}:{(1/0)}", 23);
        check("[(3-3)]?{(2%(3-3))}:{(3*(4*5))}", 60);
        check("(1+[(5>3)]?{10}:{20})", 11);
        assertFails("[5?{1}:{2}");
        assertFails("[5]?1:{2}");
        assertFails("[5]?{1}:2}");
        assertFails("[5]?{1}");
        assertFails("[(2/0)]?{1}:{23}");
        assertFails("[]?{1}:{23}");
        assertFails("[5]?{1}:{}");
        assertFails("[5]?{1}:(3)");
        assertFails("[(2-2)]?{}:{1}");
        assertFails("[2] ? {1} :{3}");
        assertFails("[2] ? {1} :{(3+}");
    }

    @Test
    public void testFunctions() throws ParserException {
        check("id(x)={x}\nid(5)", 5);
        check("max(x,y)={[(x>y)]?{x}:{y}}\nmax(2,7)", 7);
        check("max(x,y)={[(x>y)]?{x}:{y}}\nmax(7,2)", 7);
        check("fact(n)={[(n=0)]?{1}:{(n*fact((n-1)))}}\nfact(8)", 40320);
        check("sum(X,Y,_Z)={(X+(Y+_Z))}\nsum(1,sum(2,3,4),5)", 15);
        check("bin_pow(a,n)={[(n=0)]?{1}:{[((n%2)=0)]?{bin_pow((a*a),(n/2))}:" +
                        "{(a*bin_pow(a,(n-1)))}}}\nbin_pow(3,8)",
                6561);
        check("f(n)={[(n>1)]?{(f((n-1))+g((n-1)))}:{n}}\n" +
                "g(n)={[(n>1)]?{(g((n-1))+f(n))}:{n}}\nf(5)", 34);
        check("f(x,y,z)={[(((x*y)*z)<10)]?{((x+y)+z)}:" +
                "{(f((x/5),g((x-1),(y-1),(z-2)),g(((x-10)/5),(z/2),(y/3)))-5)}}\n" +
                "g(x,y,z)={f((x-1),(y-2),(z-1))}\n" +
                "f(100,50,239)", 33242);
        check("fib_rec(n)={[(n<2)]?{n}:" +
                "{(fib_rec((n-1))+fib_rec((n-2)))}}\nfib_rec(10)", 55);
        check("fib(n)={fib_loop(n,0,1)}\n" +
                "fib_loop(n,x,y)={[(n=0)]?{x}:" +
                "{fib_loop((n-1),y,(x+y))}}\nfib(40)", 102334155);
        assertFails("f(){}\nf()");
        assertFails("f(x)={}\n1");
        assertFails("f(x,)={x}\nf(5)");
        assertFails("f(x)={x}");
        assertFails("f(x,y)={(x+y)}\nf(1)");
        assertFails("f(x)={y}\nf(2)");
        assertFails("f(x)={g(x)}\nf(2)");
        assertFails("f(x,y)={(x+y)}\nf(1,2,)");
        assertFails("x");
        assertFails("f(5)");
        assertFails("f(x,x)={x}\nf(1,1)");
        assertFails("f(x)={x}\nf(x,y)={(x+y)}\nf(1)");
        assertFails("f1(x)={x}\nf1(5)");
        assertFails("f(x)=5\nf(1)");
        assertFails("f(x)={5\nf(1)");
        assertFails("f(x,y)={(x/y)}\ng(x)={f(x,x)}\ng(0)");
        assertFails("1\nf(x)={x}");
        assertFails("f(x)={x}\ng(x)={f}\ng(1)");
    }
}