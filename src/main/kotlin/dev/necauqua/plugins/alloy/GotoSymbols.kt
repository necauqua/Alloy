package dev.necauqua.plugins.alloy

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import dev.necauqua.plugins.alloy.psi.impl.SimpleNameImpl

class GotoClassContributor : ChooseByNameContributor {
    override fun getItemsByName(
        name: String,
        pattern: String,
        project: Project,
        includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        return PsiUtils.findAllSigs(project)
            .map { (_, sigDecl) -> sigDecl as SimpleNameImpl } // apparently only the impl has it
            .toTypedArray()
    }

    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        return PsiUtils.findAllSigs(project).map { it.first }.toTypedArray()
    }
}
