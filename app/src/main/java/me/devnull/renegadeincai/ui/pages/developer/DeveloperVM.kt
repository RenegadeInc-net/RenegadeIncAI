package me.devnull.renegadeincai.ui.pages.developer

import androidx.lifecycle.ViewModel
import me.devnull.renegadeincai.data.ai.AILoggingManager

class DeveloperVM(
    private val aiLoggingManager: AILoggingManager
) : ViewModel() {
    val logs = aiLoggingManager.getLogs()
}
