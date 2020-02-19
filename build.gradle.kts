import org.jetbrains.grammarkit.tasks.GenerateLexer
import org.jetbrains.grammarkit.tasks.GenerateParser
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(files("alloy.jar"))
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.3.61")
}

group = "dev.necauqua.plugins"
version = "0.1.0"

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

val generateLexer = task<GenerateLexer>("generateLexer") {
    source = "src/main/grammars/lexer.flex"
    targetDir = "src/main/gen/dev/necauqua/plugins/alloy"
    targetClass = "AlloyLexer"
    purgeOldFiles = true
}

val generateParser = task<GenerateParser>("generateParser") {
    source = "src/main/grammars/grammar.bnf"
    targetRoot = "src/main/gen"
    pathToPsiRoot = "dev/necauqua/plugins/alloy/psi"
    pathToParser = "AlloyParser"
    purgeOldFiles = true
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
//    dependsOn(generateLexer, generateParser)
}
