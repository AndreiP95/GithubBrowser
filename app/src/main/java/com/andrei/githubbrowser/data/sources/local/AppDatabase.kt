package com.andrei.githubbrowser.data.sources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andrei.githubbrowser.data.model.entity.RepoEntity

@Database(entities = [RepoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}