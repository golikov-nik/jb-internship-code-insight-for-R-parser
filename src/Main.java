import exceptions.ParserException;
import parser.Parser;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        try (var in = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println(new Parser().parseProgram(
                    in.lines().toArray(String[]::new)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            System.out.println(e.getMessage());
        }
    }
}
