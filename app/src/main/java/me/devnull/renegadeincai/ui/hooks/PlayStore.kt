package me.devnull.renegadeincai.ui.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import me.devnull.renegadeincai.utils.PlayStoreUtil

@Composable
fun rememberIsPlayStoreVersion(): Boolean {
    val context = LocalContext.current
    return remember {
        PlayStoreUtil.isInstalledFromPlayStore(context)
    }
}