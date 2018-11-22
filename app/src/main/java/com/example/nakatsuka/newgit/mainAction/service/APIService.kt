package com.example.nakatsuka.newgit.mainAction.service

import com.example.nakatsuka.newgit.mainAction.model.api.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface APIService{
    @GET("user/rule")
    fun rule(): Call<RuleResponse>

    @POST("user/create")
    fun user(@Body request: UserRequest): Call<UserResponse>

    @POST("stamp/image")
    fun image(@Body request: ImageRequest, @Header("uuid") uuid: String): Call<ImageResponse>

    @POST("stamp/judge")
    fun answer(@Body request: AnswerRequest, @Header("uuid") uuid: String): Call<AnswerResponse>

    @POST("user/goal")
    fun goal(@Header("uuid") uuid: String): Call<GoalResponse>

}

class APIClient {
    companion object {
        private const val BASE_URL = "https://nitfes2018.ske.nitech.ac.jp/knoom/"

        val instance: APIService by lazy {
            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()

            retrofit.create(APIService::class.java)
        }
    }
}
