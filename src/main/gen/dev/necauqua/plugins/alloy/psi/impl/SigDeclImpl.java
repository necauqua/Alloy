// This is a generated file. Not intended for manual editing.
package dev.necauqua.plugins.alloy.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static dev.necauqua.plugins.alloy.psi.Types.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import dev.necauqua.plugins.alloy.psi.*;

public class SigDeclImpl extends ASTWrapperPsiElement implements SigDecl {

  public SigDeclImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitSigDecl(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

}
