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
            repository.devicePackages(device)
                .collect { packages -> _uiState.update { it.copy(packagesFiltered = packages, packages = packages) } }
        }

    }

    fun onDashboardAction(action: DashboardActions) {
        when (action) {
            is DashboardActions.ClosePackage -> closePackage(action.device, action.androidPackage)
            is DashboardActions.Search -> search(action.term)
            is DashboardActions.Refresh -> refresh()
            is DashboardActions.SetDevice -> setDevice(action.device)
            is DashboardActions.ToggleOpen -> toggleOpen()
            is DashboardActions.Reboot -> reboot(action.device)
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
        val packages = _uiState.value.packages
        if (term.isBlank()) {
            _uiState.update { it.copy(searchTerm = term, packagesFiltered = it.packages) }
            return
        }
        val pattern = Regex.escape(term)
        val regex = Regex(pattern, RegexOption.IGNORE_CASE)

        val filteredPackages = packages.filter { pkg ->
            regex.containsMatchIn(pkg.id)
        }
        _uiState.update { it.copy(packagesFiltered = filteredPackages, searchTerm = term) }
    }

    private fun refresh() {
        viewModelScope.launch {
            repository.refresh()
        }

    }

    private fun reboot(device: AndroidDevice) {
        viewModelScope.launch {
            repository.reboot(device)
        }

    }


}