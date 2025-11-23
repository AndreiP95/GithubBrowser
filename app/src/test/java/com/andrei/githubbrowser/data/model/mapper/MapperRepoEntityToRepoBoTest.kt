package com.andrei.githubbrowser.data.model.mapper

import com.andrei.githubbrowser.data.model.entity.RepoEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperRepoEntityToRepoBoTest {

    @Test
    fun `map should convert RepoEntity to RepoBo`() {
        val repoEntity = RepoEntity(
            id = 1,
            name = "name",
            description = "description",
            ownerName = "owner",
            ownerAvatar = "avatar",
            stars = 10,
            forks = 5,
            updatedAt = "2023-01-01T00:00:00Z",
            language = "Kotlin",
            repoUrl = "url"
        )

        val mapper = MapperRepoEntityToRepoBo()
        val result = mapper.map(repoEntity)

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