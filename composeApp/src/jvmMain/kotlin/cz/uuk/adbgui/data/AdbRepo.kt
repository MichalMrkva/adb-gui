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

    private fun startServer() {
        getDevices()
    }

    fun refresh() {}

    private fun devicesStateFlow() = tickerFlow(1.seconds, 0.seconds).map {
        getDevices()
    }.stateIn(scope, SharingStarted.Lazily, emptyList())

    private fun getDevices(): List<AndroidDevice> {
        return emptyList()
    }

    private fun getPackages(device: AndroidDevice): List<AndroidPackage> {
        return emptyList()
    }

    fun devicePackages(device: AndroidDevice): StateFlow<List<AndroidPackage>> = tickerFlow(1.seconds, 0.seconds).map {
        getPackages(device)
    }.stateIn(scope, SharingStarted.WhileSubscribed(1.seconds.inWholeMilliseconds), emptyList())


    fun deletePackage(device: AndroidDevice, pack: AndroidPackage) {}
}
