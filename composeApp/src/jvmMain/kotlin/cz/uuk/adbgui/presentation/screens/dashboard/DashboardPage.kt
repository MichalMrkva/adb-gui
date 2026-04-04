package cz.uuk.adbgui.presentation.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    // fake package list
    val packages = List(20) { "com.example.app$it" }

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