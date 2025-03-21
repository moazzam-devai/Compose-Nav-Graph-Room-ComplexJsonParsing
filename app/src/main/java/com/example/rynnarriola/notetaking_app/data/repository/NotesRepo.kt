package com.example.rynnarriola.notetaking_app.data.repository

import com.example.rynnarriola.notetaking_app.data.local.dao.NotesDao
import com.example.rynnarriola.notetaking_app.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepo @Inject constructor(private val notesDao: NotesDao) {

    fun getAllNotes(): Flow<List<NoteEntity>> = notesDao.getAllNotes()

    //fun getNoteById(id: Int): Flow<NoteEntity> = notesDao.getNoteById(id)

    suspend fun getNoteById(noteId: Int): NoteEntity? {
        return notesDao.getNoteById(noteId)
    }

    suspend fun insert(note: NoteEntity) {
        notesDao.insert(note)
    }

    suspend fun delete(note: NoteEntity) {
        notesDao.delete(note)
    }

    suspend fun update(note: NoteEntity) {
        notesDao.update(note)
    }
}