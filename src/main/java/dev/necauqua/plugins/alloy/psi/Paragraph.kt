package dev.necauqua.plugins.alloy.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import java.lang.UnsupportedOperationException

abstract class Paragraph(node: ASTNode): ASTWrapperPsiElement(node) {

    fun getBlock(): List<PsiElement> {
        return when (this) {
            is SigDecl -> declList
            is FactDecl -> listOf(blockExpr)
            is PredDecl -> listOf(blockExpr)
            is FunDecl -> exprList
            is AssertDecl -> listOf(blockExpr)
            is CmdDecl -> blockExpr?.let(::listOf) ?: emptyList()
            else -> throw UnsupportedOperationException("idk")
        }
    }
}
