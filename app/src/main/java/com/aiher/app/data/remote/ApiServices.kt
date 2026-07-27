package com.aiher.app.data.remote

import com.aiher.app.data.model.ChatCompletionRequest
import com.aiher.app.data.model.ChatCompletionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AIChatApi {

    @POST("v1/chat/completions")
    suspend fun chatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: ChatCompletionRequest
    ): Response<ChatCompletionResponse>

    @GET("v1/models")
    suspend fun listModels(
        @Header("Authorization") authorization: String
    ): Response<Map<String, Any>>
}

interface MarketApi {

    @GET("api/apps")
    suspend fun getMarketApps(
        @Query("category") category: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<List<com.aiher.app.data.model.MarketApp>>

    @GET("api/apps/{id}")
    suspend fun getAppDetail(
        @Path("id") appId: String
    ): Response<com.aiher.app.data.model.MarketApp>

    @GET("api/apps/search")
    suspend fun searchApps(
        @Query("q") query: String
    ): Response<List<com.aiher.app.data.model.MarketApp>>
}

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(
        @Body body: Map<String, String>
    ): Response<com.aiher.app.data.model.User>

    @POST("api/auth/register")
    suspend fun register(
        @Body body: Map<String, String>
    ): Response<com.aiher.app.data.model.User>
}