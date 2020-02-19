package dev.necauqua.plugins.alloy

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import dev.necauqua.plugins.alloy.psi.Import
import dev.necauqua.plugins.alloy.psi.RelDecls
import dev.necauqua.plugins.alloy.psi.SigDecl
import groovy.lang.Tuple2


object PsiUtils {

    fun buildModuleTree(project: Project, virtualFile: VirtualFile): List<Pair<String, VirtualFile>> {
        val result = mutableListOf("" to virtualFile)
        buildModuleTree(PsiManager.getInstance(project), virtualFile, hashSetOf(), result)
        return result
    }

    private fun buildModuleTree(
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
            buildModuleTree(psiManager, file, known, result)
        }
    }

    fun findSigs(project: Project, currentVirtualFile: VirtualFile): List<String> {
        val result = mutableListOf<String>()
        val psiManager = PsiManager.getInstance(project)

        for ((prefix, virtualFile) in buildModuleTree(project, currentVirtualFile)) {
            val file = psiManager.findFile(virtualFile) as? AlloyFile ?: continue

            file.findChildrenByClass(SigDecl::class.java)
                .forEach {
                    it.simpleNameList.forEach {
                        result.add(prefix + it.text)
                    }
                }
        }
        return result
    }
}
