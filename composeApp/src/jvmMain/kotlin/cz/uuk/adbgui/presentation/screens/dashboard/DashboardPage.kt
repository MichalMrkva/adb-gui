package cz.uuk.adbgui.presentation.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DashboardPage() {
    //val state by vm.uiState.collectAsStateWithLifecycle()
    DashboardContent()

}

@Preview
@Composable
internal fun DashboardContent() {

    var packageName = androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf("")
    }

    var expanded = androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(false)
    }

    var selectedDevice = androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf("Select device")
    }

    val devices = listOf("Device 1", "Device 2", "Device 3")

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // TITLE
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = { /* refresh logic */ }
                ) {
                    Text("Refresh server")
                }

                // Dropdown
                Box {
                    OutlinedButton(
                        onClick = { expanded.value = true }
                    ) {
                        Text(selectedDevice.value)
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
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = packageName.value,
                    onValueChange = { packageName.value = it },
                    label = { Text("Package") }
                )
            }
        }
    }
}