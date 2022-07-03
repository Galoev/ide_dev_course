package com.galoev.pascal.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.galoev.pascal.lang.PascalTokenType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
%%

%public
%class _PascalLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode


DEC_DIGIT = [0-9]+
NUM_INT = "-"? {DEC_DIGIT}
NUM_REAL = {DEC_DIGIT}(\.{DEC_DIGIT})?
NUM_HEX = \$[0-9a-fA-F]+

LINE_WS = [\ \t\f]+
EOL = "\r"|"\n"|"\r\n"
WHITE_SPACE=({LINE_WS}|{EOL})+

STRING_LITERAL = (\'([^\'\r\\]|\\.)*\')

LINE_COMMENT    = "/""/"[^\r\n]*
BLOCK_COMMENT   = "(*" !([^]* "*)" [^]*) ("*)")?
BRACE_COMMENT   = "{" !([^]* "}" [^]*) ("}")?
COMMENT         = {LINE_COMMENT}|{BLOCK_COMMENT}|{BRACE_COMMENT}

IDENTIFIER      = [_a-zA-Z][_a-zA-Z0-9]{0,126}
//ALPHA_NUM = [a-zA-Z0-9_]
//IDENTIFIER = [a-z] {ALPHA_NUM}*
%%
"program"         { return PascalTokenType.Companion.getPROGRAM(); }
"begin"         { return PascalTokenType.Companion.getBEGIN(); }
"end"         { return PascalTokenType.Companion.getEND(); }
"case"         { return PascalTokenType.Companion.getCASE(); }
"of"         { return PascalTokenType.Companion.getOF(); }
"if"         { return PascalTokenType.Companion.getIF(); }
"then"         { return PascalTokenType.Companion.getTHEN(); }
"else"         { return PascalTokenType.Companion.getELSE(); }
"for"         { return PascalTokenType.Companion.getFOR(); }
"to"         { return PascalTokenType.Companion.getTO(); }
"downto"         { return PascalTokenType.Companion.getDOWNTO(); }
"do"         { return PascalTokenType.Companion.getDO(); }
"in"         { return PascalTokenType.Companion.getIN(); }
"while"         { return PascalTokenType.Companion.getWHILE(); }
"repeat"         { return PascalTokenType.Companion.getREPEAT(); }
"until"         { return PascalTokenType.Companion.getUNTIL(); }
"uses"         { return PascalTokenType.Companion.getUSES(); }
"procedure"         { return PascalTokenType.Companion.getPROCEDURE(); }
"var"         { return PascalTokenType.Companion.getVAR(); }

":="         { return PascalTokenType.Companion.getASSIGN(); }
"+="         { return PascalTokenType.Companion.getPLUS_ASSIGN(); }
"-="         { return PascalTokenType.Companion.getMINUS_ASSIGN(); }
"*="         { return PascalTokenType.Companion.getMULT_ASSIGN(); }
"/="         { return PascalTokenType.Companion.getDIV_ASSIGN(); }

"+"         { return PascalTokenType.Companion.getPLUS(); }
"-"         { return PascalTokenType.Companion.getMINUS(); }
"*"         { return PascalTokenType.Companion.getMULT(); }
"/"         { return PascalTokenType.Companion.getDIV(); }

"="         { return PascalTokenType.Companion.getEQ(); }
"<>"         { return PascalTokenType.Companion.getNEQ(); }
">"         { return PascalTokenType.Companion.getGT(); }
"<"         { return PascalTokenType.Companion.getLT(); }
">="         { return PascalTokenType.Companion.getGTEQ(); }
"<="         { return PascalTokenType.Companion.getLTEQ(); }

"and"         { return PascalTokenType.Companion.getAND(); }
"or"         { return PascalTokenType.Companion.getOR(); }
"not"         { return PascalTokenType.Companion.getNOT(); }
"xor"         { return PascalTokenType.Companion.getXOR(); }
"div"         { return PascalTokenType.Companion.getDDIV(); }
"mod"         { return PascalTokenType.Companion.getMOD(); }
"shr"         { return PascalTokenType.Companion.getSHR(); }
"shl"         { return PascalTokenType.Companion.getSHL(); }

";"         { return PascalTokenType.Companion.getSEMI(); }
":"         { return PascalTokenType.Companion.getCOLON(); }
","         { return PascalTokenType.Companion.getCOMMA(); }
"."         { return PascalTokenType.Companion.getDOT(); }
"("         { return PascalTokenType.Companion.getLPAREN(); }
")"         { return PascalTokenType.Companion.getRPAREN(); }

"integer"         { return PascalTokenType.Companion.getINTEGER(); }
"boolean"         { return PascalTokenType.Companion.getBOOLEAN(); }
"char"         { return PascalTokenType.Companion.getCHAR(); }
"real"         { return PascalTokenType.Companion.getREAL(); }
"true"         { return PascalTokenType.Companion.getTRUE(); }
"false"         { return PascalTokenType.Companion.getFALSE(); }

{WHITE_SPACE}    { return WHITE_SPACE; }
{STRING_LITERAL}         { return PascalTokenType.Companion.getSTRING(); }
{COMMENT}         { return PascalTokenType.Companion.getCOMMENT(); }
{NUM_INT}         { return PascalTokenType.Companion.getNUM_INT(); }
{NUM_REAL}         { return PascalTokenType.Companion.getNUM_REAL(); }
{NUM_HEX}         { return PascalTokenType.Companion.getNUM_HEX(); }
{IDENTIFIER}         { return PascalTokenType.Companion.getIDENTIFIER(); }


[^] { return BAD_CHARACTER; }
