package com.example.rynnarriola.notetaking_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rynnarriola.notetaking_app.data.local.dao.NotesDao
import com.example.rynnarriola.notetaking_app.data.local.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NotesDataBase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}