package cz.uuk.adbgui.domain.vm

import cz.uuk.adbgui.domain.uiState.DashboardUiStateUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VM {
    private val _uiState = MutableStateFlow(DashboardUiStateUiState())
    val uiState: StateFlow<DashboardUiStateUiState> = _uiState

}