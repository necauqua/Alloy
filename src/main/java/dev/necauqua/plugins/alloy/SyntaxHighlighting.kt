package dev.necauqua.plugins.alloy

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.options.colors.*
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import dev.necauqua.plugins.alloy.psi.Types
import javax.swing.Icon

private val BLOCK_COMMENT = arrayOf(createTextAttributesKey("ALLOY_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT))
private val C_LINE_COMMENT = arrayOf(createTextAttributesKey("ALLOY_C_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT))
private val LINE_COMMENT = arrayOf(createTextAttributesKey("ALLOY_LINE_COMMENT", C_LINE_COMMENT.first()))
private val KEYWORD = arrayOf(createTextAttributesKey("ALLOY_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD))
private val IDENTIFIER = arrayOf(createTextAttributesKey("ALLOY_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER))
private val THIS = arrayOf(createTextAttributesKey("ALLOY_THIS", DefaultLanguageHighlighterColors.KEYWORD))

private val BRACES = arrayOf(createTextAttributesKey("ALLOY_BRACES", DefaultLanguageHighlighterColors.BRACES))
private val PARENTHESES = arrayOf(createTextAttributesKey("ALLOY_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES))
private val BRACKETS = arrayOf(createTextAttributesKey("ALLOY_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS))

private val DOT = arrayOf(createTextAttributesKey("ALLOY_DOT", DefaultLanguageHighlighterColors.DOT))

private val BAD_CHARACTER = arrayOf(createTextAttributesKey("ALLOY_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER))

object SyntaxHighlighter : SyntaxHighlighter {
    override fun getTokenHighlights(tokenType: IElementType): Array<out TextAttributesKey> =
        when (tokenType) {
            Types.BLOCK_COMMENT -> BLOCK_COMMENT
            Types.C_LINE_COMMENT -> C_LINE_COMMENT
            Types.LINE_COMMENT -> LINE_COMMENT
            Types.NAME -> IDENTIFIER
            Types.LBRACE, Types.RBRACE -> BRACES
            Types.LBRACKET, Types.RBRACKET -> BRACKETS
            Types.LPAREN, Types.RPAREN -> PARENTHESES
            Types.DOT -> DOT
            TokenType.BAD_CHARACTER -> BAD_CHARACTER
            else -> if (tokenType.toString().startsWith("K_")) KEYWORD else emptyArray()
        }

    override fun getHighlightingLexer(): Lexer = FlexAdapter(AlloyLexer(null))
}

class SyntaxHighlighterFactory : SyntaxHighlighterFactory() {

    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter = SyntaxHighlighter
}

class ColorSettingsPage : ColorSettingsPage {
    override fun getIcon(): Icon? = AlloyFileType.ICON

    override fun getHighlighter(): SyntaxHighlighter = SyntaxHighlighter

    override fun getDemoText(): String = """
        /*
         * Impose an ordering on the State.
         */
        open util/ordering[State]

        // Farmer and his possessions are objects.
        abstract sig Object { eats: set Object }
        one sig Farmer, Fox, Chicken, Grain extends Object {}

        // Defines what eats what and the farmer is not around.
        fact { eats = Fox->Chicken + Chicken->Grain}

        -- Stores the objects at near and far side of river.
        sig State { near, far: set Object }

        -- In the initial state, all objects are on the near side.
        fact { first.near = Object && no first.far }

        // At most one item to move from 'from' to 'to'
        pred crossRiver [from, from', to, to': set Object] {
          one x: from | {
            from' = from - x - Farmer - from'.eats
            to' = to + x + Farmer
          }
        }

        // crossRiver transitions between states
        fact {
          all s: State, s': s.next {
            Farmer in s.near =>
              crossRiver [s.near, s'.near, s.far, s'.far]
            else
              crossRiver [s.far, s'.far, s.near, s'.near]
          }
        }

        -- the farmer moves everything to the far side of the river.
        run { last.far=Object } for exactly 8 State
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "Alloy"

    companion object {
        private val DESCRIPTORS = arrayOf(
                AttributesDescriptor("Keyword", KEYWORD.first()),
                AttributesDescriptor("Identifier", IDENTIFIER.first()),
                AttributesDescriptor("'this' reference", THIS.first()),

                AttributesDescriptor("Block Comment", BLOCK_COMMENT.first()),
                AttributesDescriptor("Line Comment (C)", C_LINE_COMMENT.first()),
                AttributesDescriptor("Line Comment", LINE_COMMENT.first()),

                AttributesDescriptor("Parentheses", PARENTHESES.first()),
                AttributesDescriptor("Braces", BRACES.first()),
                AttributesDescriptor("Brackets", BRACKETS.first()),

                AttributesDescriptor("Bad Value", BAD_CHARACTER.first())
        )
    }
}
