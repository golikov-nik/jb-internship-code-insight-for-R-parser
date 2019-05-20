package parser;

import exceptions.ParserException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser();
    }

    private void check(String program, int expected) {
        try {
            assertEquals(parser.parseProgram(program), expected);
        } catch (ParserException e) {
            fail();
        }
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
}