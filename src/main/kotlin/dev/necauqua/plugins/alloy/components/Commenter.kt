package dev.necauqua.plugins.alloy.components

import com.intellij.lang.Commenter

class Commenter : Commenter {

    companion object {

        var classyComments = false
    }

    override fun getLineCommentPrefix(): String? = if (classyComments) "-- " else "// "

    override fun getBlockCommentPrefix(): String? = "/*"
    override fun getBlockCommentSuffix(): String? = "*/"

    override fun getCommentedBlockCommentPrefix(): String? = null
    override fun getCommentedBlockCommentSuffix(): String? = null
}
