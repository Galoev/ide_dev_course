package com.galoev.expressions;

import java.util.Objects;

public class ParenExpression implements IExpression {
    private final IExpression operand;

    public ParenExpression(IExpression operand) {
        this.operand = operand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParenExpression that = (ParenExpression) o;
        return Objects.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand);
    }
}
