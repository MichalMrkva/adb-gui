package cz.uuk.adbgui.presentation.screens.dashboard

import androidx.compose.runtime.Immutable
import cz.uuk.adbgui.domain.model.AndroidDevice
import cz.uuk.adbgui.domain.model.AndroidPackage
import java.io.File

@Immutable
data class DashboardUiState(
    val isOpen: Boolean = false,
    val deviceList: List<AndroidDevice> = listOf(),
    val selectedDevice: AndroidDevice? = null,
    val searchTerm: String = "",
    val packages: List<AndroidPackage> = listOf(),
    val packagesFiltered: List<AndroidPackage> = listOf(),
    val file: File? = null,
)
