package dev.necauqua.plugins.alloy.components

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import dev.necauqua.plugins.alloy.psi.PsiBlock
import dev.necauqua.plugins.alloy.psi.Types

class FoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun getPlaceholderText(node: ASTNode): String? =
            when (node.elementType) {
                Types.BLOCK_COMMENT ->
                    (node as PsiComment).text
                            .lineSequence()
                            .map { it.replace(COMMENT_PREFIX, "") }
                            .firstOrNull { it.isNotBlank() }
                            ?.let { "/* $it ...*/" }
                else -> "{...}"
            }

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val comments = PsiTreeUtil.findChildrenOfType(root, PsiComment::class.java).asSequence()
                .mapNotNull {
                    val comment = it.node as PsiComment
                    if (comment.text.startsWith("/*")) FoldingDescriptor(comment, it.textRange) else null
                }

        val blocks = PsiTreeUtil.findChildrenOfType(root, PsiBlock::class.java).asSequence()
                .map { FoldingDescriptor(it, it.textRange) }

        return (comments + blocks).toList().toTypedArray()
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false

    companion object {
        private val COMMENT_PREFIX = Regex("^\\s*/?\\*+")
    }
}
