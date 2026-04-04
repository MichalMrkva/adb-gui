package cz.uuk.adbgui.presentation.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.uuk.adbgui.domain.vm.DashboardVM

@Composable
fun DashboardPage(vm: DashboardVM) {
    //val state by vm.uiState.collectAsStateWithLifecycle()
    val state = remember { DashboardUiStateUiState() }
    DashboardContent(
        state = state,
    )

}

@Preview
@Composable
internal fun DashboardContent(
    state: DashboardUiStateUiState = DashboardUiStateUiState(),
    onAction: (DashboardActions) -> Unit = {},
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ---------- TITLE ----------
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ADB GUI demo",
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            // ---------- ROW 1 ----------
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(onClick = { }) {
                    Text("Refresh server")
                }

                Box {
                    OutlinedButton(
                        onClick = { onAction(DashboardActions.ToggleOpen) }
                    ) {
                        Text(state.selectedDevice?.name ?: "")
                    }

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        devices.forEach { device ->
                            DropdownMenuItem(
                                text = { Text(device) },
                                onClick = {
                                    selectedDevice.value = device
                                    expanded.value = false
                                }
                            )
                        }
                    }
                }
            }

            // ---------- ROW 2 ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // takes remaining screen height
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // TextField side
                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    value = packageName.value,
                    onValueChange = { packageName.value = it },
                    label = { Text("Package") }
                )

                // LazyColumn side
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(packages) { pkg ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = pkg,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}