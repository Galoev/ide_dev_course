package com.galoev.expressions;

import com.galoev.Token;

import java.util.Objects;

public class BinaryExpression implements IExpression {
    private final IExpression firstExpression;
    private final IExpression secondExpression;
    private final Token.Type operator;

    public BinaryExpression(IExpression firstExpression, IExpression secondExpression, Token.Type operator) {
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
        this.operator = operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryExpression that = (BinaryExpression) o;
        return Objects.equals(firstExpression, that.firstExpression) && Objects.equals(secondExpression, that.secondExpression) && operator == that.operator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstExpression, secondExpression, operator);
    }
}
