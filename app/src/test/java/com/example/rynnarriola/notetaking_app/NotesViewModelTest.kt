package com.example.rynnarriola.notetaking_app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.rynnarriola.notetaking_app.data.local.entity.NoteEntity
import com.example.rynnarriola.notetaking_app.data.repository.NotesRepo
import com.example.rynnarriola.notetaking_app.ui.viewmodel.NotesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class NotesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: NotesViewModel
    private val repository: NotesRepo = mock()

    // Setting up a Test Dispatcher to control coroutines
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = NotesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }

    @Test
    fun `test addNote should update isMedicinesListPopulated and insert note into repository`() = runTest {
        // Arrange
        val note = NoteEntity(0, "Paracetamol", "500mg", "Tablet", true)
        viewModel.medicines.add(note)

        val observer = mock<Observer<Boolean>>()
        viewModel.isMedicinesListPopulated.observeForever(observer)

        // Mock the insert function (assuming it is a void method)
        doAnswer { }.`when`(repository).insert(note)

        // Act
        viewModel.addNote(note)

        // Assert
        verify(repository, times(1)).insert(note)
        assertTrue(viewModel.isMedicinesListPopulated.value == true) // Check if the LiveData was updated

        // Clean up
        viewModel.isMedicinesListPopulated.removeObserver(observer)
    }

    @Test
    fun `test removeNote should delete note from repository`() = runTest {
        // Arrange
        val note = NoteEntity(1, "Paracetamol", "500mg", "Tablet", false)

        // Act
        viewModel.removeNote(note)

        // Assert
        verify(repository).delete(note)
    }

    @Test
    fun `test passJson should populate medicines list`() = runTest {
        // Arrange
        val jsonString = """{
          "problems": [
            {
              "Diabetes": [
                {
                  "medications": [
                    {
                      "medicationsClasses": [
                        {
                          "className": [
                            {
                              "associatedDrug": [
                                {
                                  "name": "Metformin",
                                  "dose": "500 mg",
                                  "strength": "1 tablet"
                                }
                              ],
                              "associatedDrug#2": [
                                {
                                  "name": "Insulin",
                                  "dose": "100 units",
                                  "strength": "injection"
                                }
                              ]
                            }
                          ]
                        }
                      ]
                    }
                  ]
                }
              ]
            }
          ]
        }"""

        // Act
        viewModel.passJson(jsonString)

        // Assert
        assert(viewModel.medicines.isNotEmpty())
        assert(viewModel.medicines.size == 2)
        assert(viewModel.medicines[0].name == "Metformin")
        assert(viewModel.medicines[1].name == "Insulin")
    }

    @Test
    fun `test editNote should update note in repository`() = runTest {
        // Arrange
        val note = NoteEntity(1, "Paracetamol", "500mg", "Tablet", false)

        // Act
        viewModel.editNote(note)

        // Assert
        verify(repository).update(note)
    }
}
