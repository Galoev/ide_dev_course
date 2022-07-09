package com.galoev.expressions;

import java.util.Objects;

public class Literal implements IExpression {
    private final Character val;

    public Literal(Character val) {
        this.val = val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return Objects.equals(val, literal.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }
}
