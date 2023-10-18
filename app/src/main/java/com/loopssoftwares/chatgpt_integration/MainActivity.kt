package com.loopssoftwares.chatgpt_integration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.loopssoftwares.chatgpt_integration.data.HomeViewModel
import dev.tontech.api_openai_integration_sample.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val viewModel: HomeViewModel by viewModels { HomeViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnSearch?.setOnClickListener {
            val inputMessage: String = binding?.etTextInputUser?.text.toString()

            if (inputMessage.isNotEmpty()) {
                makeRequestToChatGPT(inputMessage)
            } else {
                Toast.makeText(this@MainActivity, "Pesquisa invalida", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeRequestToChatGPT(message: String) {
        lifecycleScope.launch {
            viewModel.getApiResponse(message)
            viewModel.apiResponse.onEach {  response ->
                if (response != null) {
                    binding?.tvResponseChatGpt?.text = response
                }
            }.launchIn(lifecycleScope)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}