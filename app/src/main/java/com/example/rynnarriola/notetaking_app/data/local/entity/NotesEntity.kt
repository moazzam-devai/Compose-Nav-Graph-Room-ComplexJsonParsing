package com.example.rynnarriola.notetaking_app.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val dose: String,
    val strength: String,
    var existsInDB: Boolean
) : Parcelable