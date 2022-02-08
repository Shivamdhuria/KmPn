package com.surrus.common.interactors

import com.surrus.common.domain.DataState
import com.surrus.common.domain.Dog
import com.surrus.common.domain.util.CommonFlow
import com.surrus.common.domain.util.asCommonFlow
import com.surrus.common.network.DogService
import com.surrus.common.network.models.DogDtoMapper
import kotlinx.coroutines.flow.flow

class GetDogs(private val recipeService: DogService, private val dtoMapper: DogDtoMapper) {

    @Throws(Exception::class)
    fun execute(): CommonFlow<DataState<List<Dog>>> = flow {
        try {
            emit(DataState.loading())
            val dogs = getDogsFromNetwork()
            emit(DataState.success(dogs))
        } catch (e: Exception) {
            emit(DataState.error<List<Dog>>(e.message ?: "Unknown Error"))
        }
    }.asCommonFlow()

     suspend fun getDogsFromNetwork(): List<Dog> {
        val dogDtos = recipeService.getDogs().message
        return dtoMapper.toDomainList(dogDtos)
    }
}