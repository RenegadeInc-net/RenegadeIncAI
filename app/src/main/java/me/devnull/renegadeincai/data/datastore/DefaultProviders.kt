package me.devnull.renegadeincai.data.datastore

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import me.devnull.renegadeincai.ai.provider.BalanceOption
import me.devnull.renegadeincai.ai.provider.Modality
import me.devnull.renegadeincai.ai.provider.Model
import me.devnull.renegadeincai.ai.provider.ModelAbility
import me.devnull.renegadeincai.ai.provider.ProviderSetting
import me.devnull.renegadeincai.R
import kotlin.uuid.Uuid

val DEFAULT_AUTO_MODEL_ID = Uuid.parse("b7055fb4-39f9-4042-a88a-0d80ed76cf08")

val DEFAULT_PROVIDERS = listOf(
    ProviderSetting.OpenAI(
        id = Uuid.parse("a8d2d463-e8c0-41f2-b89e-f5eb8e716cce"),
        name = "RenegadeHub",
        baseUrl = "https://api.rikka-ai.com/v1",
        apiKey = "",
        enabled = true,
        builtIn = true,
        description = {
            Text(stringResource(R.string.renegadehub_provider_description))
        },
        models = listOf(
            Model(
                id = DEFAULT_AUTO_MODEL_ID,
                modelId = "auto",
                displayName = "Auto",
                inputModalities = listOf(Modality.TEXT),
                outputModalities = listOf(Modality.TEXT),
                abilities = listOf(ModelAbility.TOOL, ModelAbility.REASONING),
            )
        )
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("1eeea727-9ee5-4cae-93e6-6fb01a4d051e"),
        name = "OpenAI",
        baseUrl = "https://api.openai.com/v1",
        apiKey = "",
        builtIn = true
    ),
    ProviderSetting.Google(
        id = Uuid.parse("6ab18148-c138-4394-a46f-1cd8c8ceaa6d"),
        name = "Gemini",
        apiKey = "",
        enabled = true,
        builtIn = true
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("1b1395ed-b702-4aeb-8bc1-b681c4456953"),
        name = "AiHubMix",
        baseUrl = "https://aihubmix.com/v1",
        apiKey = "",
        enabled = true,
        builtIn = true,
        description = {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.provider_aihubmix_desc))
                    appendLine()
                    append(stringResource(R.string.provider_website))
                    withLink(LinkAnnotation.Url("https://aihubmix.com?aff=pG7r")) {
                        withStyle(SpanStyle(MaterialTheme.colorScheme.primary)) {
                            append("https://aihubmix.com")
                        }
                    }
                    appendLine()
                    append(stringResource(R.string.provider_topup))
                    withLink(LinkAnnotation.Url("https://console.aihubmix.com/topup")) {
                        withStyle(SpanStyle(MaterialTheme.colorScheme.primary)) {
                            append("https://console.aihubmix.com/topup")
                        }
                    }
                }
            )
        },
        shortDescription = {
            Text(
                text = stringResource(R.string.provider_aihubmix_short_desc)
            )
        },
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("f099ad5b-ef03-446d-8e78-7e36787f780b"),
        name = "DeepSeek",
        baseUrl = "https://api.deepseek.com/v1",
        apiKey = "",
        builtIn = true,
        balanceOption = BalanceOption(
            enabled = true,
            apiPath = "/user/balance",
            resultPath = "balance_infos[0].total_balance"
        )
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("d5734028-d39b-4d41-9841-fd648d65440e"),
        name = "OpenRouter",
        baseUrl = "https://openrouter.ai/api/v1",
        apiKey = "",
        builtIn = true,
        balanceOption = BalanceOption(
            enabled = true,
            apiPath = "/credits",
            resultPath = "data.total_credits - data.total_usage",
        )
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("386e0f29-8228-4512-affe-8fd8add82d88"),
        name = "Vercel AI Gateway",
        baseUrl = "https://ai-gateway.vercel.sh/v1",
        apiKey = "",
        enabled = false,
        builtIn = true,
        balanceOption = BalanceOption(
            enabled = true,
            apiPath = "/credits",
            resultPath = "balance",
        )
    ),
    ProviderSetting.OpenAI(
        id = Uuid.parse("ff3cde7e-0f65-43d7-8fb2-6475c99f5990"),
        name = "xAI",
        baseUrl = "https://api.x.ai/v1",
        apiKey = "",
        enabled = false,
        builtIn = true,
        useResponseApi = true,
    ),
)
