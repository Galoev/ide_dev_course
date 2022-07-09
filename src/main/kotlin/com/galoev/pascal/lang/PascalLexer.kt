package com.galoev.pascal.lang

import com.galoev.pascal.lexer._PascalLexer
import com.intellij.lexer.FlexAdapter

class PascalLexer : FlexAdapter(_PascalLexer(null))