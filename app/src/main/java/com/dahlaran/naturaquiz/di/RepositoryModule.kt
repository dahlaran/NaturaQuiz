package com.dahlaran.naturaquiz.di

import com.dahlaran.naturaquiz.data.PlantRepositoryImpl
import com.dahlaran.naturaquiz.data.PlantService
import com.dahlaran.naturaquiz.domain.PlantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePlantRepository(plantService: PlantService): PlantRepository {
        return PlantRepositoryImpl(plantService)
    }
}
