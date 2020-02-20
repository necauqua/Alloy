package dev.necauqua.plugins.alloy.runner

import com.intellij.build.BuildTextConsoleView
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.options.ExtensionSettingsEditor
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.RawCommandLineEditor
import dev.necauqua.plugins.alloy.Icons
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JPanel
import kotlin.concurrent.thread


class AlloyConfigurationType : ConfigurationType {

    override fun getIcon(): Icon = Icons.ALLOY_ICON

    override fun getConfigurationTypeDescription(): String = "the description is here"

    override fun getId(): String = "ALLOY_RUN_CONFIGURATION"

    override fun getDisplayName(): String = "Alloy"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> =
            arrayOf(DemoConfigurationFactory(this))
}

class DemoConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {

    override fun createTemplateConfiguration(project: Project): RunConfiguration =
            DemoRunConfiguration(project, this, "Demo")

    override fun getName(): String = "Demo configuration factory"
}

class DemoRunConfiguration(project: Project, factory: ConfigurationFactory, name: String) : RunConfigurationBase<Any>(project, factory, name) {

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return DemoSettingsEditor()
    }

    //    @Throws(RuntimeConfigurationException::class)
    override fun checkConfiguration() {
    }

    // @Throws(ExecutionException::class)
    override fun getState(executor: Executor, executionEnvironment: ExecutionEnvironment): RunProfileState? {
        return RunProfileState { _, _ ->
            val view = BuildTextConsoleView(project)

            val ping = ProcessBuilder()
                    .command("ping", "8.8.8.8")
                    .start()

            val pingOutput = ping.inputStream

            val reader = BufferedReader(InputStreamReader(pingOutput))

            val handler = object : ProcessHandler() {
                override fun getProcessInput(): OutputStream? = null
                override fun detachIsDefault(): Boolean = false
                override fun detachProcessImpl() = destroyProcessImpl()

                override fun destroyProcessImpl() {
                    ping.destroy()
                }

                fun terminated() {
                    notifyProcessTerminated(ping.exitValue())
                }
            }

            thread(isDaemon = true) {
                while (true) {
                    val line = reader.readLine() ?: break // null means end of stream
                    view.append("$line\n", true)
                }
                view.append("Termination request, waiting for 3s\n", false)
                Thread.sleep(3000L)
                handler.terminated()
            }

            object : ExecutionResult {
                override fun getExecutionConsole(): ExecutionConsole = view
                override fun getProcessHandler(): ProcessHandler = handler
                override fun getActions(): Array<AnAction> = emptyArray()
            }
        }
    }
}

class DemoSettingsEditor : SettingsEditor<DemoRunConfiguration>(), ExtensionSettingsEditor {
    private lateinit var myPanel: JPanel
    private lateinit var myScriptSelector: TextFieldWithBrowseButton
    private lateinit var myScriptOptions: RawCommandLineEditor
    private lateinit var myScriptWorkingDirectory: TextFieldWithBrowseButton
    private lateinit var myInterpreterSelector: TextFieldWithBrowseButton
    private lateinit var myInterpreterOptions: RawCommandLineEditor

    override fun resetEditorFrom(demoRunConfiguration: DemoRunConfiguration) {
    }

    // @Throws(ConfigurationException::class)
    override fun applyEditorTo(demoRunConfiguration: DemoRunConfiguration) {
    }

    override fun createEditor(): JComponent = myPanel
}
