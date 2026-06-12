package me.devnull.renegadeai.ui.pages.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.Share01
import me.devnull.renegadeai.ui.components.ui.encodeForShare
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import me.devnull.renegadeai.R
import me.devnull.renegadeai.ui.components.nav.BackButton
import me.devnull.renegadeai.ui.context.LocalNavController
import org.koin.androidx.compose.koinViewModel
import kotlin.uuid.Uuid

@Composable
fun SettingImageProviderDetailPage(id: Uuid, vm: SettingVM = koinViewModel()) {
    val settings by vm.settings.collectAsStateWithLifecycle()
    val provider = settings.imageProviders.find { it.id == id } ?: return
    val navController = LocalNavController.current

    var internal by remember { mutableStateOf(provider) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(provider.name) },
                navigationIcon = { BackButton() },
                actions = {
                    val context = LocalContext.current
                    IconButton(onClick = {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, provider.encodeForShare())
                        try {
                            context.startActivity(Intent.createChooser(intent, null))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }) {
                        Icon(HugeIcons.Share01, null)
                    }
                }
            )
        }
    ) { inner ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(inner)
            .imePadding(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = stringResource(R.string.setting_provider_page_configuration))

            Button(onClick = {
                // save
                val newSettings = settings.copy(imageProviders = settings.imageProviders.map {
                    if (it.id == internal.id) internal else it
                })
                vm.updateSettings(newSettings)
                navController.popBackStack()
            }) {
                Text(stringResource(R.string.setting_provider_page_save))
            }
        }
    }
}
