package com.hamradio.logger.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hamradio.logger.ui.screens.HomeScreen
import com.hamradio.logger.ui.screens.LogbookScreen
import com.hamradio.logger.ui.screens.BandPredictorScreen
import com.hamradio.logger.ui.screens.SettingsScreen
import com.hamradio.logger.ui.screens.NewContactScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("logbook") {
            LogbookScreen(navController)
        }
        composable("band_predictor") {
            BandPredictorScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController)
        }
        composable("new_contact") {
            NewContactScreen(navController)
        }
    }
}
