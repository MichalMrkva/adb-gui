package cz.uuk.adbgui.presentation.screens.dashboard

import cz.uuk.adbgui.domain.model.AndroidDevice
import cz.uuk.adbgui.domain.model.AndroidPackage

sealed interface DashboardActions {
    object ToggleOpen : DashboardActions
    object Refresh : DashboardActions
    data class SetDevice(val device: AndroidDevice) : DashboardActions
    data class Search(val term: String) : DashboardActions
    data class ClosePackage(val device: AndroidDevice, val androidPackage: AndroidPackage) : DashboardActions
    data class Reboot(val device: AndroidDevice) : DashboardActions
}