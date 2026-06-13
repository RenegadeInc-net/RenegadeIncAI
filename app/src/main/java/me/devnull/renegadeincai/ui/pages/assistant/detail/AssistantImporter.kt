package me.devnull.renegadeincai.ui.pages.assistant.detail

import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.devnull.renegadeincai.ai.ui.UIMessage
import me.devnull.renegadeincai.data.model.Assistant
import me.devnull.renegadeincai.data.files.FilesManager
import me.devnull.renegadeincai.ui.components.ui.AutoAIIcon
import me.devnull.renegadeincai.ui.context.LocalToaster
import me.devnull.renegadeincai.utils.ImageUtils
import me.devnull.renegadeincai.utils.jsonPrimitiveOrNull
import me.devnull.renegadeincai.R
import org.koin.compose.koinInject

@Composable
fun AssistantImporter(
    modifier: Modifier = Modifier,
    onUpdate: (Assistant) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        SillyTavernImporter(onImport = onUpdate)
    }
}

@Composable
private fun SillyTavernImporter(
    onImport: (Assistant) -> Unit
) {
    val context = LocalContext.current
    val filesManager: FilesManager = koinInject()
    val scope = rememberCoroutineScope()
    val toaster = LocalToaster.current
    var isLoading by remember { mutableStateOf(false) }

    val importFailedMessage = stringResource(R.string.assistant_importer_import_failed)
    val missingDataFieldMessage = stringResource(R.string.assistant_importer_missing_data_field)
    val missingNameFieldMessage = stringResource(R.string.assistant_importer_missing_name_field)
    val missingSpecFieldMessage = stringResource(R.string.assistant_importer_missing_spec_field)
    val readJsonFailedMessage = stringResource(R.string.assistant_importer_read_json_failed)
    val unsupportedFileTypeMessage = stringResource(R.string.assistant_importer_unsupported_file_type)
    val unsupportedSpecMessage = stringResource(R.string.assistant_importer_unsupported_spec)

    val jsonPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            isLoading = true
            scope.launch {
                try {
                    runCatching {
                        importAssistantFromUri(
                            context = context,
                            uri = uri,
                            onImport = onImport,
                            toaster = toaster,
                            filesManager = filesManager,
                            missingDataFieldMessage = missingDataFieldMessage,
                            missingNameFieldMessage = missingNameFieldMessage,
                            missingSpecFieldMessage = missingSpecFieldMessage,
                            readJsonFailedMessage = readJsonFailedMessage,
                            unsupportedFileTypeMessage = unsupportedFileTypeMessage,
                            unsupportedSpecMessage = unsupportedSpecMessage,
                            importFailedMessage = importFailedMessage
                        )
                    }.onFailure { exception ->
                        exception.printStackTrace()
                        toaster.show(exception.message ?: importFailedMessage)
                    }
                } finally {
                    isLoading = false
                }
            }
        }
    }

    val pngPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            isLoading = true
            scope.launch {
                try {
                    runCatching {
                        importAssistantFromUri(
                            context = context,
                            uri = uri,
                            onImport = onImport,
                            toaster = toaster,
                            filesManager = filesManager,
                            missingDataFieldMessage = missingDataFieldMessage,
                            missingNameFieldMessage = missingNameFieldMessage,
                            missingSpecFieldMessage = missingSpecFieldMessage,
                            readJsonFailedMessage = readJsonFailedMessage,
                            unsupportedFileTypeMessage = unsupportedFileTypeMessage,
                            unsupportedSpecMessage = unsupportedSpecMessage,
                            importFailedMessage = importFailedMessage
                        )
                    }.onFailure { exception ->
                        exception.printStackTrace()
                        toaster.show(exception.message ?: importFailedMessage)
                    }
                } finally {
                    isLoading = false
                }
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedButton(
            onClick = {
                pngPickerLauncher.launch(arrayOf("image/png"))
            },
            enabled = !isLoading
        ) {
            AutoAIIcon(name = "tavern", modifier = Modifier.padding(end = 8.dp))
            Text(text = if (isLoading) stringResource(R.string.assistant_importer_importing) else stringResource(R.string.assistant_importer_import_tavern_png))
        }

        OutlinedButton(
            onClick = {
                jsonPickerLauncher.launch(arrayOf("application/json"))
            },
            enabled = !isLoading
        ) {
            AutoAIIcon(name = "tavern", modifier = Modifier.padding(end = 8.dp))
            Text(text = if (isLoading) stringResource(R.string.assistant_importer_importing) else stringResource(R.string.assistant_importer_import_tavern_json))
        }
    }
}

// region Parsing Strategy

private interface TavernCardParser {
    val specName: String
    fun parse(json: JsonObject, background: String?, missingDataFieldMessage: String, missingNameFieldMessage: String): Assistant
}

private class CharaCardV2Parser : TavernCardParser {
    override val specName: String = "chara_card_v2"

    override fun parse(json: JsonObject, background: String?, missingDataFieldMessage: String, missingNameFieldMessage: String): Assistant {
        val data = json["data"]?.jsonObject ?: error(missingDataFieldMessage)
        val name = data["name"]?.jsonPrimitiveOrNull?.contentOrNull
            ?: error(missingNameFieldMessage)
        val firstMessage = data["first_mes"]?.jsonPrimitiveOrNull?.contentOrNull
        val system = data["system_prompt"]?.jsonPrimitiveOrNull?.contentOrNull
        val description = data["description"]?.jsonPrimitiveOrNull?.contentOrNull
        val personality = data["personality"]?.jsonPrimitiveOrNull?.contentOrNull
        val scenario = data["scenario"]?.jsonPrimitiveOrNull?.contentOrNull

        val prompt = buildString {
            appendLine("You are roleplaying as $name.")
            appendLine()
            if (!system.isNullOrBlank()) {
                appendLine(system)
                appendLine()
            }
            appendLine("## Description of the character")
            appendLine(description ?: "Empty")
            appendLine()
            appendLine("## Personality of the character")
            appendLine(personality ?: "Empty")
            appendLine()
            appendLine("## Scenario")
            append(scenario ?: "Empty")
        }

        return Assistant(
            name = name,
            presetMessages = if (firstMessage != null) listOf(UIMessage.assistant(firstMessage)) else emptyList(),
            systemPrompt = prompt,
            background = background
        )
    }
}

private class CharaCardV3Parser : TavernCardParser {
    override val specName: String = "chara_card_v3"

    override fun parse(json: JsonObject, background: String?, missingDataFieldMessage: String, missingNameFieldMessage: String): Assistant {
        val data = json["data"]?.jsonObject ?: error(missingDataFieldMessage)
        val name = data["name"]?.jsonPrimitiveOrNull?.contentOrNull ?: error(missingNameFieldMessage)
        val description = data["description"]?.jsonPrimitiveOrNull?.contentOrNull
        val firstMessage = data["first_mes"]?.jsonPrimitiveOrNull?.contentOrNull
        val system = data["system_prompt"]?.jsonPrimitiveOrNull?.contentOrNull
        val personality = data["personality"]?.jsonPrimitiveOrNull?.contentOrNull
        val scenario = data["scenario"]?.jsonPrimitiveOrNull?.contentOrNull

        val prompt = buildString {
            appendLine("You are roleplaying as $name.")
            appendLine()
            if (!system.isNullOrBlank()) {
                appendLine(system)
                appendLine()
            }
            appendLine("## Description of the character")
            appendLine(description ?: "Empty")
            appendLine()
            appendLine("## Personality of the character")
            appendLine(personality ?: "Empty")
            appendLine()
            appendLine("## Scenario")
            append(scenario ?: "Empty")
        }

        return Assistant(
            name = name,
            presetMessages = if (firstMessage != null) listOf(UIMessage.assistant(firstMessage)) else emptyList(),
            systemPrompt = prompt,
            background = background
        )
    }
}

private val TAVERN_PARSERS: Map<String, TavernCardParser> = listOf(
    CharaCardV2Parser(),
    CharaCardV3Parser()
).associateBy { it.specName }

private fun parseAssistantFromJson(
    json: JsonObject,
    background: String?,
    missingDataFieldMessage: String,
    missingNameFieldMessage: String,
    missingSpecFieldMessage: String,
    unsupportedSpecMessage: String
): Assistant {
    val spec = json["spec"]?.jsonPrimitive?.contentOrNull
        ?: error(missingSpecFieldMessage)
    val parser = TAVERN_PARSERS[spec] ?: error(unsupportedSpecMessage.format(spec))
    return parser.parse(
        json = json,
        background = background,
        missingDataFieldMessage = missingDataFieldMessage,
        missingNameFieldMessage = missingNameFieldMessage
    )
}

// endregion

private suspend fun importAssistantFromUri(
    context: Context,
    uri: Uri,
    onImport: (Assistant) -> Unit,
    toaster: ToasterState,
    filesManager: FilesManager,
    missingDataFieldMessage: String,
    missingNameFieldMessage: String,
    missingSpecFieldMessage: String,
    readJsonFailedMessage: String,
    unsupportedFileTypeMessage: String,
    unsupportedSpecMessage: String,
    importFailedMessage: String
) {
    try {
        val mime = withContext(Dispatchers.IO) { filesManager.getFileMimeType(uri) }
        val (jsonString, backgroundStr) = withContext(Dispatchers.IO) {
            when (mime) {
                "image/png" -> {
                    val result = ImageUtils.getTavernCharacterMeta(context, uri)
                    result.map { base64Data ->
                        val json = String(Base64.decode(base64Data, Base64.DEFAULT))
                        val bg = filesManager.createChatFilesByContents(listOf(uri)).first().toString()
                        json to bg
                    }.getOrElse { throw it }
                }

                "application/json" -> {
                    val json = context.contentResolver.openInputStream(uri)?.bufferedReader()
                        .use { it?.readText() }
                        ?: error(readJsonFailedMessage)
                    json to null
                }

                else -> error(unsupportedFileTypeMessage.format(mime ?: "unknown"))
            }
        }
        val json = Json.parseToJsonElement(jsonString).jsonObject
        val assistant = parseAssistantFromJson(
            json = json,
            background = backgroundStr,
            missingDataFieldMessage = missingDataFieldMessage,
            missingNameFieldMessage = missingNameFieldMessage,
            missingSpecFieldMessage = missingSpecFieldMessage,
            unsupportedSpecMessage = unsupportedSpecMessage
        )
        onImport(assistant)
    } catch (exception: Exception) {
        exception.printStackTrace()
        toaster.show(
            message = exception.message ?: importFailedMessage,
            type = ToastType.Error
        )
    }
}
