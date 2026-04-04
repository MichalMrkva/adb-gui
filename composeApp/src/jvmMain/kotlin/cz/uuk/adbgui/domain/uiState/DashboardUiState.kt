package cz.uuk.adbgui.domain.uiState

import androidx.compose.runtime.Immutable
import cz.uuk.adbgui.domain.model.Device
import cz.uuk.adbgui.domain.model.Package

@Immutable
data class DashboardUiStateUiState(
    val isOpen: Boolean = false,
    val deviceList: List<Device> = listOf(),
    val selectedDevice: Device? = null,
    val searchTerm: String = "",
    val packages: List<Package> = listOf(),
)
