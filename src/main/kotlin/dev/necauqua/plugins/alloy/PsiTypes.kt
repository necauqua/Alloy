package dev.necauqua.plugins.alloy

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.tree.IElementType
import dev.necauqua.plugins.alloy.psi.*

class TokenType(debugName: String) : IElementType(debugName, AlloyLanguage)

class ElementType(debugName: String) : IElementType(debugName, AlloyLanguage)

class AlloyFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, AlloyLanguage) {

    inline val moduleDecl: ModuleDecl?
        get() = findChildByClass(ModuleDecl::class.java)

    inline val imports: Array<Import>
        get() = findChildrenByClass(Import::class.java)

    inline val paragraphs: Array<Paragraph>
        get() = findChildrenByClass(Paragraph::class.java)

    override fun getFileType(): FileType = AlloyFileType

    override fun toString(): String = "Alloy File"
}
