package com.sample.mainapplication.networking

import com.sample.mainapplication.model.MainData
import com.sample.mainapplication.model.MainResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import javax.inject.Inject

class MainRemoteDataSource @Inject constructor(){

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val mainService: MainService = retrofit.create(MainService::class.java)

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
    }
}

interface MainService {

    @GET("pokemon")
    suspend fun getMyDataList(): Response<MainResponse>

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(name: String): MainData
}