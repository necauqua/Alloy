
buildscript {
    repositories { mavenCentral() }
    dependencies { classpath(kotlin("gradle-plugin", "1.3.61")) }
}

plugins {
    idea
    kotlin("jvm") version "1.3.61"
    id("org.jetbrains.intellij") version "0.4.13"
    id("org.jetbrains.grammarkit") version "2020.1"
}

repositories { mavenCentral() }
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.3.61")
}

group = "dev.necauqua.plugins"
version = "0.1.0"

//sourceSets {
//    val main by getting {
//        java.srcDir("src/gen")
//        kotlin.srcDir("src/main/kotlin")
//        resources.srcDir("src/main/resources")
//    }
//}

sourceSets {
    main {
        java.srcDirs("src/main/gen")
    }
}

idea {
    module {
        generatedSourceDirs.add(file("src/main/gen"))
    }
}

intellij {
    updateSinceUntilBuild = false
    instrumentCode = true
    version = "2019.3"
}

//patchPluginXml {
//    version = project.version
//}
