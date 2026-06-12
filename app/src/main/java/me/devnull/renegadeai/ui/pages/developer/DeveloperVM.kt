package me.devnull.renegadeai.ui.pages.developer

import androidx.lifecycle.ViewModel
import me.devnull.renegadeai.data.ai.AILoggingManager

class DeveloperVM(
    private val aiLoggingManager: AILoggingManager
) : ViewModel() {
    val logs = aiLoggingManager.getLogs()
}
