package com.deep.seek

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deep.seek.models.ChatRequest
import com.deep.seek.models.Message
import com.deep.seek.models.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> get() = _messages

    private var isRequestInProgress = false // ✅ Prevent multiple simultaneous API calls

    fun sendMessage(userMessage: String) {
        if (userMessage.isBlank() || isRequestInProgress) return // ✅ Prevent empty messages & multiple requests

        isRequestInProgress = true // ✅ Mark request in progress
        viewModelScope.launch {
            val newMessage = Message(role = "user", content = userMessage)

            // ✅ Prevent duplicates before adding the message
            if (!_messages.value.orEmpty().contains(newMessage)) {
                _messages.value = _messages.value.orEmpty().plus(newMessage)
            }

            val chatRequest = ChatRequest(
                model = "deepseek-chat",
                messages = listOf(
                    Message(role = "system", content = "You are a helpful assistant."),
                    newMessage
                ),
                stream = false
            )

            Log.d("DEEPSEEK TAG", "Sending message to API: $userMessage") // ✅ Debugging

            try {
                val response = RetrofitClient.instance.getChatCompletion(
                    "Bearer sk-8a2ca54d2ebd4d87a2113c9e10d82561", chatRequest
                )

                val assistantMessage = response.choices[0].message

                if (assistantMessage.content.isNotBlank()) {
                    simulateTypingEffect(assistantMessage)
                }

            } catch (e: Exception) {
                Log.e("DEEPSEEK TAG", "Exception: ${e.localizedMessage}")
                simulateTypingEffect(Message(role = "assistant", content = "Sorry, something went wrong."))
            } finally {
                isRequestInProgress = false // ✅ Reset flag
            }
        }
    }

    private fun simulateTypingEffect(fullMessage: Message) {
        viewModelScope.launch(Dispatchers.Main) {
            var typedText = ""
            if (fullMessage.content.isNotBlank()) {
                for (char in fullMessage.content) {
                    typedText += char

                    // ✅ Remove old unfinished assistant messages
                    val filteredMessages = _messages.value.orEmpty().filter { it.role != "assistant" }

                    _messages.value = filteredMessages + Message(role = "assistant", content = typedText)
                    delay(30) // ✅ Typing effect delay
                }
            }
        }
    }
}


