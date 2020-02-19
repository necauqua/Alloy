package dev.necauqua.plugins.alloy.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.elementType
import com.intellij.util.IncorrectOperationException
import dev.necauqua.plugins.alloy.AlloyFile

object PsiImplUtil {

    private fun <T: PsiElement> T.reference(ref: () -> PsiElement?): PsiReferenceBase<T> {
        return object : PsiReferenceBase<T>(this) {
            override fun resolve(): PsiElement? = ref()
        }
    }

    @JvmStatic
    fun getReference(element: SimpleName): PsiReference? {
        val parent = element.parent
        if (parent.parent.elementType == Types.IMPORT) {
            val psiManager = PsiManager.getInstance(element.project)
            return element.reference {
                val file = element.containingFile.virtualFile
                        .parent
                        ?.findFileByRelativePath("${parent.text}.als")
                        ?: return@reference null

                val psiFile = psiManager
                        .findFile(file)
                        as? AlloyFile
                        ?: return@reference null

                psiFile.moduleDecl?.qualName ?: psiFile
            }
        }
        return null
    }

    @JvmStatic
    fun getName(element: SimpleName): String {
        return element.text
    }

    @JvmStatic
    fun setName(element: SimpleName, name: String): PsiElement {
        throw IncorrectOperationException("not supported yet, sry")
    }

    @JvmStatic
    fun getReference(element: QualNamePart): PsiReference? {
        val prefix = element.parent as QualPrefix
        if (prefix.parent.parent.elementType == Types.IMPORT) {
            val psiManager = PsiManager.getInstance(element.project)

            return element.reference {
                val file = element.containingFile.virtualFile
                        .parent
                        ?.findFileByRelativePath((prefix.qualNamePartList.asSequence().takeWhile { it != element } + element).joinToString("/") { it.text })
                        ?: return@reference null

                psiManager.findDirectory(file)
            }
        }
        return null
    }

    @JvmStatic
    fun getName(element: QualNamePart): String {
        return element.text
    }

    @JvmStatic
    fun setName(element: QualNamePart, name: String): PsiElement {
        throw IncorrectOperationException("not supported yet, sry")
    }
}
