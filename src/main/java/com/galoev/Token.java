package com.galoev;

public class Token {
    public enum Type {
        DIGIT(-1),
        VARIABLE(-1),
        PLUS(1),
        MINUS(1),
        MULTIPLY(2),
        DIVIDE(2),
        LPAREN(0),
        RPAREN(0);
        private final int priority;

        Type(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
    }

    private final Type type;
    private final Character val;

    public Token(Type type) {
        this(type, null);
    }

    public Token(Type type, Character val) {
        this.type = type;
        this.val = val;
    }

    public Type getType() {
        return type;
    }

    public Character getVal() {
        return val;
    }

    @Override
    public String toString() {
        if (type.equals(Type.DIGIT) || type.equals(Type.VARIABLE)) {
            return type + ":" + val + "";
        }
        return type.toString();
    }
}
