package dev.necauqua.plugins.alloy

import com.intellij.application.options.*
import com.intellij.formatting.*
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.Indent
import com.intellij.formatting.WrapType.NONE
import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.codeStyle.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import com.intellij.psi.formatter.common.AbstractBlock
import dev.necauqua.plugins.alloy.psi.PsiBlock


class ScanBlock(node: ASTNode) : AbstractBlock(node, Wrap.createWrap(NONE, false), Alignment.createAlignment()) {

    override fun buildChildren(): List<Block> {
        val blocks = mutableListOf<Block>()

//        PsiTreeUtil.findChildrenOfType(node.psi, PsiBlock::class.java)

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

class CodeStyleSettings(settings: CodeStyleSettings?) : CustomCodeStyleSettings("AlloyCodeStyleSettings", settings)

class CodeStyleSettingsProvider : CodeStyleSettingsProvider() {

    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings? =
            CodeStyleSettings(settings)

    override fun getConfigurableDisplayName(): String? = "Alloy"

    override fun createConfigurable(settings: CodeStyleSettings, modelSettings: CodeStyleSettings): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(settings, modelSettings, configurableDisplayName) {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
                return SimpleCodeStyleMainPanel(currentSettings, settings)
            }
        }
    }

    private class SimpleCodeStyleMainPanel(currentSettings: CodeStyleSettings?, settings: CodeStyleSettings?) :
            TabbedLanguageCodeStylePanel(AlloyLanguage, currentSettings, settings)
}

class SimpleLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getLanguage(): Language = AlloyLanguage

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
    }

    override fun getCodeSample(settingsType: SettingsType): String? = CODE_SAMPLE
}
