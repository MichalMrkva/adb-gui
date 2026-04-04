package cz.uuk.adbgui.domain.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uuk.adbgui.data.AdbRepository
import cz.uuk.adbgui.domain.model.AndroidDevice
import cz.uuk.adbgui.domain.model.AndroidPackage
import cz.uuk.adbgui.presentation.screens.dashboard.DashboardActions
import cz.uuk.adbgui.presentation.screens.dashboard.DashboardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardVM : ViewModel() {
    private val repository: AdbRepository = AdbRepository()
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    init {
        loadDevices()
    }

    private fun loadDevices() {
        viewModelScope.launch {
            repository.devices.collect { devices ->
                _uiState.update { old ->
                    if (old.selectedDevice == null && devices.isNotEmpty()) {
                        getPackages(devices[0])
                        old.copy(deviceList = devices, selectedDevice = devices[0])
                    } else {
                        old.copy(deviceList = devices)
                    }
                }
            }
        }
    }

    private fun getPackages(device: AndroidDevice) {
        viewModelScope.launch {
            repository.devicePackages(device).collect { packages -> _uiState.update { it.copy(packages = packages) } }
        }

    }

    fun onDashboardAction(action: DashboardActions) {
        when (action) {
            is DashboardActions.ClosePackage -> closePackage(action.device, action.androidPackage)
            is DashboardActions.Search -> TODO()
            is DashboardActions.Refresh -> TODO()
            is DashboardActions.SetDevice -> setDevice(action.device)
            is DashboardActions.ToggleOpen -> toggleOpen()
        }
    }

    private fun closePackage(device: AndroidDevice, androidPackage: AndroidPackage) {
        viewModelScope.launch {
            repository.deletePackage(device, androidPackage)
        }
    }

    private fun setDevice(device: AndroidDevice) {
        _uiState.update { it.copy(selectedDevice = device) }
    }

    private fun toggleOpen() {
        _uiState.update { it.copy(isOpen = !it.isOpen) }
    }

    private fun search(term: String) {
        viewModelScope.launch {}
    }

    private fun refresh() {

    }


}