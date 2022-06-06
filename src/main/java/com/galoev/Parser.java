package com.galoev;

import com.galoev.exceptions.ParserException;
import com.galoev.expressions.*;

import java.util.List;
import java.util.Stack;

public class Parser {
    public IExpression parse(List<Token> tokens) throws ParserException {
        System.out.println(tokens);
        Stack<IExpression> operands = new Stack<>();
        Stack<Token> operators = new Stack<>();

        for (Token token : tokens) {
            System.out.println(token);
            switch (token.getType()) {
                case DIGIT -> operands.push(new Literal(token.getVal()));
                case VARIABLE -> operands.push(new Variable(token.getVal()));
                case LPAREN -> operators.push(token);
                case RPAREN -> {
                    while (operators.size() > 0 && !operators.peek().getType().equals(Token.Type.LPAREN)) {
                        combineExpressions(operands, operators);
                    }
                    if (operators.size() == 0 || !operators.peek().getType().equals(Token.Type.LPAREN) || operands.size() == 0)
                        throw new ParserException();
                    operators.pop();
                    operands.push(new ParenExpression(operands.pop()));
                }
                case PLUS, MINUS, MULTIPLY, DIVIDE -> {
                    int priority = token.getType().getPriority();
                    while (operators.size() > 0 && operators.peek().getType().getPriority() >= priority) {
                        combineExpressions(operands, operators);
                    }
                    operators.push(token);
                }
                default -> throw new ParserException();
            }
        }

        while (operators.size() > 0) {
            combineExpressions(operands, operators);
        }

        if (operands.size() != 1) throw new ParserException();
        return operands.pop();
    }

    private void combineExpressions(Stack<IExpression> operands, Stack<Token> operators) throws ParserException {
        if (operands.size() < 2) throw new ParserException();
        IExpression firstExpression = operands.pop();
        IExpression secondExpression = operands.pop();
        Token operator = operators.pop();
        operands.push(new BinaryExpression(firstExpression, secondExpression, operator.getType()));
    }
}
