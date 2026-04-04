package cz.uuk.adbgui.domain.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uuk.adbgui.data.AdbRepository
import cz.uuk.adbgui.presentation.screens.dashboard.DashboardUiStateUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardVM : ViewModel() {
    private val repository: AdbRepository = AdbRepository()
    private val _uiState = MutableStateFlow(DashboardUiStateUiState())
    val uiState: StateFlow<DashboardUiStateUiState> = _uiState

    init {
        loadDevices()
    }

    fun loadDevices() {
        viewModelScope.launch {
            repository.devices.collect { devices ->
                _uiState.update { old ->
                    if (old.selectedDevice == null && devices.isNotEmpty()) {
                        old.copy(deviceList = devices, selectedDevice = devices[0])
                    } else {
                        old.copy(deviceList = devices)
                    }
                }
            }
        }
    }

}