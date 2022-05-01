package com.wiki.i_character.use_cases

import com.wiki.cf_data.CharacterDto
import com.wiki.i_character.CharactersApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface GetCharacterInfoUseCase {
    suspend operator fun invoke(id: Int): Flow<CharacterDto>
}

class GetCharacterInfoUseCaseImpl(
    private val apiService: CharactersApiService
) : GetCharacterInfoUseCase {

    override suspend fun invoke(id: Int): Flow<CharacterDto> {
        return flowOf(apiService.getCharacter(id.toString()).toCharacterDto())
    }

}