package cz.uuk.adbgui.domain.intefaces

interface Actions {
    object ToggleOpen : Actions
    object Refresh : Actions
    data class SetDevice(val device: String): Actions
    data class Search(val term: String) : Actions
    data class ClosePackage(val packageId: String): Actions

}