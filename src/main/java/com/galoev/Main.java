package com.galoev;

import com.galoev.expressions.IExpression;

import java.util.List;
import java.util.Scanner;

public class Main {
    private final Scanner scanner;
    private final Lexer lexer;
    private final Parser parser;

    public Main() {
        scanner = new Scanner(System.in);
        lexer = new Lexer();
        parser = new Parser();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public void run() {
        try {
            while (true) {
                String input = scanner.nextLine();
                List<Token> tokens = lexer.getTokens(input);
                IExpression expression = parser.parse(tokens);
                System.out.println(tokens);
                System.out.println(expression);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
