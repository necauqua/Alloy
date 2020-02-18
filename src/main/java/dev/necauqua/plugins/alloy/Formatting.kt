package dev.necauqua.plugins.alloy

import com.intellij.formatting.*
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.WrapType.NONE
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import dev.necauqua.plugins.alloy.psi.PsiBlock

class ScanBlock(node: ASTNode) : AbstractBlock(node, Wrap.createWrap(NONE, false), Alignment.createAlignment()) {

    override fun buildChildren(): List<Block> {
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode
        while (child != null) {
            if (child.elementType != WHITE_SPACE) {
                blocks += if (child.psi.parent is PsiBlock && child.treeNext != null && child.treePrev != null) {
                    IndentBlock(child)
                } else ScanBlock(child)
            }
            child = child.treeNext
        }
        return blocks
    }

    override fun getIndent(): Indent? = Indent.getNoneIndent()

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = null

    override fun isLeaf(): Boolean = true
}

class IndentBlock(node: ASTNode) : AbstractBlock(node, Wrap.createWrap(NONE, false), Alignment.createAlignment()) {

    override fun buildChildren(): List<Block> = emptyList()

    override fun getIndent(): Indent? = Indent.getNormalIndent()

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = null

    override fun isLeaf(): Boolean = true
}

class FormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel =
            FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile, ScanBlock(element.node), settings)
}
