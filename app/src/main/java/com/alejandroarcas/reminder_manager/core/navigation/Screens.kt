package com.alejandroarcas.reminder_manager.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object ListScreen

@Serializable
data class DetailScreen(val id: Int)

@Serializable
object AddScreen