// This is a generated file. Not intended for manual editing.
package dev.necauqua.plugins.alloy.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import dev.necauqua.plugins.alloy.ElementType;
import dev.necauqua.plugins.alloy.TokenType;
import dev.necauqua.plugins.alloy.psi.impl.*;

public interface Types {

  IElementType SIG_DECL = new ElementType("SIG_DECL");

  IElementType ARROW = new TokenType("ARROW");
  IElementType COMMA = new TokenType("COMMA");
  IElementType COMMENT = new TokenType("COMMENT");
  IElementType INTEGER = new TokenType("INTEGER");
  IElementType K_ABSTRACT = new TokenType("K_ABSTRACT");
  IElementType K_ALL = new TokenType("K_ALL");
  IElementType K_AND = new TokenType("K_AND");
  IElementType K_AS = new TokenType("K_AS");
  IElementType K_ASSERT = new TokenType("K_ASSERT");
  IElementType K_BUT = new TokenType("K_BUT");
  IElementType K_CHECK = new TokenType("K_CHECK");
  IElementType K_DISJ = new TokenType("K_DISJ");
  IElementType K_DISJOINT = new TokenType("K_DISJOINT");
  IElementType K_ELSE = new TokenType("K_ELSE");
  IElementType K_ENUM = new TokenType("K_ENUM");
  IElementType K_EXACTLY = new TokenType("K_EXACTLY");
  IElementType K_EXH = new TokenType("K_EXH");
  IElementType K_EXHAUSTIVE = new TokenType("K_EXHAUSTIVE");
  IElementType K_EXPECT = new TokenType("K_EXPECT");
  IElementType K_EXTENDS = new TokenType("K_EXTENDS");
  IElementType K_FACT = new TokenType("K_FACT");
  IElementType K_FOR = new TokenType("K_FOR");
  IElementType K_FUN = new TokenType("K_FUN");
  IElementType K_IDEN = new TokenType("K_IDEN");
  IElementType K_IFF = new TokenType("K_IFF");
  IElementType K_IMPLIES = new TokenType("K_IMPLIES");
  IElementType K_IN = new TokenType("K_IN");
  IElementType K_INT = new TokenType("K_INT");
  IElementType K_INT_C = new TokenType("K_INT_C");
  IElementType K_LET = new TokenType("K_LET");
  IElementType K_LONE = new TokenType("K_LONE");
  IElementType K_MODULE = new TokenType("K_MODULE");
  IElementType K_NO = new TokenType("K_NO");
  IElementType K_NONE = new TokenType("K_NONE");
  IElementType K_NOT = new TokenType("K_NOT");
  IElementType K_ONE = new TokenType("K_ONE");
  IElementType K_OPEN = new TokenType("K_OPEN");
  IElementType K_OR = new TokenType("K_OR");
  IElementType K_PART = new TokenType("K_PART");
  IElementType K_PARTITION = new TokenType("K_PARTITION");
  IElementType K_PRED = new TokenType("K_PRED");
  IElementType K_PRIVATE = new TokenType("K_PRIVATE");
  IElementType K_RUN = new TokenType("K_RUN");
  IElementType K_SEQ = new TokenType("K_SEQ");
  IElementType K_SET = new TokenType("K_SET");
  IElementType K_SIG = new TokenType("K_SIG");
  IElementType K_SOME = new TokenType("K_SOME");
  IElementType K_STRING = new TokenType("K_STRING");
  IElementType K_SUM = new TokenType("K_SUM");
  IElementType K_THIS = new TokenType("K_THIS");
  IElementType K_UNIV = new TokenType("K_UNIV");
  IElementType LBRACE = new TokenType("LBRACE");
  IElementType LBRACKET = new TokenType("LBRACKET");
  IElementType NAME = new TokenType("NAME");
  IElementType PLUS = new TokenType("PLUS");
  IElementType QUAL_NAME = new TokenType("QUAL_NAME");
  IElementType RBRACE = new TokenType("RBRACE");
  IElementType RBRACKET = new TokenType("RBRACKET");
  IElementType WHITESPACE = new TokenType("WHITESPACE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == SIG_DECL) {
        return new SigDeclImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
