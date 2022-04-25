package com.wiki.i_character.use_cases

import com.wiki.i_character.CharactersApiService
import com.wiki.i_character.data.CharactersResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface GetAllCharactersUseCase {
    suspend operator fun invoke(page: Int = 0): Result<Flow<CharactersResponse>>
}

class GetAllCharactersUseCaseImpl(
    private val apiService: CharactersApiService
) : GetAllCharactersUseCase {

    override suspend fun invoke(page: Int): Result<Flow<CharactersResponse>> {
        return Result.success(flowOf(apiService.getAllCharacters(page)))
    }

}