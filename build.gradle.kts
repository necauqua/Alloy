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
    implementation(files("alloy.jar", "alloy-src.zip"))
}

group = "dev.necauqua.plugins"
version = "0.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

sourceSets {
    main {
        java.srcDirs("src/main/gen")
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

fun generateParserTask(suffix: String = "", config: GenerateParser.() -> Unit = {}) =
        task<GenerateParser>("generateParser${suffix.capitalize()}") {
            source = "src/main/grammars/grammar.bnf"
            targetRoot = "src/main/gen"
            pathToParser = "AlloyParser"
            pathToPsiRoot = "dev/necauqua/plugins/alloy/psi"
            purgeOldFiles = true
            config()
        }

val generateParserInitial = generateParserTask("initial")

val compileKotlin = tasks.named("compileKotlin") {
    dependsOn(generateLexer, generateParserInitial)
}

val generateParser = generateParserTask {
    dependsOn(compileKotlin)
    classpath(compileKotlin.get().outputs)
}

tasks.named("compileJava") {
    dependsOn(generateParser)
}

idea {
    module {
        inheritOutputDirs = true
        generatedSourceDirs.add(file("src/main/gen"))

        workspace {

            // old XML manipulation does not work, so we go all in dork on it
            // need to quickly insert one thing, won't bother even with proper XML stuff

            val workspace = file(".idea/workspace.xml")
            val workspaceXml = workspace.readText()
            if (workspaceXml.contains("<component name=\"RunManager\">")) {
                return@workspace
            }
            workspace.writeText(workspaceXml.replace(Regex("(<project.*?>)"), """
                $1
                  <component name="RunManager">
                    <configuration name="run" type="GradleRunConfiguration" factoryName="Gradle">
                      <ExternalSystemSettings>
                        <option name="externalProjectPath" value="\${'$'}PROJECT_DIR\${'$'}" />
                        <option name="externalSystemIdString" value="GRADLE" />
                        <option name="taskNames">
                          <list>
                            <option value="runIde" />
                          </list>
                        </option>
                      </ExternalSystemSettings>
                    </configuration>
                  </component>
            """.trimIndent()))

            val kotlinScripting = file(".idea/kotlinScripting.xml")
            if (!kotlinScripting.exists()) {
                kotlinScripting.writeText("""
                    <?xml version="1.0" encoding="UTF-8"?>
                    <project version="4">
                      <component name="KotlinScriptingSettings">
                        <option name="isAutoReloadEnabled" value="true" />
                        <option name="suppressDefinitionsCheck" value="true" />
                      </component>
                    </project>
                """.trimIndent())
            }
        }
    }
}
