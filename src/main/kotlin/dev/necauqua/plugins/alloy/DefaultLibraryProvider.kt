package dev.necauqua.plugins.alloy

import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.AdditionalLibraryRootsProvider
import com.intellij.openapi.roots.SyntheticLibrary
import com.intellij.openapi.vfs.*
import org.alloytools.alloy.core.AlloyCore
import javax.swing.Icon

val libs = listOf(AlloyLibrary)

class DefaultLibraryProvider : AdditionalLibraryRootsProvider() {

    override fun getAdditionalProjectLibraries(project: Project): Collection<SyntheticLibrary> = libs
}

object AlloyLibrary : SyntheticLibrary(), ItemPresentation {

    override fun getLocationString(): String? = null

    override fun getIcon(unused: Boolean): Icon? = Icons.ALLOY_ICON

    override fun getPresentableText(): String? = "Alloy stdlib"

    val files: List<VirtualFile> by lazy {
        // really weird way to get this
        // works for now, later will do something better
        AlloyCore::class.java.classLoader.getResource("models")
            ?.toString()
            ?.let { it.substring(9, it.length - 9) }
            ?.takeIf { it.endsWith(".jar") }
            ?.let { LocalFileSystem.getInstance().findFileByPath(it) }
            ?.takeIf { it.isValid }
            ?.let { JarFileSystem.getInstance().getRootByLocal(it) }
            ?.takeIf { it.isValid }
            ?.findChild("models")
            ?.let(::listOf)
            ?: emptyList()
    }

    override fun getSourceRoots(): Collection<VirtualFile> = files

    override fun hashCode(): Int = 0

    override fun equals(other: Any?): Boolean = this === other
}
