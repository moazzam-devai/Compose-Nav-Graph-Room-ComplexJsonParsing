import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.rynnarriola.notetaking_app.data.local.entity.NoteEntity
import com.example.rynnarriola.notetaking_app.ui.base.Screen
import com.example.rynnarriola.notetaking_app.ui.viewmodel.NotesViewModel
import com.example.rynnarriola.notetaking_app.util.Util

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingTaskScreen(
    navController: NavController,
    email: String,
    viewModel: NotesViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }
    var dbChanged by remember { mutableStateOf(false) }
    val isMedicinesListPopulated by viewModel.isMedicinesListPopulated.observeAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val inputStream = context.assets.open("data.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        // To parse the json and populate on the screen
        viewModel.passJson(jsonString)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    title = { Text("Medication App") },
                    actions = {
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                    }
                )

                // Dropdown Menu for navigation
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.align(Alignment.TopEnd).padding(end = 16.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("Downloaded Medicines") },
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            navController.navigate(Screen.DownloadedMedicinesScreen.route)
                            showMenu = false
                        }
                    )
                }
            }

            GreetingCard(email)
            MedicationList(viewModel.medicines, navController, viewModel)

            isMedicinesListPopulated?.let {
                dbChanged = !dbChanged
            }
        }
    }
}

@Composable
fun GreetingCard(userEmail: String) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "${Util.greetUser()} $userEmail",
            modifier = Modifier.padding(16.dp),
            fontSize = 20.sp
        )
    }
}

@Composable
fun MedicationList(
    medications: List<NoteEntity>,
    navController: NavController,
    viewModel: NotesViewModel
) {
    if (medications.isEmpty().not()) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(medications.size) { index ->
                val medication = medications[index]
                MedicationCard(
                    medication,
                    onClick = {
                        // Set medication in the current back stack entry's savedStateHandle before navigating
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "medication",
                            medication
                        )
                        // Navigate to the detail screen
                        navController.navigate(Screen.MedicationDetailScreen.route)
                    },
                    onDownload = {
                        // Call the download function in your ViewModel
                        viewModel.addNote(medication)
                    }
                )
            }
        }
    }
}

@Composable
fun MedicationCard(
    medication: NoteEntity,
    onClick: () -> Unit,
    onDownload: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Name: ${medication.name}", fontSize = 16.sp)
                Text(text = "Dose: ${medication.dose}", fontSize = 16.sp)
                Text(text = "Strength: ${medication.strength}", fontSize = 16.sp)
            }

            Icon(
                imageVector = if (medication.existsInDB.not()) Icons.Default.Download else Icons.Default.Done,  // This is the default download icon
                contentDescription = "Download",
                modifier = Modifier
                    .size(24.dp) // You can adjust the size here
                    .align(Alignment.CenterVertically)
                    .clickable {
                        if (medication.existsInDB.not())
                            onDownload()
                    }
            )
        }
    }
}
