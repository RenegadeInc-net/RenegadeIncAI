package me.devnull.renegadeincai.ui.pages.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.devnull.renegadeincai.R
import me.devnull.renegadeincai.ai.provider.ImageProviderSetting
import me.devnull.renegadeincai.ui.components.nav.BackButton
import me.devnull.renegadeincai.ui.context.LocalNavController
import org.koin.androidx.compose.koinViewModel
import kotlin.uuid.Uuid

@Composable
fun SettingImageProviderDetailPage(id: Uuid, vm: SettingVM = koinViewModel()) {
    val settings by vm.settings.collectAsStateWithLifecycle()
    val provider = settings.imageProviders.find { it.id == id }
    val navController = LocalNavController.current

    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text(text = provider?.name ?: "Unknown") },
                navigationIcon = { BackButton() },
                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            )
        }
    ) { inner ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(inner)
            .padding(16.dp)) {
            provider?.let {
                Text(text = "Provider Details for ${it.name}", style = MaterialTheme.typography.headlineMedium)
                // TODO: Add full configuration UI
            }
        }
    }
}
