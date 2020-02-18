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
import dev.necauqua.plugins.alloy.psi.Types

class SimpleBlock constructor(
    node: ASTNode, wrap: Wrap?, alignment: Alignment?,
    private val spacingBuilder: SpacingBuilder
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> {
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode
        while (child != null) {
            if (child.elementType !== WHITE_SPACE) {
                val block: Block = SimpleBlock(
                    child, Wrap.createWrap(NONE, false), Alignment.createAlignment(),
                    spacingBuilder
                )
                blocks.add(block)
            }
            child = child.treeNext
        }
        return blocks
    }

    override fun getIndent(): Indent? = Indent.getNoneIndent()

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = spacingBuilder.getSpacing(this, child1, child2)

    override fun isLeaf(): Boolean = myNode.firstChildNode == null
}

class FormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
        return FormattingModelProvider
            .createFormattingModelForPsiFile(
                element.containingFile,
                SimpleBlock(
                    element.node,
                    Wrap.createWrap(NONE, false),
                    Alignment.createAlignment(),
                    createSpaceBuilder(settings)
                ),
                settings
            )
    }

    private fun createSpaceBuilder(settings: CodeStyleSettings): SpacingBuilder {
        return SpacingBuilder(settings, AlloyLanguage)
            .around(Types.EQ)
            .spaceIf(settings.getCommonSettings(AlloyLanguage.id).SPACE_AROUND_EQUALITY_OPERATORS)
    }

    override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? = null
}
