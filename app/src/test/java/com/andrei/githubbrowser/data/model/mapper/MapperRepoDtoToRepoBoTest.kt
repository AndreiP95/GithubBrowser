package com.andrei.githubbrowser.data.model.mapper

import com.andrei.githubbrowser.data.model.dto.Owner
import com.andrei.githubbrowser.data.model.dto.RepoDto
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperRepoDtoToRepoBoTest {

    @Test
    fun `map should convert RepoDto to RepoBo`() {
        val repoDto = RepoDto(
            id = 1,
            name = "name",
            description = "description",
            owner = Owner(login = "owner", avatarUrl = "avatar"),
            stargazersCount = 10,
            forksCount = 5,
            updatedAt = "2023-01-01T00:00:00Z",
            language = "Kotlin",
            repoUrl = "url"
        )

        val mapper = MapperRepoDtoToRepoBo()
        val result = mapper.map(repoDto)

        assertEquals(1L, result.id)
        assertEquals("name", result.name)
        assertEquals("description", result.description)
        assertEquals("owner", result.ownerName)
        assertEquals("avatar", result.ownerAvatar)
        assertEquals(10, result.stars)
        assertEquals(5, result.forks)
        assertEquals("2023-01-01T00:00:00Z", result.updatedAt)
        assertEquals("Kotlin", result.language)
        assertEquals("url", result.repoUrl)
    }
}