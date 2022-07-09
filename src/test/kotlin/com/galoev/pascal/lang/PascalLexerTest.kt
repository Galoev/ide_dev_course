package com.galoev.pascal.lang

import com.intellij.lexer.Lexer
import com.intellij.testFramework.LexerTestCase
import com.intellij.testFramework.ParsingTestCase.loadFileDefault

class PascalLexerTest : LexerTestCase() {
    fun testSimpleTest() = doTest(loadFileDefault(dirPath, "simpleTest.pas"))
    fun testCommentsTest() = doTest(loadFileDefault(dirPath, "commentsTest.pas"))
    fun testIdentifiersTest() = doTest(loadFileDefault(dirPath, "identifiersTest.pas"))
    fun testNumbersTest() = doTest(loadFileDefault(dirPath, "numbersTest.pas"))
    fun testStringsTest() = doTest(loadFileDefault(dirPath, "stringsTest.pas"))

    override fun createLexer(): Lexer {
        return PascalLexer()
    }

    override fun getDirPath(): String {
        return "src/test/data/lexer"
    }

    override fun getPathToTestDataFile(extension: String): String {
        return dirPath + "/" + getTestName(true) + extension
    }
}