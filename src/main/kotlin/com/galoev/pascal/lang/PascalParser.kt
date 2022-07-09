package com.galoev.pascal.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class PascalParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        InnerParser(builder).parseProgram()
        return builder.treeBuilt
    }

    class InnerParser(private val builder: PsiBuilder) {
        fun parseProgram() {
            val mark = builder.mark()

            parseProgramHeader()
            expectAdvance(PascalTokenType.SEMI, "';'")
            if (builder.tokenType == PascalTokenType.USES) {
                parseUsesClause()
            }
            parseBlock()
            expectAdvance(PascalTokenType.DOT, "'.'")

            if (builder.tokenType != null)
                parseGarbage()

            mark.done(PascalElementType.PASCAL_FILE)
        }

        private fun parseBlock() {
            val mark = builder.mark()

            tryParseDeclaration()
            parseCompoundStatement()

            mark.done(PascalElementType.BLOCK)
        }

        private fun parseCompoundStatement() {
            val mark = builder.mark()

            assertAdvance(PascalTokenType.BEGIN)
            parseStatementList()
            expectAdvance(PascalTokenType.END, "'END'")

            mark.done(PascalElementType.COMPOUND_STATEMENT)
        }

        private fun parseStatementList() {
            parseLoop("statement", PascalTokenType.SEMI, "';'", listOf(
                PascalTokenType.END,
                PascalTokenType.BEGIN,
                PascalTokenType.PROCEDURE,
                PascalTokenType.USES,
                PascalTokenType.UNTIL)) {
                parseStatement()
            }
        }


        private fun parseStatement() {
            if (tryParseAssignments()) return
            if (tryParseProcedureStatement()) return
            if (tryParseStructuredStatement()) return
        }

        private fun tryParseStructuredStatement(): Boolean {
            val mark = builder.mark()

            if (builder.tokenType == PascalTokenType.BEGIN) {
                parseCompoundStatement()
                mark.done(PascalElementType.STRUCTURED_STATEMENT)
                return true
            }

            if (tryParseConditionalStatement()) {
                mark.done(PascalElementType.STRUCTURED_STATEMENT)
                return true
            }

            if (tryParseRepetitiveStatement()) {
                mark.done(PascalElementType.STRUCTURED_STATEMENT)
                return true
            }

            mark.rollbackTo()
            return false
        }

        private fun tryParseRepetitiveStatement(): Boolean {
            return when (builder.tokenType) {
                PascalTokenType.FOR -> {parseForStatement(); true}
                PascalTokenType.REPEAT -> {parseRepeatStatement(); true}
                PascalTokenType.WHILE -> {parseWhileStatement(); true}
                else -> false
            }
        }

        private fun parseWhileStatement() {
            val mark = builder.mark()

            assertAdvance(PascalTokenType.WHILE)
            parseExpression()
            expectAdvance(PascalTokenType.DO, "'DO'")
            parseStatement()

            mark.done(PascalElementType.WHILE_STATEMENT)
        }

        private fun parseRepeatStatement() {
            val mark = builder.mark()

            assertAdvance(PascalTokenType.REPEAT)
            parseStatementList()
            if (builder.tokenType == PascalTokenType.SEMI) {
                expectAdvance(PascalTokenType.SEMI, "'SEMI'")
            }
            expectAdvance(PascalTokenType.UNTIL, "'UNTIL'")
            parseExpression()

            mark.done(PascalElementType.REPEAT_STATEMENT)
        }

        private fun parseForStatement() {
            val mark = builder.mark()

            assertAdvance(PascalTokenType.FOR)
            parseIdentifier()
            expectAdvance(PascalTokenType.ASSIGN, "':='")
            parseExpression()
            when (builder.tokenType) {
                PascalTokenType.TO -> expectAdvance(PascalTokenType.TO, "'TO'")
                PascalTokenType.DOWNTO -> expectAdvance(PascalTokenType.DOWNTO, "'DOWNTO'")
                else -> errorAdvance("'TO' or 'DOWNTO'")
            }
            parseExpression()
            expectAdvance(PascalTokenType.DO, "'DO'")
            parseStatement()

            mark.done(PascalElementType.FOR_STATEMENT)
        }

        private fun tryParseConditionalStatement(): Boolean {
            return when(builder.tokenType) {
                PascalTokenType.CASE -> {
                    parseCaseStatement();
                    true
                }
                PascalTokenType.IF -> {
                    parseIfStatement();
                    true
                }
                else -> false
            }
        }

        private fun parseIfStatement() {
            val mark = builder.mark()

            assertAdvance(PascalTokenType.IF)
            parseExpression()
            expectAdvance(PascalTokenType.THEN, "'THEN'")
            parseStatement()
            if (builder.tokenType == PascalTokenType.SEMI)
                expectAdvance(PascalTokenType.SEMI, "';'")

            if (builder.tokenType == PascalTokenType.ELSE) {
                parseElseIf()
            }

            mark.done(PascalElementType.IF_STATEMENT)
        }

        private fun parseCaseStatement() {
            val mark = builder.mark()

            assertAdvance(PascalTokenType.CASE)
            parseExpression()
            expectAdvance(PascalTokenType.OF, "'OF'")
            parseCases()
            if (builder.tokenType == PascalTokenType.ELSE) {
                parseElsePart()
            }
            expectAdvance(PascalTokenType.END, "'END'")

            mark.done(PascalElementType.CASE_STATEMENT)
        }

        private fun parseElsePart() {
            val mark = builder.mark()
            assertAdvance(PascalTokenType.ELSE)
            parseStatementList()
            mark.done(PascalElementType.ELSE)
        }

        private fun parseElseIf() {
            val mark = builder.mark()
            assertAdvance(PascalTokenType.ELSE)
            parseStatement()
            mark.done(PascalElementType.ELSE)
        }

        private fun parseCases() {
            parseLoop("cases", PascalTokenType.SEMI, "';'", listOf(
                PascalTokenType.END,
                PascalTokenType.ELSE,
                PascalTokenType.BEGIN,
                PascalTokenType.PROCEDURE)) {
                val mark = builder.mark()
                parseCaseConstants()
                expectAdvance(PascalTokenType.COLON, "':'")
                parseStatement()
                mark.done(PascalElementType.CASE_ENTRY)
            }
        }

        private fun parseCaseConstants() {
            parseLoop("case constants", PascalTokenType.COMMA, "','", listOf(
                PascalTokenType.COLON,
                PascalTokenType.END,
                PascalTokenType.SEMI,
                PascalTokenType.BEGIN,
                PascalTokenType.PROCEDURE)) {
                val mark = builder.mark()
                parseConstant()
                if (builder.tokenType == PascalTokenType.DOT) {
                    assertAdvance(PascalTokenType.DOT)
                    expectAdvance(PascalTokenType.DOT, "'.'")
                    parseConstant()
                }
                mark.done(PascalElementType.CASE_CONSTANT)
            }
        }

        private fun parseConstant() {
            val mark = builder.mark()
            when(builder.tokenType) {
                PascalTokenType.PLUS -> assertAdvance(PascalTokenType.PLUS)
                PascalTokenType.MINUS -> assertAdvance(PascalTokenType.MINUS)
            }
            parseUnsignedConstant()
            mark.done(PascalElementType.CONSTANT)
        }

        private fun tryParseProcedureStatement(): Boolean {
            val mark = builder.mark()
            if (builder.tokenType == PascalTokenType.IDENTIFIER) {
                parseIdentifier()
            }
            else {
                mark.rollbackTo()
                return false
            }

            if (builder.tokenType == PascalTokenType.LPAREN || builder.tokenType == PascalTokenType.SEMI) {
                if (builder.tokenType == PascalTokenType.LPAREN) {
                    parseActualParameterList()
                }
            }
            else {
                mark.rollbackTo()
                return false
            }

            mark.done(PascalElementType.PROCEDURE_CALL_STATEMENT)
            return true
        }

        private fun parseActualParameterList() {
            val mark = builder.mark()

            assertAdvance(PascalTokenType.LPAREN)
            parseLoop("actual parameter", PascalTokenType.COMMA, "','", listOf(
                PascalTokenType.RPAREN,
                PascalTokenType.SEMI,
                PascalTokenType.END,
                PascalTokenType.PROCEDURE,
                PascalTokenType.USES)) {
                parseExpression()
            }
            expectAdvance(PascalTokenType.RPAREN, "')'")

            mark.done(PascalElementType.PROCEDURE_ACTUAL_PARAMETERS)
        }

        private fun tryParseAssignments(): Boolean {
            val mark = builder.mark()

            if (builder.tokenType == PascalTokenType.IDENTIFIER) {
                parseIdentifier()
            }
            else {
                mark.rollbackTo()
                return false
            }

            if (builder.tokenType in PascalTokenType.ASSIGNS) {
                parseAssignOperator()
            } else {
                mark.rollbackTo()
                return false
            }

            parseExpression()
            mark.done(PascalElementType.ASSIGNMENT_STATEMENT)
            return true
        }

        private fun parseAssignOperator() {
            val mark = builder.mark()
            when(builder.tokenType) {
                PascalTokenType.ASSIGN -> assertAdvance(PascalTokenType.ASSIGN)
                PascalTokenType.PLUS_ASSIGN -> assertAdvance(PascalTokenType.PLUS_ASSIGN)
                PascalTokenType.MINUS_ASSIGN -> assertAdvance(PascalTokenType.MINUS_ASSIGN)
                PascalTokenType.MULT_ASSIGN -> assertAdvance(PascalTokenType.MULT_ASSIGN)
                PascalTokenType.DIV_ASSIGN -> assertAdvance(PascalTokenType.DIV_ASSIGN)
                else -> errorAdvance("ASSIGNMENT_OPERATOR")
            }
            mark.done(PascalElementType.ASSIGNMENT_OPERATOR)
        }

        private fun tryParseDeclaration(): Boolean {
            if (builder.tokenType == PascalTokenType.VAR ||
                builder.tokenType == PascalTokenType.PROCEDURE) {
                parseDeclaration()
                return true
            }
            return false
        }

        private fun parseDeclaration() {
            val mark = builder.mark()
            when (builder.tokenType) {
                PascalTokenType.VAR -> parseVariables()
                PascalTokenType.PROCEDURE -> parseProcedure()
                else -> errorAdvance("'DECLARATION'")
            }
            mark.done(PascalElementType.DECLARATION)
        }

        private fun parseProcedure() {
            val mark = builder.mark()

            parseProcedureHeader()
            expectAdvance(PascalTokenType.SEMI, "';'")
            parseBlock()
            expectAdvance(PascalTokenType.SEMI, "';'")

            mark.done(PascalElementType.PROCEDURE)
        }

        private fun parseProcedureHeader() {
            val mark = builder.mark()

            assertAdvance(PascalTokenType.PROCEDURE)
            parseIdentifier()
            if (builder.tokenType == PascalTokenType.LPAREN) {
                parseParameterList(PascalElementType.PROCEDURE_PARAMETERS)
            }

            mark.done(PascalElementType.PROCEDURE_HEADER)
        }

        private fun parseVariables() {
            val mark = builder.mark()

            assertAdvance(PascalTokenType.VAR)
            parseLoop("variables", PascalTokenType.SEMI, "';'", listOf(
                PascalTokenType.BEGIN,
                PascalTokenType.RPAREN,
                PascalTokenType.END,
                PascalTokenType.PROCEDURE,
                PascalTokenType.USES)) {
                val variableMark = builder.mark()
                parseIdentifier()
                expectAdvance(PascalTokenType.COLON, "':'")
                parseType()
                if (builder.tokenType != PascalTokenType.SEMI) {
                    assertAdvance(PascalTokenType.EQ)
                    parseExpression()
                }
                variableMark.done(PascalElementType.VARIABLE_DECLARATION)
            }

            mark.done(PascalElementType.VARIABLES)
        }

        private fun parseExpression() {
            val mark = builder.mark()

            parseSimpleExpression()
            if (builder.tokenType in PascalTokenType.RELATIONAL_OPERATORS) {
                parseRelationalOperator()
                parseSimpleExpression()
            }

            mark.done(PascalElementType.EXPRESSION)
        }

        private fun parseRelationalOperator() {
            val mark = builder.mark()
            when(builder.tokenType) {
                PascalTokenType.EQ -> assertAdvance(PascalTokenType.EQ)
                PascalTokenType.NEQ -> assertAdvance(PascalTokenType.NEQ)
                PascalTokenType.GT -> assertAdvance(PascalTokenType.GT)
                PascalTokenType.LT -> assertAdvance(PascalTokenType.LT)
                PascalTokenType.GTEQ -> assertAdvance(PascalTokenType.GTEQ)
                PascalTokenType.LTEQ -> assertAdvance(PascalTokenType.LTEQ)
                PascalTokenType.IN -> assertAdvance(PascalTokenType.IN)
                else -> errorAdvance("RELATIONAL_OPERATOR")
            }
            mark.done(PascalElementType.RELATIONAL_OPERATOR)
        }

        private fun parseSimpleExpression() {
            val mark = builder.mark()

            parseTerm()
            while(builder.tokenType in PascalTokenType.ADDING_OPERATORS) {
                parseAddingOperator()
                parseTerm()
            }

            mark.done(PascalElementType.SIMPLE_EXPRESSION)
        }

        private fun parseAddingOperator() {
            val mark = builder.mark()
            when(builder.tokenType) {
                PascalTokenType.PLUS -> assertAdvance(PascalTokenType.PLUS)
                PascalTokenType.MINUS -> assertAdvance(PascalTokenType.MINUS)
                PascalTokenType.OR -> assertAdvance(PascalTokenType.OR)
                PascalTokenType.XOR -> assertAdvance(PascalTokenType.XOR)
                else -> errorAdvance("ADDING_OPERATOR")
            }
            mark.done(PascalElementType.ADDING_OPERATOR)
        }

        private fun parseTerm() {
            val mark = builder.mark()

            parseFactor()
            if (builder.tokenType in PascalTokenType.MULTIPLYING_OPERATORS) {
                parseMultiplyingOperator()
                parseFactor()
            }

            mark.done(PascalElementType.TERM)
        }

        private fun parseMultiplyingOperator() {
            val mark = builder.mark()
            when(builder.tokenType) {
                PascalTokenType.MULT -> assertAdvance(PascalTokenType.MULT)
                PascalTokenType.DIV -> assertAdvance(PascalTokenType.DIV)
                PascalTokenType.DDIV -> assertAdvance(PascalTokenType.DDIV)
                PascalTokenType.MOD -> assertAdvance(PascalTokenType.MOD)
                PascalTokenType.AND -> assertAdvance(PascalTokenType.AND)
                PascalTokenType.SHR -> assertAdvance(PascalTokenType.SHR)
                PascalTokenType.SHL -> assertAdvance(PascalTokenType.SHL)
                else -> errorAdvance("MULTIPLYING_OPERATORS")
            }
            mark.done(PascalElementType.MULTIPLYING_OPERATORS)
        }

        private fun parseFactor() {
            val mark = builder.mark()
            when(builder.tokenType) {
                PascalTokenType.LPAREN -> {
                    assertAdvance(PascalTokenType.LPAREN)
                    parseExpression()
                    expectAdvance(PascalTokenType.RPAREN, "')'")
                }
                PascalTokenType.IDENTIFIER -> assertAdvance(PascalTokenType.IDENTIFIER)
                PascalTokenType.NOT -> {
                    assertAdvance(PascalTokenType.NOT)
                    parseFactor()
                }
                PascalTokenType.PLUS -> {
                    assertAdvance(PascalTokenType.PLUS)
                    parseFactor()
                }
                PascalTokenType.MINUS -> {
                    assertAdvance(PascalTokenType.MINUS)
                    parseFactor()
                }
                else -> {
                    if(!tryParseUnsignedConstant()) {
                        errorAdvance("FACTOR")
                    }
                }
            }
            mark.done(PascalElementType.FACTOR)
        }

        private fun tryParseUnsignedConstant(): Boolean {
            if (builder.tokenType == PascalTokenType.NUM_INT ||
                builder.tokenType == PascalTokenType.NUM_REAL ||
                builder.tokenType == PascalTokenType.NUM_HEX ||
                builder.tokenType == PascalTokenType.STRING) {
                parseUnsignedConstant()
                return true
            }
            return false
        }

        private fun parseUnsignedConstant() {
            val mark = builder.mark()
            when(builder.tokenType) {
                PascalTokenType.NUM_INT -> assertAdvance(PascalTokenType.NUM_INT)
                PascalTokenType.NUM_REAL -> assertAdvance(PascalTokenType.NUM_REAL)
                PascalTokenType.NUM_HEX -> assertAdvance(PascalTokenType.NUM_HEX)
                PascalTokenType.STRING -> assertAdvance(PascalTokenType.STRING)
            }
            mark.done(PascalElementType.UNSIGNED_CONSTANT)
        }

        private fun parseType() {
            val mark = builder.mark()
            when(builder.tokenType) {
                PascalTokenType.INTEGER -> assertAdvance(PascalTokenType.INTEGER)
                PascalTokenType.REAL -> assertAdvance(PascalTokenType.REAL)
                PascalTokenType.BOOLEAN -> assertAdvance(PascalTokenType.BOOLEAN)
                PascalTokenType.CHAR -> assertAdvance(PascalTokenType.CHAR)
                else -> errorAdvance("TYPE")
            }
            mark.done(PascalElementType.TYPE)
        }

        private fun parseProgramHeader() {
            val mark = builder.mark()

            expectAdvance(PascalTokenType.PROGRAM, "'PROGRAM'")
            parseIdentifier()
            if (builder.tokenType == PascalTokenType.LPAREN) {
                val parameterMark = builder.mark()

                assertAdvance(PascalTokenType.LPAREN)
                parseLoop("parameter", PascalTokenType.COMMA, "','", listOf(
                    PascalTokenType.RPAREN,
                    PascalTokenType.SEMI,
                    PascalTokenType.END,
                    PascalTokenType.PROCEDURE,
                    PascalTokenType.USES)) {
                    parseIdentifier()
                }
                expectAdvance(PascalTokenType.RPAREN, "')'")

                parameterMark.done(PascalElementType.PROGRAM_PARAMETERS)
            }

            mark.done(PascalElementType.PROGRAM_HEADER)
        }

        private fun parseUsesClause() {
            val mark = builder.mark()

            assertAdvance(PascalTokenType.USES)
            parseLoop("uses", PascalTokenType.COMMA, "','", listOf(
                PascalTokenType.SEMI,
                PascalTokenType.BEGIN,
                PascalTokenType.END,
                PascalTokenType.PROCEDURE,
                PascalTokenType.USES)) {
                val useMark = builder.mark()
                parseIdentifier()
                if (builder.tokenType == PascalTokenType.IN) {
                    expectAdvance(PascalTokenType.IN, "'IN'")
                    expectAdvance(PascalTokenType.STRING, "'STRING'")
                }
                useMark.done(PascalElementType.USES_ENTRY)
            }
            expectAdvance(PascalTokenType.SEMI, "';'")

            mark.done(PascalElementType.USES_CLAUSE)
        }

        private fun parseIdentifier() {
            val mark = builder.mark()
            assertAdvance(PascalTokenType.IDENTIFIER)
            mark.done(PascalElementType.IDENTIFIER)
        }

        private fun parseParameterList(parametersKind: PascalElementType) {
            val mark = builder.mark()

            assertAdvance(PascalTokenType.LPAREN)
            parseLoop("parameter", PascalTokenType.COMMA, "','", listOf(
                PascalTokenType.RPAREN,
                PascalTokenType.SEMI,
                PascalTokenType.END,
                PascalTokenType.PROCEDURE,
                PascalTokenType.USES)) {
                parseIdentifierWithType()
            }
            expectAdvance(PascalTokenType.RPAREN, "')'")

            mark.done(parametersKind)
        }

        private fun parseIdentifierWithType() {
            val mark = builder.mark()
            parseIdentifier()
            expectAdvance(PascalTokenType.COLON, "':'")
            parseType()
            mark.done(PascalElementType.IDENTIFIER_WITH_TYPE)
        }

        private fun parseLoop(itemName: String,
                              separator: PascalTokenType?,
                              separatorName: String?,
                              stopper: List<PascalTokenType>,
                              itemParser: () -> Unit) {
            assert(separator == null && separatorName == null || separator != null && separatorName != null)

            while (builder.tokenType != null && builder.tokenType !in stopper) {
                val offsetBeforeBody = builder.currentOffset
                itemParser()

                if (builder.currentOffset == offsetBeforeBody) {
                    errorAdvance(itemName)
                    continue
                }

                if (separator == null)
                    continue
                else if (builder.tokenType == separator) {
                    assertAdvance(separator)
                    continue
                }
                else if (builder.tokenType in stopper) {
                    break
                }
                else {
                    builder.error("Expected $separatorName")
                    continue
                }
            }
        }

        private fun advance(): IElementType? {
            val result = builder.tokenType
            builder.advanceLexer()
            while (builder.tokenType == TokenType.BAD_CHARACTER) {
                val badMark = builder.mark()
                builder.advanceLexer()
                badMark.error("Unexpected character")
            }
            return result
        }

        private fun errorAdvance(expectedName: String) {
            val mark = builder.mark()
            advance()
            mark.error("Expected $expectedName")
        }

        private fun expectAdvance(expectedTt: PascalTokenType, expectedName: String): Boolean {
            return if (builder.tokenType == expectedTt) {
                advance()
                true
            } else {
                builder.error("Expected $expectedName")
                false
            }
        }

        private fun assertAdvance(tt: IElementType) {
            assert(builder.tokenType == tt)
            advance()
        }

        private fun parseGarbage() {
            val mark = builder.mark()
            builder.error("Garbage at the end of file")

            while (builder.tokenType != null) {
                advance()
            }

            mark.done(PascalElementType.GARBAGE_AT_THE_END_OF_FILE)
        }
    }
}

class PascalParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer {
        return PascalLexer()
    }

    override fun createParser(project: Project?): PsiParser {
        return PascalParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return PascalElementType.PASCAL_STUB_FILE
    }

    override fun getCommentTokens(): TokenSet {
        return PascalTokenType.COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return PascalTokenType.STRINGS
    }

    override fun createElement(node: ASTNode): PsiElement {
        return PascalElementType.createElement(node)
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return PascalFile(viewProvider)
    }
}