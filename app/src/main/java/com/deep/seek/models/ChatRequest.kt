package com.deep.seek.models

data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val stream: Boolean
)

data class Message(
    val role: String,
    val content: String
)

data class ChatResponse(
    val id: String,
    val created: Long,
    val choices: List<Choice>,
    val usage: Usage
)

data class Choice(
    val index: Int,
    val message: Message,
    val finish_reason: String
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)