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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.time.Duration.Companion.seconds

class AdbRepository(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    val devices: StateFlow<List<AndroidDevice>> = devicesStateFlow()

    init {
        scope.launch {
            startServer()
        }
    }

    private suspend fun startServer() {
        runAdb("start-server")
    }

    suspend fun refresh() {
        runAdb("kill-server")
        runAdb("start-server")
    }

    suspend fun reboot(device: AndroidDevice) {
        runAdb("-s", device.id, "reboot")
    }

    suspend fun screenshot(device: AndroidDevice): File? {
        return try {
            val process = withContext(Dispatchers.IO) {
                ProcessBuilder("adb", "-s", device.id, "exec-out", "screencap", "-p")
                    .redirectErrorStream(false)
                    .start()
            }
            val bytes = process.inputStream.readBytes()
            withContext(Dispatchers.IO) {
                process.waitFor()
            }
            if (bytes.isEmpty()) return null
            val desktop = File(System.getProperty("user.home"), "Desktop")
            val timestamp = System.currentTimeMillis()
            val file = File(desktop, "${device.name}_$timestamp.png")
            file.writeBytes(bytes)
            file
        } catch (e: Exception) {
            null
        }
    }

    private fun devicesStateFlow() = tickerFlow(1.seconds, 0.seconds).map {
        val devices = getDevices()
        devices
    }.stateIn(scope, SharingStarted.Lazily, emptyList())

    private suspend fun getDevices(): List<AndroidDevice> {
        val output = runAdb("devices", "-l") ?: return emptyList()
        return output.lines()
            .drop(1)
            .filter { it.trim().split("\\s+".toRegex()).getOrNull(1) == "device" }
            .map { line ->
                val parts = line.trim().split("\\s+".toRegex())
                val serial = parts[0]
                val model = line.substringAfter("model:", "").substringBefore(" ").ifEmpty { serial }
                AndroidDevice(id = serial, name = model)
            }
    }

    private suspend fun getPackages(device: AndroidDevice): List<AndroidPackage> {

        val output = runAdb("-s", device.id, "shell", "pm", "list", "packages") ?: return emptyList()
        return output.lines()
            .filter { it.startsWith("package:") }
            .map { AndroidPackage(id = it.removePrefix("package:").trim()) }
    }

    fun devicePackages(device: AndroidDevice): StateFlow<List<AndroidPackage>> = tickerFlow(1.seconds, 0.seconds).map {
        getPackages(device)
    }.stateIn(scope, SharingStarted.WhileSubscribed(1.seconds.inWholeMilliseconds), emptyList())

    suspend fun deletePackage(device: AndroidDevice, pack: AndroidPackage) {
        runAdb("-s", device.id, "uninstall", pack.id)
    }

    private suspend fun runAdb(vararg args: String): String? = withContext(Dispatchers.IO) {
        try {
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
