package dev.necauqua.plugins.alloy

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.*
import com.intellij.patterns.StandardPatterns.not
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import dev.necauqua.plugins.alloy.psi.Paragraph
import dev.necauqua.plugins.alloy.psi.Types

class SimpleCompletionContributor : CompletionContributor() {

    private fun ElementPattern<out PsiElement>.basic(completion: Completion) =
            extend(CompletionType.BASIC, this, CompletionAdapter(completion))

    private fun ElementPattern<out PsiElement>.smart(completion: Completion) =
            extend(CompletionType.SMART, this, CompletionAdapter(completion))

    init {

        val topLevel = psiElement()
                .withParent(psiFile())

        val tld = or(psiElement(Types.MODULE_DECL), psiElement(Types.IMPORT), psiElement(Paragraph::class.java))

        val skip = psiElement().whitespaceCommentEmptyOrError()

//        psiElement()
//                .withParent(topLevel.andNot(topLevel.withChild(psiElement(Types.MODULE_DECL))))
////                .andNot(psiElement().afterSibling(psiElement(Paragraph::class.java)))
//
//                .basic { params, _, result ->
//                    val name = params.originalFile.name.removeSuffix(".als")
//                    result.addElement(LookupElementBuilder.create("module")
//                            .bold()
//                            .withTailText(" $name")
//                            .withInsertedSuffix(" $name \n"))
//                }

        or(topLevel.afterSiblingSkipping(skip, tld), topLevel.isFirstAcceptedChild(not(skip)))
                .basic { _, _, result ->
                    STATEMENT_STARTERS.forEach {
                        result.addElement(LookupElementBuilder.create(it).bold().withInsertedSuffix(" "))
                    }
                }

//        psiElement().afterLeaf()



//        psiElement().afterLeaf(or(
//                psiElement(Types.K_LONE),
//                psiElement(Types.K_ONE),
//                psiElement(Types.K_SOME)
//        )).basic { _, _, result ->
//            arrayOf("sig", "abstract").forEach {
//                result.addElement(LookupElementBuilder.create(it).bold().withSuffix(" "))
//            }
//        }

//        psiElement().afterLeaf(psiElement(Types.K_ABSTRACT)).basic { _, _, result ->
//            arrayOf("sig", "lone", "one", "some").forEach {
//                result.addElement(LookupElementBuilder.create(it).bold())
//            }
//        }

//        sigPrefix.basic { _, _, result ->
//            result.addElement(LookupElementBuilder.create("sig").bold().withAutoCompletionPolicy(ALWAYS_AUTOCOMPLETE))
//        }

//        topLevel.basic { parameters, _, result ->
//            PsiUtils.findSigs(parameters.position.project, parameters.originalFile.virtualFile).forEach {
//                result.addElement(LookupElementBuilder.create(it))
//            }
//        }
    }

    companion object {

        val STATEMENT_STARTERS = arrayOf(
                "abstract", "lone", "one", "some", "sig",
                "assert", "check", "fact", "fun", "pred"
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

typealias Completion = (CompletionParameters, ProcessingContext, CompletionResultSet) -> Unit

class CompletionAdapter(private val completion: Completion) : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) =
            completion(parameters, context, result)
}

fun LookupElementBuilder.withInsertedSuffix(suffix: String): LookupElementBuilder =
        withInsertHandler { context, _ ->
            context.document.insertString(context.selectionEndOffset, suffix)
            EditorModificationUtil.moveCaretRelatively(context.editor, suffix.length)
        }
