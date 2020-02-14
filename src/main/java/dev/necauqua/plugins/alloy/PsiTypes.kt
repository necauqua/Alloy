package dev.necauqua.plugins.alloy

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.tree.IElementType

class TokenType(debugName: String) : IElementType(debugName, AlloyLanguage)

class ElementType(debugName: String) : IElementType(debugName, AlloyLanguage)

class AlloyFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, AlloyLanguage) {
    override fun getFileType(): FileType = AlloyFileType

    override fun toString(): String = "Simple File"
}
