package dev.necauqua.plugins.alloy.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import java.lang.UnsupportedOperationException

abstract class Paragraph(node: ASTNode): ASTWrapperPsiElement(node) {

}
