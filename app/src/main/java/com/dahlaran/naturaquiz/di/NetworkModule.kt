package com.dahlaran.naturaquiz.di

import com.dahlaran.naturaquiz.data.PlantService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://trefle.io/api/v1"
    private const val API_TOKEN = "YGEw3MIekubf1uUODnIo17t-Vh19Ja6nm9QwZiwXtJo"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePlantService(retrofit: Retrofit): PlantService {
        return retrofit.create(PlantService::class.java)
    }
}
