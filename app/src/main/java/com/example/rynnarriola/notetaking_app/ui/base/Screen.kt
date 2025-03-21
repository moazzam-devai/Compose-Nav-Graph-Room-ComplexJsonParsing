package com.example.rynnarriola.notetaking_app.ui.base

sealed class Screen(val route : String) {

    data object LoginScreen: Screen("login_screen")
    data object PendingTaskScreen: Screen("pending_task_screen")
    data object MedicationDetailScreen: Screen("medication_detail_screen")
    data object DownloadedMedicinesScreen: Screen("downloaded_medicines_screen")
    data object AddNoteScreen: Screen("add_note_screen")
    data object EditNoteScreen: Screen("edit_note_screen")

    }