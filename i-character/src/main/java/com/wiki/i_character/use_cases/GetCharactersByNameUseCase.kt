package com.wiki.i_character.use_cases

import com.wiki.i_character.CharactersApiService
import com.wiki.i_character.data.CharactersResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface GetCharactersByNameUseCase {
    suspend operator fun invoke(text: String, page: Int = 1): Result<Flow<CharactersResponse>>
}

class GetCharactersByNameUseCaseImpl(
    private val apiService: CharactersApiService
) : GetCharactersByNameUseCase {

    override suspend fun invoke(text: String, page: Int): Result<Flow<CharactersResponse>> {
        return Result.success(flowOf(apiService.getCharactersByName(page = page, name = text)))
    }

}