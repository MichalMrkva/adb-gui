package cz.uuk.adbgui.data

import cz.uuk.adbgui.domain.model.Device
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdbRepository {
    private val devices = MutableStateFlow<List<Device>>(emptyList())
    fun getDevices(): StateFlow<List<Device>> = devices

    private fun startServer() {}
}