package com.galoev.pascal.lang

import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.jetbrains.annotations.NonNls

class PascalTokenType(@NonNls debugName: String) : IElementType(debugName, PascalLanguage) {
    override fun toString(): String {
        return "PascalTokenType." + super.toString()
    }

    companion object {
        val PROGRAM = PascalTokenType("PROGRAM")
        val BEGIN = PascalTokenType("BEGIN")
        val END = PascalTokenType("END")
        val CASE = PascalTokenType("CASE");
        val OF = PascalTokenType("OF")
        val IF = PascalTokenType("IF")
        val THEN = PascalTokenType("THEN")
        val ELSE = PascalTokenType("ELSE")
        val FOR = PascalTokenType("FOR")
        val TO = PascalTokenType("TO")
        val DOWNTO = PascalTokenType("DOWNTO")
        val DO = PascalTokenType("DO")
        val IN = PascalTokenType("IN")
        val WHILE = PascalTokenType("WHILE")
        val REPEAT = PascalTokenType("REPEAT")
        val UNTIL = PascalTokenType("UNTIL")
        val USES = PascalTokenType("USES")
        val PROCEDURE = PascalTokenType("PROCEDURE")
        val VAR = PascalTokenType("VAR")

        val ASSIGN = PascalTokenType("ASSIGN")
        val PLUS_ASSIGN = PascalTokenType("PLUS_ASSIGN")
        val MINUS_ASSIGN = PascalTokenType("MINUS_ASSIGN")
        val MULT_ASSIGN = PascalTokenType("MULT_ASSIGN")
        val DIV_ASSIGN = PascalTokenType("DIV_ASSIGN")

        val PLUS = PascalTokenType("PLUS")
        val MINUS = PascalTokenType("MINUS")
        val MULT = PascalTokenType("MULT")
        val DIV = PascalTokenType("DIV")

        val EQ = PascalTokenType("EQ")
        val NEQ = PascalTokenType("NEQ")
        val GT = PascalTokenType("GT")
        val LT = PascalTokenType("LT")
        val GTEQ = PascalTokenType("GTEQ")
        val LTEQ = PascalTokenType("LTEQ")

        val AND = PascalTokenType("AND")
        val OR = PascalTokenType("OR")
        val NOT = PascalTokenType("NOT")
        val XOR = PascalTokenType("XOR")
        val DDIV = PascalTokenType("DDIV")
        val MOD = PascalTokenType("MOD")
        val SHR = PascalTokenType("SHR")
        val SHL = PascalTokenType("SHL")

        val SEMI = PascalTokenType("SEMI")
        val COLON = PascalTokenType("COLON")
        val COMMA = PascalTokenType("COMMA")
        val DOT = PascalTokenType("DOT")
        val LPAREN = PascalTokenType("LPAREN")
        val RPAREN = PascalTokenType("RPAREN")

        val INTEGER = PascalTokenType("INTEGER")
        val BOOLEAN = PascalTokenType("BOOLEAN")
        val CHAR = PascalTokenType("CHAR")
        val REAL = PascalTokenType("REAL")
        val TRUE = PascalTokenType("TRUE")
        val FALSE = PascalTokenType("FALSE")

        val COMMENT = PascalTokenType("COMMENT")
        val IDENTIFIER = PascalTokenType("IDENTIFIER")
        val STRING = PascalTokenType("STRING")
        val NUM_INT = PascalTokenType("NUMBER INT")
        val NUM_HEX = PascalTokenType("NUMBER HEX")
        val NUM_REAL = PascalTokenType("NUMBER REAL")

        val KEYWORDS = TokenSet.create(PROGRAM, BEGIN, END, CASE, OF, IF, THEN, ELSE, FOR, TO, DOWNTO, DO, IN, WHILE,
            REPEAT, UNTIL, USES, PROCEDURE, VAR, TRUE, FALSE)
        val ASSIGNS = TokenSet.create(ASSIGN, PLUS_ASSIGN, MINUS_ASSIGN, MULT_ASSIGN, DIV_ASSIGN)
        val OPERATORS = TokenSet.create(AND, OR, NOT, XOR, DDIV, MOD, SHR, SHL, PLUS, MINUS, MULT, DIV, EQ, NEQ, GT, LT, GTEQ, LTEQ)
        val TYPES = TokenSet.create(INTEGER, REAL, BOOLEAN, CHAR)
        val NUMBERS = TokenSet.create(NUM_INT, NUM_HEX, NUM_REAL)
        val PARENS = TokenSet.create(LPAREN, RPAREN)
    }
}