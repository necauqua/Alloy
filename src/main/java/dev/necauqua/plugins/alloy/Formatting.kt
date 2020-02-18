package dev.necauqua.plugins.alloy

import com.intellij.formatting.*
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.WrapType.NONE
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import dev.necauqua.plugins.alloy.psi.*

class BasicBlock(
    node: ASTNode, wrap: Wrap?, alignment: Alignment?
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> {
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode
        while (child != null) {
            val psi = child.psi
            if (psi is Decl || psi is Expr || psi.parent is PsiBlock) {
                blocks += BlockBlock(
                    child, Wrap.createWrap(NONE, false), Alignment.createAlignment()
                )
            } else if (child.elementType !== WHITE_SPACE) {
                blocks += BasicBlock(
                    child, Wrap.createWrap(NONE, false), Alignment.createAlignment()
                )
            }
            child = child.treeNext
        }
        return blocks
    }


    override fun getIndent(): Indent? = Indent.getNoneIndent()

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = null

    override fun isLeaf(): Boolean = true
}

class BlockBlock(
    node: ASTNode, wrap: Wrap?, alignment: Alignment?
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> = emptyList()

    override fun getIndent(): Indent? = Indent.getNormalIndent(true)

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = null

    override fun isLeaf(): Boolean = true
}

class FormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
        return FormattingModelProvider
            .createFormattingModelForPsiFile(
                element.containingFile,
                BasicBlock(
                    element.node,
                    Wrap.createWrap(NONE, false),
                    Alignment.createAlignment()
                ),
                settings
            )
    }

    override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? = null
}
