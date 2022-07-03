package com.galoev.pascal.ide

import com.galoev.pascal.lang.PascalFileType
import com.galoev.pascal.lang.PascalLanguage
import com.galoev.pascal.lang.PascalLexer
import com.galoev.pascal.lang.PascalTokenType
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.tree.IElementType

enum class PascalTextAttributeKeys(humanName: String, fallback: TextAttributesKey) {
    COMMENT("Comment", DefaultLanguageHighlighterColors.DOC_COMMENT),
    STRING("String", DefaultLanguageHighlighterColors.STRING),
    NUMBERS("Number", DefaultLanguageHighlighterColors.NUMBER),
    KEYWORD("Keyword", DefaultLanguageHighlighterColors.KEYWORD),

    DOT("Dot", DefaultLanguageHighlighterColors.DOT),
    COMMA("Comma", DefaultLanguageHighlighterColors.COMMA),
    SEMICOLON("Semicolon", DefaultLanguageHighlighterColors.SEMICOLON),
    COLON("Colon", DefaultLanguageHighlighterColors.SEMICOLON),
    OPERATORS("Operators", DefaultLanguageHighlighterColors.OPERATION_SIGN),

    PARENTHESES("Parentheses", DefaultLanguageHighlighterColors.PARENTHESES),

    ASSIGNMENTS("Signs", DefaultLanguageHighlighterColors.COMMA),
    TYPES("Types", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL),
    IDENTIFIER("Library", DefaultLanguageHighlighterColors.IDENTIFIER);

    val key = TextAttributesKey.createTextAttributesKey("Pascal.$name", fallback)
    val descriptor = AttributesDescriptor(humanName, key)
}

class PascalColorSettingsPage : ColorSettingsPage {
    override fun getDisplayName() = PascalLanguage.displayName
    override fun getIcon() = PascalFileType.icon
    override fun getAttributeDescriptors() = PascalTextAttributeKeys.values().map { it.descriptor }.toTypedArray()
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getHighlighter() = PascalSyntaxHighlighter()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey> {
        return PascalTextAttributeKeys.values().associateBy({ it.name }, { it.key })
    }

    private val DEMO_TEXT = CodeStyleAbstractPanel.readFromFile(PascalLanguage::class.java, "Pascal.pas")
    override fun getDemoText(): String = DEMO_TEXT
}

class PascalSyntaxHighlighter : SyntaxHighlighterBase() {
    companion object {
        val keys1 = HashMap<IElementType, TextAttributesKey>()

        init
        {
            fillMap(keys1, PascalTokenType.KEYWORDS, PascalTextAttributeKeys.KEYWORD.key)
            fillMap(keys1, PascalTokenType.ASSIGNS, PascalTextAttributeKeys.ASSIGNMENTS.key)
            fillMap(keys1, PascalTokenType.OPERATORS, PascalTextAttributeKeys.OPERATORS.key)
            fillMap(keys1, PascalTokenType.TYPES, PascalTextAttributeKeys.TYPES.key)
            fillMap(keys1, PascalTokenType.NUMBERS, PascalTextAttributeKeys.NUMBERS.key)
            fillMap(keys1, PascalTokenType.PARENS, PascalTextAttributeKeys.PARENTHESES.key)

            keys1[PascalTokenType.SEMI] = PascalTextAttributeKeys.SEMICOLON.key
            keys1[PascalTokenType.COLON] = PascalTextAttributeKeys.COLON.key
            keys1[PascalTokenType.COMMA] = PascalTextAttributeKeys.COMMA.key
            keys1[PascalTokenType.DOT] = PascalTextAttributeKeys.DOT.key

            keys1[PascalTokenType.COMMENT] = PascalTextAttributeKeys.COMMENT.key
            keys1[PascalTokenType.IDENTIFIER] = PascalTextAttributeKeys.IDENTIFIER.key
            keys1[PascalTokenType.STRING] = PascalTextAttributeKeys.STRING.key
        }
    }

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        return pack(keys1[tokenType])
    }

    override fun getHighlightingLexer(): Lexer {
        return PascalLexer()
    }
}

class PascalSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        return PascalSyntaxHighlighter()
    }
}