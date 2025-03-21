package com.example.rynnarriola.notetaking_app.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rynnarriola.notetaking_app.data.local.entity.NoteEntity

@Composable
fun MedicationDetailScreen(medicationDetails: NoteEntity) {
    Scaffold { padding ->
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Medication Details", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Name: ${medicationDetails.name}")
            Text(text = "Dose: ${medicationDetails.dose}")
            Text(text = "Strength: ${medicationDetails.strength}")
        }
    }
}