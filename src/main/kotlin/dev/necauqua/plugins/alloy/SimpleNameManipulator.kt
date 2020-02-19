package dev.necauqua.plugins.alloy

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import dev.necauqua.plugins.alloy.psi.QualNamePart
import dev.necauqua.plugins.alloy.psi.SimpleName

class SimpleNameManipulator: AbstractElementManipulator<SimpleName>() {

    override fun handleContentChange(element: SimpleName, range: TextRange, newContent: String): SimpleName? {
        return element.setName(newContent) as SimpleName?
    }
}

class QualNamePartManipulator : AbstractElementManipulator<QualNamePart>() {

    override fun handleContentChange(element: QualNamePart, range: TextRange, newContent: String): QualNamePart? {
        return element.setName(newContent) as QualNamePart?
    }
}
