package сom.galoev;

import com.galoev.Lexer;
import com.galoev.Parser;
import com.galoev.Token;
import com.galoev.exceptions.LexerException;
import com.galoev.exceptions.ParserException;
import com.galoev.expressions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    private final Lexer lexer;
    private final Parser parser;

    public ParserTest() {
        this.lexer = new Lexer();
        this.parser = new Parser();
    }

    @Test
    public void testSimpleInput() throws Exception {
        assertEquals(new Literal('4'), parser.parse(lexer.getTokens("4")));
        assertEquals(new Variable('a'), parser.parse(lexer.getTokens("a")));
    }

    @Test
    public void testIllegalInput() {
        assertThrows(ParserException.class, () -> parser.parse(new ArrayList<>()));
        assertThrows(ParserException.class, () -> parser.parse(lexer.getTokens("+(1")));
        assertThrows(ParserException.class, () -> parser.parse(lexer.getTokens("a+")));
        assertThrows(ParserException.class, () -> parser.parse(lexer.getTokens("a*3)")));
        assertThrows(ParserException.class, () -> parser.parse(lexer.getTokens("3+/4")));
        assertThrows(ParserException.class, () -> parser.parse(lexer.getTokens("3+3*")));
        assertThrows(ParserException.class, () -> parser.parse(lexer.getTokens("(a+c*6+)")));
    }

    @Test
    public void testSimpleExpression() throws Exception {
        assertEquals(new BinaryExpression(new Literal('4'), new Variable('a'), Token.Type.MULTIPLY),
                    parser.parse(lexer.getTokens("a * 4")));
    }

    @Test
    public void testPriority() throws Exception {
        IExpression expected = new BinaryExpression(
                new BinaryExpression(new Literal('9'), new BinaryExpression(new Literal('8'), new Literal('7'), Token.Type.DIVIDE), Token.Type.MULTIPLY),
                new BinaryExpression(new BinaryExpression(new Literal('6'), new ParenExpression(new BinaryExpression(new Variable('c'), new Variable('b'), Token.Type.MINUS)), Token.Type.MULTIPLY),
                        new BinaryExpression(new BinaryExpression(new Literal('5'), new Variable('a'), Token.Type.DIVIDE),
                                new BinaryExpression(new Literal('1'), new Literal('4'), Token.Type.MULTIPLY), Token.Type.PLUS), Token.Type.PLUS), Token.Type.PLUS);
        IExpression actual = parser.parse(lexer.getTokens("4 * 1 + a / 5 + (b - c) * 6 + 7 / 8 * 9"));
        assertEquals(expected, actual);
    }

}
