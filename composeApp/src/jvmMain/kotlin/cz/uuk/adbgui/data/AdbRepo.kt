package cz.uuk.adbgui.data

import cz.uuk.adbgui.domain.model.AndroidDevice
import cz.uuk.adbgui.domain.model.AndroidPackage
import cz.uuk.adbgui.utils.tickerFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds

class AdbRepository(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    val devices: StateFlow<List<AndroidDevice>> = devicesStateFlow()

    init {
        startServer()
    }

    private fun startServer() {
        runAdb("start-server")
    }

    fun refresh() {}

    private fun devicesStateFlow() = tickerFlow(1.seconds, 0.seconds).map {
        getDevices()
    }.stateIn(scope, SharingStarted.Lazily, emptyList())

    private fun getDevices(): List<AndroidDevice> {
        val output = runAdb("devices", "-l") ?: return emptyList()
        return output.lines()
            .drop(1)
            .filter { it.contains("\tdevice") }
            .map { line ->
                val parts = line.trim().split("\\s+".toRegex())
                val serial = parts[0]
                val model = line.substringAfter("model:", "").substringBefore(" ").ifEmpty { serial }
                AndroidDevice(id = serial, name = model)
            }
    }

    private fun getPackages(device: AndroidDevice): List<AndroidPackage> {

        val output = runAdb("-s", device.id, "shell", "pm", "list", "packages") ?: return emptyList()
        return output.lines()
            .filter { it.startsWith("package:") }
            .map { AndroidPackage(id = it.removePrefix("package:").trim()) }
    }

    fun devicePackages(device: AndroidDevice): StateFlow<List<AndroidPackage>> = tickerFlow(1.seconds, 0.seconds).map {
        getPackages(device)
    }.stateIn(scope, SharingStarted.WhileSubscribed(1.seconds.inWholeMilliseconds), emptyList())

    fun deletePackage(device: AndroidDevice, pack: AndroidPackage) {
        runAdb("-s", device.id, "shell", "pm", "uninstall", "--user", "0", pack.id)
    }

    private fun runAdb(vararg args: String): String? {
        return try {
            val process = ProcessBuilder("adb", *args)
                .redirectErrorStream(true)
                .start()
            val output = process.inputStream.bufferedReader().readText()
            process.waitFor()
            output
        } catch (e: Exception) {
            null
        }
    }
}
