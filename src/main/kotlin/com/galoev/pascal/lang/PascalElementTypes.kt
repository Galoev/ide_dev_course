package com.galoev.pascal.lang

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import org.jetbrains.annotations.NonNls

class PascalElementType(@NonNls debugName: String) : IElementType(debugName, PascalLanguage) {
    companion object {
        val PASCAL_FILE = IFileElementType(PascalLanguage)
        val PASCAL_STUB_FILE = PascalStubFileElementType()

        val PROGRAM_HEADER = PascalElementType("PROGRAM_HEADER")
        val IDENTIFIER = PascalElementType("IDENTIFIER")
        val PROGRAM_PARAMETERS = PascalElementType("PROGRAM_PARAMETERS")
        val USES_CLAUSE = PascalElementType("USES_CLAUSE")
        val USES_ENTRY = PascalElementType("USES_ENTRY")
        val BLOCK = PascalElementType("BLOCK")
        val DECLARATION = PascalElementType("DECLARATION")
        val VARIABLES = PascalElementType("VARIABLES")
        val VARIABLE_DECLARATION = PascalElementType("VARIABLE_DECLARATION")
        val TYPE = PascalElementType("TYPE")
        val EXPRESSION = PascalElementType("EXPRESSION")
        val SIMPLE_EXPRESSION = PascalElementType("SIMPLE_EXPRESSION")
        val RELATIONAL_OPERATOR = PascalElementType("RELATIONAL_OPERATOR")
        val TERM = PascalElementType("TERM")
        val ADDING_OPERATOR = PascalElementType("ADDING_OPERATOR")
        val FACTOR = PascalElementType("FACTOR")
        val MULTIPLYING_OPERATORS = PascalElementType("MULTIPLYING_OPERATORS")
        val UNSIGNED_CONSTANT = PascalElementType("UNSIGNED_CONSTANT")
        val PROCEDURE = PascalElementType("PROCEDURE")
        val PROCEDURE_HEADER = PascalElementType("PROCEDURE_HEADER")
        val PROCEDURE_PARAMETERS = PascalElementType("PROCEDURE_PARAMETERS")
        val IDENTIFIER_WITH_TYPE = PascalElementType("IDENTIFIER_WITH_TYPE")
        val COMPOUND_STATEMENT = PascalElementType("COMPOUND_STATEMENT")
        val ASSIGNMENT_STATEMENT = PascalElementType("ASSIGNMENT_STATEMENT")
        val ASSIGNMENT_OPERATOR = PascalElementType("ASSIGNMENT_OPERATOR")
        val PROCEDURE_CALL_STATEMENT = PascalElementType("PROCEDURE_CALL_STATEMENT")
        val PROCEDURE_ACTUAL_PARAMETERS = PascalElementType("PROCEDURE_ACTUAL_PARAMETERS")
        val STRUCTURED_STATEMENT = PascalElementType("STRUCTURED_STATEMENT")
        val CASE_STATEMENT = PascalElementType("CASE_STATEMENT")
        val CASE_ENTRY = PascalElementType("CASE_ENTRY")
        val CASE_CONSTANT = PascalElementType("CASE_CONSTANT")
        val CONSTANT = PascalElementType("CONSTANT")
        val ELSE = PascalElementType("ELSE")
        val IF_STATEMENT = PascalElementType("IF_STATEMENT")
        val FOR_STATEMENT = PascalElementType("FOR_STATEMENT")
        val REPEAT_STATEMENT = PascalElementType("REPEAT_STATEMENT")
        val WHILE_STATEMENT = PascalElementType("WHILE_STATEMENT")
        val GARBAGE_AT_THE_END_OF_FILE = PascalElementType("GARBAGE_AT_THE_END_OF_FILE")

        fun createElement(node: ASTNode): PsiElement {
            return PascalSimpleNode(node)
        }
    }
}