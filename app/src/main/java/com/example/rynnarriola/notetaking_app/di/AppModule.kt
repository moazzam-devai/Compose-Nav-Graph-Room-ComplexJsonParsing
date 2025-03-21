package com.example.rynnarriola.notetaking_app.di

import android.content.Context
import androidx.room.Room
import com.example.rynnarriola.notetaking_app.data.local.NotesDataBase
import com.example.rynnarriola.notetaking_app.data.local.dao.NotesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @DatabaseName
    @Provides
    fun provideDatabaseName(): String = "notes-database"


    @Provides
    @Singleton
    fun provideNewsDatabase(
        @ApplicationContext context: Context,
        @DatabaseName databaseName: String
    ): NotesDataBase {
        return Room.databaseBuilder(
            context,
            NotesDataBase::class.java,
            databaseName
        )
            .fallbackToDestructiveMigration() // Allow destructive migration
            .build()
    }

    @Provides
    @Singleton
    fun provideNotesDao(notesDatabase: NotesDataBase): NotesDao {
        return notesDatabase.notesDao()
    }
}