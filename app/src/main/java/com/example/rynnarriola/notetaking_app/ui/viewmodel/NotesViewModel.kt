package com.example.rynnarriola.notetaking_app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rynnarriola.notetaking_app.data.local.entity.NoteEntity
import com.example.rynnarriola.notetaking_app.data.repository.NotesRepo
import com.google.gson.Gson
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val repository: NotesRepo) : ViewModel() {
    val notes: Flow<List<NoteEntity>> = repository.getAllNotes()
    val medicines = mutableListOf<NoteEntity>()

    private val _isMedicinesListPopulated = MutableLiveData<Boolean>()
    val isMedicinesListPopulated: LiveData<Boolean>
        get() = _isMedicinesListPopulated

    //fun getNoteById(id: Int): Flow<NoteEntity> = repository.getNoteById(id)x

    fun passJson(jsonString: String) {
        if (medicines.isEmpty()) {
            viewModelScope.launch {
                parseJson(jsonString)
            }
        }
    }

    fun addNote(note: NoteEntity) {
        medicines[note.id].existsInDB = true
        _isMedicinesListPopulated.value = true
        viewModelScope.launch {
            repository.insert(note)
        }
    }

    //additional functionality but not part of the feature
    fun removeNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    //additional functionality but not part of the feature
    fun editNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.update(note)
        }
    }

    private suspend fun parseJson(jsonString: String) {
        val gson = Gson()
        val jsonElement = gson.fromJson(jsonString, JsonElement::class.java)
        val medicationDetails = mutableListOf<NoteEntity>()

        val problemsArray = jsonElement.asJsonObject["problems"].asJsonArray

        for (problem in problemsArray) {
            val diabetes = problem.asJsonObject["Diabetes"].asJsonArray

            for (disease in diabetes) {
                val medications = disease.asJsonObject["medications"].asJsonArray

                for (medication in medications) {
                    val medicationClasses =
                        medication.asJsonObject["medicationsClasses"].asJsonArray

                    for (medClass in medicationClasses) {
                        for (classItem in medClass.asJsonObject.entrySet()) {
                            val associatedDrugs =
                                classItem.value.asJsonArray[0].asJsonObject["associatedDrug"].asJsonArray
                            val associatedDrugs2 =
                                classItem.value.asJsonArray[0].asJsonObject["associatedDrug#2"].asJsonArray

                            for (drug in associatedDrugs) {
                                val name = drug.asJsonObject["name"].asString
                                val dose = drug.asJsonObject["dose"].asString
                                val strength = drug.asJsonObject["strength"].asString

                                medicationDetails.add(
                                    NoteEntity(
                                        medicationDetails.size,
                                        name,
                                        dose,
                                        strength,
                                        false
                                    )
                                )
                            }

                            for (drug in associatedDrugs2) {
                                val name = drug.asJsonObject["name"].asString
                                val dose = drug.asJsonObject["dose"].asString
                                val strength = drug.asJsonObject["strength"].asString

                                medicationDetails.add(
                                    NoteEntity(
                                        medicationDetails.size,
                                        name,
                                        dose,
                                        strength,
                                        false
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        medicines.addAll(medicationDetails)

        // Check if any medicines were saved to DB
        medicines.forEachIndexed { index, medicine ->
            withContext(Dispatchers.IO) {
                val note = repository.getNoteById(medicine.id)
                note?.let {
                    medicines[index].existsInDB = true
                }
            }
        }
    }
}