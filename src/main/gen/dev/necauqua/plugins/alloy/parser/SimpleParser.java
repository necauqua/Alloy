// This is a generated file. Not intended for manual editing.
package dev.necauqua.plugins.alloy.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static dev.necauqua.plugins.alloy.psi.Types.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class SimpleParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return simpleFile(b, l + 1);
  }

  /* ********************************************************** */
  // sigDecl|COMMENT|WHITESPACE
  static boolean item_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_")) return false;
    boolean r;
    r = sigDecl(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    if (!r) r = consumeToken(b, WHITESPACE);
    return r;
  }

  /* ********************************************************** */
  // K_ABSTRACT? (K_LONE | K_SOME | K_ONE)? K_SIG ((NAME COMMA)* NAME) (K_EXTENDS QUAL_NAME | K_IN NAME (PLUS QUAL_NAME)*)? LBRACE /*sigBody*/ RBRACE
  public static boolean sigDecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sigDecl")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SIG_DECL, "<sig decl>");
    r = sigDecl_0(b, l + 1);
    r = r && sigDecl_1(b, l + 1);
    r = r && consumeToken(b, K_SIG);
    r = r && sigDecl_3(b, l + 1);
    r = r && sigDecl_4(b, l + 1);
    r = r && consumeTokens(b, 0, LBRACE, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // K_ABSTRACT?
  private static boolean sigDecl_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sigDecl_0")) return false;
    consumeToken(b, K_ABSTRACT);
    return true;
  }

  // (K_LONE | K_SOME | K_ONE)?
  private static boolean sigDecl_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sigDecl_1")) return false;
    sigDecl_1_0(b, l + 1);
    return true;
  }

  // K_LONE | K_SOME | K_ONE
  private static boolean sigDecl_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sigDecl_1_0")) return false;
    boolean r;
    r = consumeToken(b, K_LONE);
    if (!r) r = consumeToken(b, K_SOME);
    if (!r) r = consumeToken(b, K_ONE);
    return r;
  }

  // (NAME COMMA)* NAME
  private static boolean sigDecl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sigDecl_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sigDecl_3_0(b, l + 1);
    r = r && consumeToken(b, NAME);
    exit_section_(b, m, null, r);
    return r;
  }

  // (NAME COMMA)*
  private static boolean sigDecl_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sigDecl_3_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!sigDecl_3_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "sigDecl_3_0", c)) break;
    }
    return true;
  }

  // NAME COMMA
  private static boolean sigDecl_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sigDecl_3_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, NAME, COMMA);
    exit_section_(b, m, null, r);
    return r;
  }

  // (K_EXTENDS QUAL_NAME | K_IN NAME (PLUS QUAL_NAME)*)?
  private static boolean sigDecl_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sigDecl_4")) return false;
    sigDecl_4_0(b, l + 1);
    return true;
  }

  // K_EXTENDS QUAL_NAME | K_IN NAME (PLUS QUAL_NAME)*
  private static boolean sigDecl_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sigDecl_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, K_EXTENDS, QUAL_NAME);
    if (!r) r = sigDecl_4_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // K_IN NAME (PLUS QUAL_NAME)*
  private static boolean sigDecl_4_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sigDecl_4_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, K_IN, NAME);
    r = r && sigDecl_4_0_1_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (PLUS QUAL_NAME)*
  private static boolean sigDecl_4_0_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sigDecl_4_0_1_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!sigDecl_4_0_1_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "sigDecl_4_0_1_2", c)) break;
    }
    return true;
  }

  // PLUS QUAL_NAME
  private static boolean sigDecl_4_0_1_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sigDecl_4_0_1_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, PLUS, QUAL_NAME);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // item_*
  static boolean simpleFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simpleFile")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "simpleFile", c)) break;
    }
    return true;
  }

}
