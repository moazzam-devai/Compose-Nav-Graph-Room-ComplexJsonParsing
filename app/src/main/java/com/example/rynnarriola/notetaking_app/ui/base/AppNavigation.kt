package com.example.rynnarriola.notetaking_app.ui.base

import PendingTaskScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rynnarriola.notetaking_app.data.local.entity.NoteEntity
import com.example.rynnarriola.notetaking_app.ui.screen.DownloadedMedicinesScreen
import com.example.rynnarriola.notetaking_app.ui.screen.LoginScreen
import com.example.rynnarriola.notetaking_app.ui.screen.MedicationDetailScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }

        composable(route = "${Screen.PendingTaskScreen.route}/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            PendingTaskScreen(navController = navController, email)
        }
        composable(route = Screen.DownloadedMedicinesScreen.route) {
            DownloadedMedicinesScreen(navController = navController)
        }
        composable(
            route = Screen.MedicationDetailScreen.route
        ) {
            // Retrieve the object using savedStateHandle
            val medication = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<NoteEntity>("medication")

            // Pass the medication to your screen
            medication?.let { MedicationDetailScreen(it) }
        }

    }
}