package cz.uuk.adbgui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cz.uuk.adbgui.domain.vm.DashboardVM
import cz.uuk.adbgui.presentation.screens.dashboard.DashboardPage

@Composable
@Preview
fun App() {
    val vm = DashboardVM()
    DashboardPage(vm)
}