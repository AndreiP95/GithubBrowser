package com.andrei.githubbrowser.data.sources.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrei.githubbrowser.data.model.entity.RepoEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var repoDao: RepoDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        repoDao = database.repoDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetRepos() = runTest {
        val repos = listOf(
            RepoEntity(1, "repo1", "desc1", "owner1", "avatar1", 10, 5, "date1", "lang1", "url1"),
            RepoEntity(2, "repo2", "desc2", "owner2", "avatar2", 20, 15, "date2", "lang2", "url2")
        )

        repoDao.insertRepos(repos)
        val retrievedRepos = repoDao.getRepos().first()

        assertEquals(2, retrievedRepos.size)
        assertEquals(repos[0], retrievedRepos[0])
    }

    @Test
    fun clearRepos() = runTest {
        val repos = listOf(
            RepoEntity(1, "repo1", "desc1", "owner1", "avatar1", 10, 5, "date1", "lang1", "url1")
        )
        repoDao.insertRepos(repos)
        repoDao.clearRepos()

        val retrievedRepos = repoDao.getRepos().first()
        assertTrue(retrievedRepos.isEmpty())
    }

    @Test
    fun getRepos_whenEmpty() = runTest {
        val retrievedRepos = repoDao.getRepos().first()
        assertTrue(retrievedRepos.isEmpty())
    }
}