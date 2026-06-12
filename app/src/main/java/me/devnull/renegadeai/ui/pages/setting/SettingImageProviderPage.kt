package me.devnull.renegadeai.ui.pages.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import me.rerere.ai.provider.ImageProviderSetting
import me.devnull.renegadeai.ui.components.ui.decodeImageProviderSetting
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.Add01
import me.devnull.renegadeai.R
import me.devnull.renegadeai.Screen
import me.devnull.renegadeai.ui.components.nav.BackButton
import me.devnull.renegadeai.ui.components.ui.AutoAIIcon
import me.devnull.renegadeai.ui.context.LocalNavController
import org.koin.androidx.compose.koinViewModel
import kotlin.uuid.Uuid
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SettingImageProviderPage(vm: SettingVM = koinViewModel()) {
    val settings by vm.settings.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    var search by remember { mutableStateOf("") }

    val filtered = remember(settings.imageProviders, search) {
        if (search.isBlank()) settings.imageProviders else settings.imageProviders.filter { it.name.contains(search, ignoreCase = true) }
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showAddDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var importText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text(stringResource(R.string.setting_provider_page_title)) },
                navigationIcon = { BackButton() },
                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                actions = {
                    IconButton(onClick = { showImportDialog = true }) {
                        Icon(HugeIcons.Share01, "Import")
                    }
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(HugeIcons.Add01, "Add")
                    }
                }
            )
        }
    ) { inner ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(inner)
            .imePadding()) {
            LazyColumn(contentPadding = PaddingValues(16.dp), state = rememberLazyListState()) {
                items(filtered, key = { it.id }) { provider ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (provider.enabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.errorContainer
                        ),
                        onClick = { navController.navigate(Screen.SettingImageProviderDetail(providerId = provider.id.toString())) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            AutoAIIcon(provider.name)
                            Text(text = provider.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = "${provider.models.size} models", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(stringResource(R.string.setting_provider_page_add_provider)) },
            text = {
                Column {
                    Text(stringResource(R.string.setting_provider_page_add_provider))
                    // simple type choices
                    ImageProviderTypeChooser(onChoose = { type ->
                        val newProvider: ImageProviderSetting = when (type) {
                            "OpenAI" -> ImageProviderSetting.OpenAI()
                            "Google" -> ImageProviderSetting.Google()
                            "Claude" -> ImageProviderSetting.Claude()
                            "AUTOMATIC1111" -> ImageProviderSetting.Automatic1111()
                            "ComfyUI" -> ImageProviderSetting.ComfyUI()
                            "Swarm" -> ImageProviderSetting.Swarm()
                            else -> ImageProviderSetting.Automatic1111()
                        }
                        val newSettings = settings.copy(imageProviders = settings.imageProviders + newProvider)
                        vm.updateSettings(newSettings)
                        showAddDialog = false
                    })
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddDialog = false }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }

    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = { Text(stringResource(R.string.setting_provider_page_import)) },
            text = {
                Column {
                    OutlinedTextField(value = importText, onValueChange = { importText = it }, label = { Text("Paste encoded provider string") })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    try {
                        val provider = decodeImageProviderSetting(importText.trim())
                        val newSettings = settings.copy(imageProviders = settings.imageProviders + provider)
                        vm.updateSettings(newSettings)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    showImportDialog = false
                }) { Text(stringResource(R.string.setting_provider_page_import)) }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }
}

@Composable
private fun ImageProviderTypeChooser(onChoose: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = { onChoose("OpenAI") }, modifier = Modifier.fillMaxWidth()) { Text("OpenAI Images") }
        Button(onClick = { onChoose("Google") }, modifier = Modifier.fillMaxWidth()) { Text("Google Images") }
        Button(onClick = { onChoose("Claude") }, modifier = Modifier.fillMaxWidth()) { Text("Claude Images") }
        Button(onClick = { onChoose("AUTOMATIC1111") }, modifier = Modifier.fillMaxWidth()) { Text("AUTOMATIC1111") }
        Button(onClick = { onChoose("ComfyUI") }, modifier = Modifier.fillMaxWidth()) { Text("ComfyUI") }
        Button(onClick = { onChoose("Swarm") }, modifier = Modifier.fillMaxWidth()) { Text("Swarm") }
        Spacer(Modifier.height(4.dp))
    }
}
