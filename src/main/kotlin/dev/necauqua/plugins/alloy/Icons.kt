package dev.necauqua.plugins.alloy

import java.awt.*
import javax.swing.Icon

object Icons {

    val ALLOY_ICON = object : Icon {
        override fun paintIcon(c: Component, g: Graphics, x: Int, y: Int) {
            (g as Graphics2D).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g.color = Color.gray
            g.fillOval(2, 2, 14, 14)
            g.color = Color.red
            g.font = g.font.deriveFont(14.0f)
            g.drawString("a", 6, 13)
        }

        override fun getIconHeight(): Int = 16
        override fun getIconWidth(): Int = 16
    }
}
