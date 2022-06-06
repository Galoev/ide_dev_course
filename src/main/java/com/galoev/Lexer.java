package com.galoev;

import com.galoev.exceptions.LexerException;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    public List<Token> getTokens(String input) throws LexerException {
        int len = input.length();
        List<Token> tokens = new ArrayList<>();
        char currentChar;
        for (int i = 0; i < len; i++) {
            currentChar = input.charAt(i);
            switch (currentChar) {
                case '+' -> tokens.add(new Token(Token.Type.PLUS));
                case '-' -> tokens.add(new Token(Token.Type.MINUS));
                case '*' -> tokens.add(new Token(Token.Type.MULTIPLY));
                case '/' -> tokens.add(new Token(Token.Type.DIVIDE));
                case '(' -> tokens.add(new Token(Token.Type.LPAREN));
                case ')' -> tokens.add(new Token(Token.Type.RPAREN));
                default -> {
                    if (Character.isDigit(currentChar)) {
                        tokens.add(new Token(Token.Type.DIGIT, currentChar));
                        continue;
                    }
                    if (Character.isLetter(currentChar)) {
                        tokens.add(new Token(Token.Type.VARIABLE, currentChar));
                        continue;
                    }
                    if (Character.isWhitespace(currentChar)) {
                        continue;
                    }
                    throw new LexerException("Illegal character: " + currentChar);
                }
            }
        }
        return tokens;
    }
}
