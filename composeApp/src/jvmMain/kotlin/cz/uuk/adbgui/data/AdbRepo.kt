package cz.uuk.adbgui.data

import cz.uuk.adbgui.domain.model.Device
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdbRepository {
    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices

    private fun startServer() {
        getDevices()
    }

    private fun getDevices() {}

    fun devicePackages(device: Device): StateFlow<List<Package>> {
        val flow = MutableStateFlow<List<Package>>(emptyList())
        return flow
    }
}