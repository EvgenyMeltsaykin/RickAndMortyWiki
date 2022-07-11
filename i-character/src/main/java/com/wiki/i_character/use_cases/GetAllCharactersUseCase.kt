package com.wiki.i_character.use_cases

import com.wiki.i_character.CharactersApiService
import com.wiki.i_character.data.CharactersResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetAllCharactersUseCase(
    private val apiService: CharactersApiService
) {

    suspend operator fun invoke(page: Int = 0): Result<Flow<CharactersResponse>> {
        return Result.success(flowOf(apiService.getAllCharacters(page)))
    }

}