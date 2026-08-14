package com.circleearn.circlettc.ui.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable data object Home : Screen()
    @Serializable data object Jobs : Screen()
    @Serializable data object Wallet : Screen()
    @Serializable data object TopUp : Screen()
    @Serializable data object Profile : Screen()
}
