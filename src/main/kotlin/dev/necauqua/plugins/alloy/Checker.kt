package dev.necauqua.plugins.alloy

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import dev.necauqua.plugins.alloy.psi.Types

class Annotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element.elementType != Types.SIMPLE_NAME || element.parent.elementType != Types.SIG_DECL) {
            return
        }

        val text = element.text
        // todo this may be optimized
        if (PsiUtils.findVisibleSigs(element.project, element.containingFile.virtualFile).count { it == text } > 1) {
            holder.createErrorAnnotation(element, "Sig already defined")
        }
    }
}
