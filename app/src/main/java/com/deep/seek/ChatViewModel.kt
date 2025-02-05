package com.deep.seek

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deep.seek.models.ChatRequest
import com.deep.seek.models.Message
import com.deep.seek.models.RetrofitClient
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    fun sendMessage(userMessage: String) {

        viewModelScope.launch {

            val chatRequest = ChatRequest(
                model = "deepseek-chat",
                messages = listOf(
                    Message(role = "system", content = "You are a helpful assistant."),
                    Message(role = "user", content = userMessage)
                ),
                stream = false
            )

            try {
                val response = RetrofitClient.instance.getChatCompletion("Bearer sk-8a2ca54d2ebd4d87a2113c9e10d82561", chatRequest)
                val assistantMessage = response.choices[0].message
                _messages.value = _messages.value.orEmpty() + listOf(
                    Message(role = "user", content = userMessage),
                    assistantMessage
                )
            } catch (e: Exception) {
                Log.d("DEEPSEEK TAG","Exception : " + e.localizedMessage)
            }
        }
    }
}