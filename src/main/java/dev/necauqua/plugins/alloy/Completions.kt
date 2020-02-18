package dev.necauqua.plugins.alloy

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.highlighting.BraceMatcher
import com.intellij.codeInsight.highlighting.PairedBraceMatcherAdapter
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.ASTNode
import com.intellij.lang.BracePair
import com.intellij.lang.Commenter
import com.intellij.lang.PairedBraceMatcher
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PlatformPatterns.psiFile
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import dev.necauqua.plugins.alloy.psi.Paragraph
import dev.necauqua.plugins.alloy.psi.Types


class KeywordCompletionProvider(private vararg val keywords: String) : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        keywords.forEach { keyword -> result.addElement(LookupElementBuilder.create(keyword).bold()) }
    }
}

//fun InsertionContext.addSuffix(suffix: String) {
//    document.insertString(selectionEndOffset, suffix)
//    EditorModificationUtil.moveCaretRelatively(editor, suffix.length)
//}

class SimpleCompletionContributor : CompletionContributor() {

    init {
//        extend(CompletionType.BASIC, topLevel(), KeywordCompletionProvider(*STATEMENT_STARTERS))
//        extend(CompletionType.BASIC, psiElement(Types.NAME), KeywordCompletionProvider(*KEYWORDS))
        extend(CompletionType.BASIC, topLevel(), object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(
                parameters: CompletionParameters,
                context: ProcessingContext,
                result: CompletionResultSet
            ) {
                PsiUtils.findSigs(parameters.position.project, parameters.originalFile.virtualFile).forEach {
                    println(it)
                    result.addElement(LookupElementBuilder.create(it))
                }
            }
        })
    }

    private fun topLevel() = psiElement().withParent(psiFile())

    companion object {

        val STATEMENT_STARTERS = arrayOf(
                "abstract sig ", "assert ", "check ", "fact ", "fun ", "lone sig ", "one sig ", "pred ", "sig ", "some sig "
        )

        val KEYWORDS = arrayOf(
                "abstract", "all", "and", "as", "assert", "but", "check", "disj", "disjoint", "else",
                "enum", "exactly", "exh", "exhaustive", "expect", "extends", "fact", "for", "fun", "iden", "iff",
                "implies", "in", "int_c", "int", "let", "lone", "module", "no", "none", "not", "one", "open", "or",
                "part", "partition", "pred", "private", "run", "seq", "set", "sig", "some", "string", "sum", "this",
                "univ"
        )
    }
}

class BraceMatcher : PairedBraceMatcher {

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int = openingBraceOffset

    override fun getPairs(): Array<BracePair> = BRACE_PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

    companion object {
        val BRACE_PAIRS = arrayOf(
                BracePair(Types.LBRACE, Types.RBRACE, true),
                BracePair(Types.LBRACKET, Types.RBRACKET, false),
                BracePair(Types.LPAREN, Types.RPAREN, false)
        )
    }
}

class AdaptedBraceMatcher : PairedBraceMatcherAdapter(BraceMatcher(), AlloyLanguage)

class Commenter : Commenter {

    override fun getLineCommentPrefix(): String? = "-- "

    override fun getBlockCommentPrefix(): String? = "/* "
    override fun getBlockCommentSuffix(): String? = " */"

    override fun getCommentedBlockCommentPrefix(): String? = null
    override fun getCommentedBlockCommentSuffix(): String? = null
}

class FoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun getPlaceholderText(node: ASTNode): String? =
            when (node.elementType) {
                Types.SIG_DECL -> "sig { ... }"
                Types.FACT_DECL -> "fact { ... }"
                Types.PRED_DECL -> "pred { ... }"
                Types.FUN_DECL -> "fun { ... }"
                Types.ASSERT_DECL -> "assert { ... }"
                Types.CMD_DECL -> "run { ... }"
                Types.BLOCK_COMMENT -> (node as PsiComment).text
                        .lineSequence()
                        .map { it.replace(COMMENT_PREFIX, "") }
                        .firstOrNull { it.isNotBlank() }
                        ?.let { "/* $it ...*/" }
                else -> null
            }

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val comments = PsiTreeUtil.findChildrenOfType(root, PsiComment::class.java).asSequence()
                .mapNotNull {
                    val comment = it.node as PsiComment
                    if (comment.text.startsWith("/*")) FoldingDescriptor(comment, it.textRange) else null
                }

        val paragraphs = (root as? AlloyFile)?.paragraphs.orEmpty().asSequence()
                .map { FoldingDescriptor(it.node, it.textRange) }

        return (comments + paragraphs).toList().toTypedArray()
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false

    companion object {
        private val COMMENT_PREFIX = Regex("^\\s*/?\\*+")
    }
}
