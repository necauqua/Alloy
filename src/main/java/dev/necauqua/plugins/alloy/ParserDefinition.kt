package dev.necauqua.plugins.alloy

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.ParserDefinition.SpaceRequirements.MAY
import com.intellij.lang.PsiParser
import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import dev.necauqua.plugins.alloy.parser.AlloyParser
import dev.necauqua.plugins.alloy.psi.Types

class ParserDefinition : ParserDefinition {

    companion object {
        val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)
        val COMMENTS = TokenSet.create(Types.BLOCK_COMMENT, Types.C_LINE_COMMENT, Types.LINE_COMMENT)
        val FILE = IFileElementType(AlloyLanguage)
    }

    override fun createLexer(project: Project): Lexer = FlexAdapter(AlloyLexer())

    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    override fun createParser(project: Project): PsiParser = AlloyParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun createFile(viewProvider: FileViewProvider): PsiFile = AlloyFile(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): SpaceRequirements = MAY

    override fun createElement(node: ASTNode): PsiElement = Types.Factory.createElement(node)
}

