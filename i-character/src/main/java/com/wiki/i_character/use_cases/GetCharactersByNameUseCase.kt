package com.wiki.i_character.use_cases

import com.wiki.i_character.CharactersApiService
import com.wiki.i_character.data.CharactersResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetCharactersByNameUseCase(
    private val apiService: CharactersApiService
) {

    suspend operator fun invoke(text: String, page: Int): Result<Flow<CharactersResponse>> {
        return Result.success(flowOf(apiService.getCharactersByName(page = page, name = text)))
    }

}