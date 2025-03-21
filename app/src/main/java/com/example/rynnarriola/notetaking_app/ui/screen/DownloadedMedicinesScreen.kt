package com.example.rynnarriola.notetaking_app.ui.screen

import MedicationList
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.rynnarriola.notetaking_app.ui.viewmodel.NotesViewModel

@Composable
fun DownloadedMedicinesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val medicines by viewModel.notes.collectAsState(initial = emptyList())

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        MedicationList(medicines, navController, viewModel)
    }
}