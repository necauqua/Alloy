package dev.necauqua.plugins.alloy

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import java.awt.*
import javax.swing.Icon

object AlloyLanguage : Language("alloy") {
    override fun getAssociatedFileType(): LanguageFileType = AlloyFileType
}

object AlloyFileType : LanguageFileType(AlloyLanguage) {

    override fun getDefaultExtension(): String = "als"

    override fun getName(): String = "alloy"

    override fun getDescription(): String = "Alloy source file"

    override fun getIcon(): Icon? = object : Icon {
        override fun paintIcon(c: Component, g: Graphics, x: Int, y: Int) {
            g.color = Color.gray
            g.fillOval(2, 2, 14, 14)
            g.color = Color.red
            (g as Graphics2D).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g.font = g.font.deriveFont(14.0f)
            g.drawString("a", 6, 13)
        }

        override fun getIconHeight(): Int = 16
        override fun getIconWidth(): Int = 16
    }
}
