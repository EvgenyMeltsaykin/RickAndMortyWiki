package com.wiki.i_character.use_cases

import com.wiki.cf_network.data.PaginationInfo
import com.wiki.i_character.CharactersApiService
import com.wiki.i_character.data.CharactersResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class GetCharactersByNameUseCase(
    private val apiService: CharactersApiService
) {

    suspend operator fun invoke(text: String, page: Int = 1): Result<Flow<CharactersResponse>> {
        return try {
            Result.success(flowOf(apiService.getCharactersByName(page = page, name = text)))
        } catch (e: Exception) {
            Result.success(emptyFlow())
        }
    }

    suspend operator fun invoke(text: String): CharactersResponse {
        return try {
            apiService.getCharactersByName(page = 1, name = text)
        } catch (e: Exception) {
            CharactersResponse(result = emptyList(), PaginationInfo())
        }
    }


}