package com.loopssoftwares.chatgpt_integration

import android.graphics.ColorSpace.Model
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration.Companion.seconds

class ApiBuilder {
    private val apikey = "YOUR API HERE"
    private val config = OpenAIConfig(token = apikey, timeout = Timeout(socket = 60.seconds))
    private val openAiApi = OpenAI(config)

    @OptIn(BetaOpenAI::class)
    fun sendMessageRequestToApi(message: String): Flow<ChatCompletionChunk> {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = message
                )
            )
        )

        return openAiApi.chatCompletions(chatCompletionRequest)
    }
}
