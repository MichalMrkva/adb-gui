package cz.uuk.adbgui.data

import cz.uuk.adbgui.domain.model.Device
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class AdbRepository(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices

    private fun startServer() {
        getDevices()
    }

    private fun getDevices() {}

    private fun getPackages(device: Device): List<Package> {
        return emptyList()
    }

    suspend fun devicePackages(device: Device): StateFlow<List<Package>> = tickerFlow(1.seconds).map {
        getPackages(device)
    }.stateIn(scope)


    fun deletePackage(device: Device, pack: Package) {

    }
}


fun tickerFlow(period: Duration, initialDelay: Duration = period) = flow {
    delay(initialDelay)
    while (true) {
        emit(Unit)
        delay(period)
    }
}