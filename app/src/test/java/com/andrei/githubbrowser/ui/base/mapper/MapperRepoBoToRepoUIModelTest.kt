package com.andrei.githubbrowser.ui.base.mapper

import com.andrei.githubbrowser.R
import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.utils.StringProvider
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperRepoBoToRepoUIModelTest {

    @Test
    fun `map should convert RepoBo to RepoUiModel`() {
        val stringProvider = mockk<StringProvider>(relaxed = true)
        coEvery { stringProvider.getString(R.string.repo_details_unknown_language) } returns "Unknown"
        coEvery { stringProvider.getString(R.string.repo_details_no_description) } returns "No description provided."

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

        val mapper = MapperRepoBoToRepoUIModel(stringProvider)
        val result = mapper.map(repoBo)

        assertEquals("name", result.name)
        assertEquals("owner", result.ownerName)
        assertEquals("avatar", result.ownerAvatarUrl)
        assertEquals("url", result.repoUrl)
        assertEquals("5", result.forks)
        assertEquals("10", result.stars)
        assertEquals("Kotlin", result.language)
        assertEquals("Jan 01, 2023", result.updatedAt)
        assertEquals("description", result.description)
    }
}