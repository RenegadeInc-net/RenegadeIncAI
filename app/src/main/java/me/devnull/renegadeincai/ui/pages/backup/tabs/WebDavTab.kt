package me.devnull.renegadeincai.ui.pages.backup.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dokar.sonner.ToastType
import kotlinx.coroutines.launch
import me.devnull.renegadeincai.R
import me.devnull.renegadeincai.data.sync.webdav.WebDavBackupItem
import me.devnull.renegadeincai.ui.components.ui.CardGroup
import me.devnull.renegadeincai.ui.context.LocalToaster
import me.devnull.renegadeincai.ui.pages.backup.BackupVM
import me.devnull.renegadeincai.utils.UiState
import me.devnull.renegadeincai.utils.fileSizeToString
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.Delete02
import me.rerere.hugeicons.stroke.Download02
import me.rerere.hugeicons.stroke.Upload02
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun WebDavTab(
    vm: BackupVM,
    onShowRestartDialog: () -> Unit,
) {
    val settings by vm.settings.collectAsStateWithLifecycle()
    val webDavConfig = settings.webDavConfig
    val backupItemsState by vm.webDavBackupItems.collectAsStateWithLifecycle()
    val toaster = LocalToaster.current
    val scope = rememberCoroutineScope()
    var showBackupFiles by remember { mutableStateOf(false) }
    var isBackingUp by remember { mutableStateOf(false) }
    var restoringItemId by remember { mutableStateOf<String?>(null) }

    val connectionSuccessMessage = stringResource(R.string.backup_page_connection_success)
    val connectionFailedMessage = stringResource(R.string.backup_page_connection_failed)
    val backupSuccessMessage = stringResource(R.string.backup_page_backup_success)
    val unknownErrorMessage = stringResource(R.string.backup_page_unknown_error)
    val deleteSuccessMessage = stringResource(R.string.backup_page_delete_success)
    val deleteFailedMessage = stringResource(R.string.backup_page_delete_failed)
    val restoreSuccessMessage = stringResource(R.string.backup_page_restore_success)
    val restoreFailedMessage = stringResource(R.string.backup_page_restore_failed)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = webDavConfig.url,
            onValueChange = { vm.updateSettings(settings.copy(webDavConfig = webDavConfig.copy(url = it))) },
            label = { Text("URL") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("https://example.com/dav") }
        )

        OutlinedTextField(
            value = webDavConfig.username,
            onValueChange = { vm.updateSettings(settings.copy(webDavConfig = webDavConfig.copy(username = it))) },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = webDavConfig.password,
            onValueChange = { vm.updateSettings(settings.copy(webDavConfig = webDavConfig.copy(password = it))) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            OutlinedButton(
                onClick = {
                    scope.launch {
                        try {
                            vm.testWebDav()
                            toaster.show(
                                connectionSuccessMessage,
                                type = ToastType.Success
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            toaster.show(
                                connectionFailedMessage.format(
                                    e.message ?: ""
                                ),
                                type = ToastType.Error
                            )
                        }
                    }
                }
            ) {
                Text(stringResource(R.string.backup_page_test_connection))
            }
            OutlinedButton(
                onClick = {
                    vm.loadBackupFileItems()
                    showBackupFiles = true
                }
            ) {
                Text(stringResource(R.string.backup_page_restore))
            }

            Button(
                onClick = {
                    scope.launch {
                        isBackingUp = true
                        runCatching {
                            vm.backup()
                            vm.loadBackupFileItems()
                            toaster.show(
                                backupSuccessMessage,
                                type = ToastType.Success
                            )
                        }.onFailure {
                            it.printStackTrace()
                            toaster.show(
                                it.message ?: unknownErrorMessage,
                                type = ToastType.Error
                            )
                        }
                        isBackingUp = false
                    }
                },
                enabled = !isBackingUp
            ) {
                if (isBackingUp) {
                    CircularWavyProgressIndicator(
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Icon(HugeIcons.Upload02, contentDescription = null, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    if (isBackingUp) {
                        stringResource(R.string.backup_page_backing_up)
                    } else {
                        stringResource(R.string.backup_page_backup_now)
                    }
                )
            }
        }
    }

    if (showBackupFiles) {
        ModalBottomSheet(
            onDismissRequest = {
                showBackupFiles = false
            },
            sheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden, enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.backup_page_webdav_backup_files),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge
                )
                when (val state = backupItemsState) {
                    is UiState.Success -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.data) { item ->
                                WebDavBackupItemCard(
                                    item = item,
                                    isRestoring = restoringItemId == item.displayName,
                                    onDelete = {
                                        scope.launch {
                                            runCatching {
                                                vm.deleteWebDavBackupFile(item)
                                                toaster.show(
                                                    deleteSuccessMessage,
                                                    type = ToastType.Success
                                                )
                                                vm.loadBackupFileItems()
                                            }.onFailure { err ->
                                                err.printStackTrace()
                                                toaster.show(
                                                    deleteFailedMessage.format(
                                                        err.message ?: ""
                                                    ),
                                                    type = ToastType.Error
                                                )
                                            }
                                        }
                                    },
                                    onRestore = { restoreItem ->
                                        scope.launch {
                                            restoringItemId = restoreItem.displayName
                                            runCatching {
                                                vm.restore(item = restoreItem)
                                                toaster.show(
                                                    restoreSuccessMessage,
                                                    type = ToastType.Success
                                                )
                                                showBackupFiles = false
                                                onShowRestartDialog()
                                            }.onFailure { err ->
                                                err.printStackTrace()
                                                toaster.show(
                                                    restoreFailedMessage.format(
                                                        err.message ?: ""
                                                    ),
                                                    type = ToastType.Error
                                                )
                                            }
                                            restoringItemId = null
                                        }
                                    },
                                )
                            }
                        }
                    }
                    is UiState.Error -> {
                        Text(
                            text = state.error.message ?: unknownErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun WebDavBackupItemCard(
    item: WebDavBackupItem,
    isRestoring: Boolean = false,
    onDelete: (WebDavBackupItem) -> Unit = {},
    onRestore: (WebDavBackupItem) -> Unit = {},
) {
    val formatter = remember {
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())
    }

    CardGroup {
        item(
            headlineContent = {
                Text(
                    text = item.displayName,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingContent = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.size.fileSizeToString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formatter.format(item.lastModified),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onDelete(item) },
                            enabled = !isRestoring
                        ) {
                            Icon(
                                imageVector = HugeIcons.Delete02,
                                contentDescription = stringResource(R.string.delete),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }

                        IconButton(
                            onClick = { onRestore(item) },
                            enabled = !isRestoring
                        ) {
                            if (isRestoring) {
                                CircularWavyProgressIndicator(
                                    modifier = Modifier.size(18.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = HugeIcons.Download02,
                                    contentDescription = stringResource(R.string.backup_page_restore)
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
