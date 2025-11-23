package com.andrei.githubbrowser.data.model.mapper

import com.andrei.githubbrowser.data.model.bo.RepoBo
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperRepoBoToRepoEntityTest {

    @Test
    fun `map should convert RepoBo to RepoEntity`() {
        val repoBo = RepoBo(
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

        val mapper = MapperRepoBoToRepoEntity()
        val result = mapper.map(repoBo)

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