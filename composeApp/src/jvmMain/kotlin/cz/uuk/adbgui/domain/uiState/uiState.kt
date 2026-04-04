package cz.uuk.adbgui.domain.uiState

import androidx.compose.runtime.Immutable

@Immutable
data class UiState(
    val isOpen: Boolean = false,
    val deviceList: List<UiState> =listOf(),
    val selectedDevice: String = "",
    val searchTerm: String = "",
    val packages: List<UiState> = listOf(),

)
