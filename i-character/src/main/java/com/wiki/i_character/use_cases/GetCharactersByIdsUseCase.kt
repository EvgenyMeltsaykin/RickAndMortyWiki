package com.wiki.i_character.use_cases

import com.wiki.cf_data.CharacterDto
import com.wiki.i_character.CharactersApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface GetCharactersByIdsUseCase {
    suspend operator fun invoke(ids: List<String>): Flow<List<CharacterDto>>
}

class GetCharactersByIdsUseCaseImpl(
    private val apiService: CharactersApiService
) : GetCharactersByIdsUseCase {

    override suspend fun invoke(ids: List<String>): Flow<List<CharacterDto>> {
        val idsString = ids.joinToString(separator = ",")
        return if (ids.size > 1) {
            flowOf(apiService.getCharacters(idsString).map { it.toCharacterDto() })
        } else {
            flowOf(listOf(apiService.getCharacter(idsString).toCharacterDto()))
        }
    }

}