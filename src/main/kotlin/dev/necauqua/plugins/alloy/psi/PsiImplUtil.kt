package dev.necauqua.plugins.alloy.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.elementType
import com.intellij.util.IncorrectOperationException
import dev.necauqua.plugins.alloy.AlloyFile
import dev.necauqua.plugins.alloy.AlloyLibrary
import dev.necauqua.plugins.alloy.Icons
import dev.necauqua.plugins.alloy.PsiUtils
import javax.swing.Icon


object PsiImplUtil {

    private fun <T : PsiElement> reference(element: T, ref: T.() -> PsiElement?): PsiReferenceBase<T> {
        return object : PsiReferenceBase<T>(element) {
            override fun resolve(): PsiElement? = element.ref()
        }
    }

    @JvmStatic
    fun getReference(element: SimpleName): PsiReference? {
        val parent = element.parent
        if (parent.parent.elementType == Types.IMPORT) {
            val psiManager = PsiManager.getInstance(element.project)
            return reference(element) {
                val file = containingFile.virtualFile
                        .parent
                        ?.findFileByRelativePath("${parent.text}.als")
                        ?: AlloyLibrary.defaultSourceRoot?.findFileByRelativePath("${parent.text}.als")
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
        return PsiUtils.createSimpleName(element.project, name)
    }

    @JvmStatic
    fun getNameIdentifier(element: SimpleName): PsiElement {
        return element
    }

    @JvmStatic
    fun getPresentation(element: SimpleName): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String? {
                val psiFile = element.containingFile
                val file = psiFile.virtualFile
                val relativePath = (ProjectRootManager.getInstance(element.project)
                        .fileIndex
                        .getSourceRootForFile(file)
                        ?.path
                        ?.let { file.path.substring(it.length + 1) }
                        ?: file.path)

                val thing = if (element.parent.elementType == Types.SIG_DECL) {
                    "Sig ${element.text}"
                } else "<unknown>"

                val isModule = (psiFile as AlloyFile?)?.moduleDecl != null
                val fileType = if (isModule) "module" else "file"
                val place = if (isModule) relativePath.removeSuffix(".als") else relativePath
                return "$thing in $fileType '$place'"
            }

            override fun getLocationString(): String? = null

            override fun getIcon(unused: Boolean): Icon? = Icons.ALLOY_ICON
        }
    }

    @JvmStatic
    fun getReference(element: QualNamePart): PsiReference? {
        val prefix = element.parent as QualPrefix
        if (prefix.parent.parent.elementType == Types.IMPORT) {
            val psiManager = PsiManager.getInstance(element.project)
            return reference(element) {
                val file = containingFile.virtualFile.parent?.let {
                    val path = (prefix.qualNamePartList.asSequence().takeWhile { it != element } + element).joinToString("/") { it.text }
                    it.findFileByRelativePath(path) ?: AlloyLibrary.defaultSourceRoot?.findFileByRelativePath(path)
                } ?: return@reference null
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

    @JvmStatic
    fun getNameIdentifier(element: QualNamePart): PsiElement {
        return element
    }
}
