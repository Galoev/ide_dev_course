package com.galoev.pascal.lang

import com.intellij.testFramework.ParsingTestCase

class PascalParserTest : ParsingTestCase("parser", "pas", true, PascalParserDefinition()) {
    fun testSimpleTest() = doTest(true)
    fun testUsesClause() = doTest(true)
    fun testVariableDeclaration() = doTest(true)
    fun testProcedureDeclaration() = doTest(true)
    fun testAssignmentStatements() = doTest(true)
    fun testProcedureStatements() = doTest(true)
    fun testCaseStatements() = doTest(true)
    fun testIfStatements() = doTest(true)
    fun testForStatements() = doTest(true)
    fun testRepeatStatements() = doTest(true)
    fun testWhileStatements() = doTest(true)


    override fun getTestDataPath(): String {
        return "src/test/data"
    }
}