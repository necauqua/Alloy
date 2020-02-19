package dev.necauqua.plugins.alloy

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object AlloyLanguage : Language("alloy") {
    override fun getAssociatedFileType(): LanguageFileType = AlloyFileType
}

object AlloyFileType : LanguageFileType(AlloyLanguage) {

    override fun getDefaultExtension(): String = "als"

    override fun getName(): String = "alloy"

    override fun getDescription(): String = "Alloy source file"

    override fun getIcon(): Icon? = Icons.ALLOY_ICON
}
