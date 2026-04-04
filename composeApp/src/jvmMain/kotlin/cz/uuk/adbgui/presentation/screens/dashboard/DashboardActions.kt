package cz.uuk.adbgui.presentation.screens.dashboard

import cz.uuk.adbgui.domain.model.Device

interface DashboardActions {
    object ToggleOpen : DashboardActions
    object Refresh : DashboardActions
    data class SetDevice(val device: Device) : DashboardActions
    data class Search(val term: String) : DashboardActions
    data class ClosePackage(val packageId: String) : DashboardActions
}