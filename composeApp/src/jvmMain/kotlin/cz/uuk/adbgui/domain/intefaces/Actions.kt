package cz.uuk.adbgui.domain.intefaces

import cz.uuk.adbgui.domain.model.Device

interface Actions {
    object ToggleOpen : Actions
    object Refresh : Actions
    data class SetDevice(val device: Device) : Actions
    data class Search(val term: String) : Actions
    data class ClosePackage(val packageId: String) : Actions
}