package cz.uuk.adbgui.presentation.screens.dashboard

// for image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cross
import cz.uuk.adbgui.domain.vm.DashboardVM
import java.io.File
import org.jetbrains.skia.Image as SkiaImage

@Composable
fun DashboardPage(vm: DashboardVM) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    DashboardContent(
        state = state,
        onAction = vm::onDashboardAction
    )

}

@Preview
@Composable
internal fun DashboardContent(
    state: DashboardUiState = DashboardUiState(),
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

                Button(onClick = { onAction(DashboardActions.Refresh) }) {
                    Text("Refresh server")
                }
                Button(onClick = { onAction(DashboardActions.Reboot) }) {
                    Text("Reboot")
                }
                Button(onClick = { onAction(DashboardActions.Screenshot) }) {
                    Text("Screenshot")
                }

                Box {
                    OutlinedButton(
                        onClick = { onAction(DashboardActions.ToggleOpen) }
                    ) {
                        Text(state.selectedDevice?.name ?: "")
                    }

                    DropdownMenu(
                        expanded = state.isOpen,
                        onDismissRequest = { onAction(DashboardActions.ToggleOpen) }
                    ) {
                        state.deviceList.forEach { device ->
                            DropdownMenuItem(
                                text = { Text(device.name) },
                                onClick = {
                                    onAction(DashboardActions.SetDevice(device))
                                    onAction(DashboardActions.ToggleOpen)
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
                    value = state.searchTerm,
                    onValueChange = { onAction(DashboardActions.Search(it)) },
                    label = { Text("Package") }
                )

                // LazyColumn side
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.packagesFiltered) { pkg ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Package name
                                Text(
                                    text = pkg.id,
                                    modifier = Modifier.padding(12.dp)
                                )

                                // Delete button
                                IconButton(
                                    onClick = {
                                        onAction(DashboardActions.ClosePackage(state.selectedDevice!!, pkg))
                                    }
                                ) {
                                    Icon(
                                        imageVector = FontAwesomeIcons.Solid.Cross,
                                        contentDescription = "Delete package"
                                    )
                                }
                            }
                        }
                    }
                }

                // IMAGE PREVIEW AREA
                FileImage(
                    imagePath = state.file,
                    onRemove = { onAction(DashboardActions.OnHide) }
                )
            }
        }
    }
}


@Composable
fun FileImage(
    imagePath: File?,
    onRemove: () -> Unit
) {
    if (imagePath == null) return

    val imageBitmap = remember(imagePath) {
        try {
            val bytes = imagePath.readBytes()
            SkiaImage.makeFromEncoded(bytes).asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    imageBitmap?.let { bitmap ->

        Box(
            modifier = Modifier.padding(16.dp)
        ) {

            Image(
                bitmap = bitmap,
                contentDescription = "Selected image",
                modifier = Modifier.size(250.dp)
            )

            // remove button
            IconButton(
                onClick = onRemove,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Cross,
                    contentDescription = "Remove image"
                )
            }
        }
    }
}