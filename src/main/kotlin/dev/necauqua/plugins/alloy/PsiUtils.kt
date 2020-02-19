package dev.necauqua.plugins.alloy

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import dev.necauqua.plugins.alloy.psi.SigDecl
import dev.necauqua.plugins.alloy.psi.SimpleName


object PsiUtils {

    fun findAllSigs(project: Project): List<Pair<String, SimpleName>> {
        val result = mutableListOf<Pair<String, SimpleName>>()
        val psiManager = PsiManager.getInstance(project)

        for (file in FileTypeIndex.getFiles(AlloyFileType, GlobalSearchScope.allScope(project))) {
            val root = psiManager.findFile(file) as? AlloyFile ?: continue
            val prefix = "${file.nameWithoutExtension}/"
            root.findChildrenByClass(SigDecl::class.java)
                .asSequence()
                .flatMap { it.simpleNameList.asSequence() }
                .mapTo(result) { "$prefix/${it.text}" to it }
        }
        return result
    }

    fun findVisibleSigs(project: Project, currentVirtualFile: VirtualFile): List<String> {
        val result = mutableListOf<String>()
        val psiManager = PsiManager.getInstance(project)

        for ((prefix, virtualFile) in buildModuleTree(project, currentVirtualFile)) {
            val file = psiManager.findFile(virtualFile) as? AlloyFile ?: continue

            file.findChildrenByClass(SigDecl::class.java)
                .asSequence()
                .flatMap { it.simpleNameList.asSequence() }
                .mapTo(result) { prefix + it.text }
        }
        return result
    }

    fun createSimpleName(project: Project, name: String): SimpleName {
        val file = createFile(project, "sig $name {}")
        return file.firstChild.children[1] as SimpleName
    }

    fun createFile(project: Project, text: String): AlloyFile =
        PsiFileFactory.getInstance(project).createFileFromText("dummy.als", AlloyFileType, text) as AlloyFile

    private fun buildModuleTree(project: Project, virtualFile: VirtualFile): List<Pair<String, VirtualFile>> {

        fun dfs(
            psiManager: PsiManager,
            virtualFile: VirtualFile,
            known: MutableSet<String>,
            result: MutableList<Pair<String, VirtualFile>>
        ) {
            val root = psiManager.findFile(virtualFile) as? AlloyFile ?: return
            val dir = virtualFile.parent
            for (import in root.imports) {
                val name = import.qualName.text
                if (!known.add(name)) {
                    continue
                }
                val pkg = import.simpleName?.text ?: name
                val file = dir.findFileByRelativePath("$name.als") ?: continue
                result += "$pkg/" to file
                dfs(psiManager, file, known, result)
            }
        }

        val result = mutableListOf("" to virtualFile)
        dfs(PsiManager.getInstance(project), virtualFile, hashSetOf(), result)
        return result
    }
}
