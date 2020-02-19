package dev.necauqua.plugins.alloy.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import dev.necauqua.plugins.alloy.Icons

class NewFile : CreateFileFromTemplateAction("Alloy File", "", Icons.ALLOY_ICON), DumbAware {

    override fun getActionName(directory: PsiDirectory, newName: String, templateName: String): String = "Alloy File"

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("Alloy File")
            .addKind("Empty File", Icons.ALLOY_ICON, "Alloy File")
    }
}
