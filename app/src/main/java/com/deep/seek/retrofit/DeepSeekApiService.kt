package com.deep.seek.retrofit

import com.deep.seek.models.ChatRequest
import com.deep.seek.models.ChatResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DeepSeekApiService {

    @POST("chat/completions")
    suspend fun getChatCompletion(
        @Header("Authorization") auth: String,
        @Body request: ChatRequest): ChatResponse
}