package com.loopssoftwares.chatgpt_integration.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.aallam.openai.api.BetaOpenAI
import com.loopssoftwares.chatgpt_integration.ApiBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val api: ApiBuilder): ViewModel() {

    private val _apiResponse = MutableStateFlow<String?>(null)
    val apiResponse: StateFlow<String?>
        get() = _apiResponse.asStateFlow()

    @OptIn(BetaOpenAI::class)
    fun getApiResponse(request: String) {
        viewModelScope.launch {
            try {
                val response = api.sendMessageRequestToApi(request)
                val stringBuilder = StringBuilder()

                response.collect {chuck ->
                    val choices = chuck.choices

                    if (choices.isNotEmpty()) {
                        val completion = choices.last()
                        val delta = completion.delta

                        if (delta?.content != null) {
                            stringBuilder.append(delta.content.toString())
                            _apiResponse.value = stringBuilder.toString()
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("OPEN API ERROR: ", e.message.toString())
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val api = ApiBuilder()
                return HomeViewModel(
                    api = api
                ) as T
            }
        }
    }

}