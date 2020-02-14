package dev.necauqua.plugins.alloy

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import dev.necauqua.plugins.alloy.psi.Types

private val COMMENT = arrayOf(createTextAttributesKey("SIMPLE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT))
private val KEYWORD = arrayOf(createTextAttributesKey("SIMPLE_KEY", DefaultLanguageHighlighterColors.KEYWORD))
private val BAD_CHARACTED = arrayOf(createTextAttributesKey("SIMPLE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER))

object AlloySyntaxHighlighter : SyntaxHighlighter {
    override fun getTokenHighlights(tokenType: IElementType): Array<out TextAttributesKey> =
        when (tokenType) {
            Types.COMMENT -> COMMENT
            TokenType.BAD_CHARACTER -> BAD_CHARACTED
            else -> if (tokenType.toString().startsWith("K_")) KEYWORD else emptyArray()
        }

    override fun getHighlightingLexer(): Lexer = FlexAdapter(AlloyLexer(null))
}

class AlsSyntaxHighlighterFactory : SyntaxHighlighterFactory() {

    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter = AlloySyntaxHighlighter
}
