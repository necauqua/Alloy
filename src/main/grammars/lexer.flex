package dev.necauqua.plugins.alloy;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import com.intellij.lexer.FlexLexer;
import dev.necauqua.plugins.alloy.psi.Types;

%%

%{
  public AlloyLexer() {
    this(null);
  }
%}


%public
%class AlloyLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

// allow any usable combination of linebreaks
EOL         = \n | \r\n | \r

WHITE_SPACE = {EOL} | [ \t]

BLOCK_COMMENT = "/*" ~"*/"
C_LINE_COMMENT = "//" [^\n\r]* {EOL}?
LINE_COMMENT = "--" [^\n\r]* {EOL}?

NAME = [a-zA-Z$%?!_'\"] [a-zA-Z0-9$%?!_'\"]*

INTEGER = (0 | [1-9][0-9]*)

%%

<YYINITIAL> {
    "abstract"       { return Types.K_ABSTRACT; }
    "all"            { return Types.K_ALL; }
    "and"            { return Types.K_AND; }
    "as"             { return Types.K_AS; }
    "assert"         { return Types.K_ASSERT; }
    "but"            { return Types.K_BUT; }
    "check"          { return Types.K_CHECK; }
    "disj"           { return Types.K_DISJ; }
    "disjoint"       { return Types.K_DISJOINT; }
    "else"           { return Types.K_ELSE; }
    "enum"           { return Types.K_ENUM; }
    "exactly"        { return Types.K_EXACTLY; }
    "exh"            { return Types.K_EXH; }
    "exhaustive"     { return Types.K_EXHAUSTIVE; }
    "expect"         { return Types.K_EXPECT; }
    "extends"        { return Types.K_EXTENDS; }
    "fact"           { return Types.K_FACT; }
    "for"            { return Types.K_FOR; }
    "fun"            { return Types.K_FUN; }
    "iden"           { return Types.K_IDEN; }
    "iff"            { return Types.K_IFF; }
    "implies"        { return Types.K_IMPLIES; }
    "in"             { return Types.K_IN; }
    "int_c"          { return Types.K_INT_C; }
    "int"            { return Types.K_INT; }
    "let"            { return Types.K_LET; }
    "lone"           { return Types.K_LONE; }
    "module"         { return Types.K_MODULE; }
    "no"             { return Types.K_NO; }
    "none"           { return Types.K_NONE; }
    "not"            { return Types.K_NOT; }
    "one"            { return Types.K_ONE; }
    "open"           { return Types.K_OPEN; }
    "or"             { return Types.K_OR; }
    "part"           { return Types.K_PART; }
    "partition"      { return Types.K_PARTITION; }
    "pred"           { return Types.K_PRED; }
    "private"        { return Types.K_PRIVATE; }
    "run"            { return Types.K_RUN; }
    "seq"            { return Types.K_SEQ; }
    "set"            { return Types.K_SET; }
    "sig"            { return Types.K_SIG; }
    "some"           { return Types.K_SOME; }
    "string"         { return Types.K_STRING; }
    "sum"            { return Types.K_SUM; }
    "this"           { return Types.K_THIS; }
    "univ"           { return Types.K_UNIV; }

    // Alloy Language Specification, appendix B:
    // The following sequences of characters should be recognized as single tokens:
    "=>"             { return Types.FAT_ARROW; }
    ">="             { return Types.GE; }
    "<="             { return Types.LE; }
    "->"             { return Types.ARROW; }
    "<:"             { return Types.LRESTRICT; }
    ":>"             { return Types.RRESTRICT; }
    "++"             { return Types.REL_OVERRIDE; }
    "&&"             { return Types.AND; }
    "||"             { return Types.OR; }

    "/"              { return Types.NAME_SEP; }
    ":"              { return Types.COLON; }
    "."              { return Types.DOT; }
    ","              { return Types.COMMA; }
    "{"              { return Types.LBRACE; }
    "}"              { return Types.RBRACE; }
    "["              { return Types.LBRACKET; }
    "]"              { return Types.RBRACKET; }
    "("              { return Types.LPAREN; }
    ")"              { return Types.RPAREN; }
    "@"              { return Types.AT; }
    "!"              { return Types.NOT_OP; }
    "#"              { return Types.SHARP; }
    "~"              { return Types.TILDE; }
    "*"              { return Types.MUL; }
    "+"              { return Types.PLUS; }
    "-"              { return Types.MINUS; }
    "^"              { return Types.HAT; }
    "<"              { return Types.LT; }
    ">"              { return Types.GT; }
    "="              { return Types.EQ; }
    "|"              { return Types.BAR; }
    "&"              { return Types.JOIN; }

    {NAME}          { return Types.NAME; }
    {INTEGER}        { return Types.INTEGER; }

    {BLOCK_COMMENT}  { return Types.BLOCK_COMMENT; }
    {C_LINE_COMMENT} { return Types.C_LINE_COMMENT; }
    {LINE_COMMENT}   { return Types.LINE_COMMENT; }

    {WHITE_SPACE}+   { return TokenType.WHITE_SPACE; }
}


[^] { return TokenType.BAD_CHARACTER; }
