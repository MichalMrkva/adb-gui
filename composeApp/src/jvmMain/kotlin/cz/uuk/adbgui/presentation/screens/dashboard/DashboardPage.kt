package cz.uuk.adbgui.presentation.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DashboardPage() {
    //val state by vm.uiState.collectAsStateWithLifecycle()
    DashboardContent()

}

@Preview
@Composable
internal fun DashboardContent(
    //state: DashboardUiState = DashboardUiState(),
    //onAction: (DashboardUiAction) -> Unit = {},
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {}
    }
}