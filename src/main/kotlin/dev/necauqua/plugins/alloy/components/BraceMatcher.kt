package dev.necauqua.plugins.alloy.components

import com.intellij.codeInsight.highlighting.PairedBraceMatcherAdapter
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import dev.necauqua.plugins.alloy.AlloyLanguage
import dev.necauqua.plugins.alloy.psi.Types

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
