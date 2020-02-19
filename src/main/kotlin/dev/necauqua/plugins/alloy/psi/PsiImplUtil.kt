package dev.necauqua.plugins.alloy.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.IncorrectOperationException
import dev.necauqua.plugins.alloy.AlloyFile

object PsiImplUtil {

    @JvmStatic
    fun getReference(element: SimpleName): PsiReference? {
        val parent = element.parent
        if (parent is QualName) {
            val grandparent = parent.parent
            if (grandparent is Import) {
                val psiManager = PsiManager.getInstance(element.project)
                return object : PsiReferenceBase<SimpleName>(element) {
                    override fun resolve(): PsiElement? {
                        val file = element.containingFile.virtualFile
                                .parent
                                ?.findFileByRelativePath("${parent.text}.als")
                                ?: return null

                        val psiFile = psiManager
                                .findFile(file)
                                as? AlloyFile
                                ?: return null

                        return psiFile.moduleDecl?.qualName ?: psiFile
                    }
                }
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
        val parent = prefix.parent
        if (parent is QualName && parent.parent is Import) {
            val psiManager = PsiManager.getInstance(element.project)
            return object : PsiReferenceBase<QualNamePart>(element) {
                override fun resolve(): PsiElement? {
                    val dir = (prefix.qualNamePartList.asSequence().takeWhile { it != element } + element).joinToString("/") { it.text }

                    println(dir)

                    val file = element.containingFile.virtualFile
                            .parent
                            ?.findFileByRelativePath(dir)
                            ?: return null

                    return psiManager.findDirectory(file)
                }
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
