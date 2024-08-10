package com.example.workoutapp.screens.helpers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.workoutapp.navigation.NavSubGraphs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericTopBar(
    navController: NavController,
    title : String, // Could be a composable
    includeBackArrow : Boolean = true,
    firstAction : @Composable () -> Unit = {},
) {

    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {

            // Could use this space for a navigation menu
            // IconButton(onClick = {}) {
            //     Icon(
            //         imageVector = Icons.Filled.Menu,
            //         contentDescription = "Menu"
            //     )
            // }

            if (!includeBackArrow) {
                return@TopAppBar
            }
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Go back"
                )
            }
        },
        actions = {

            // Could have a settings menu (or just a menu) with a logout option
            // IconButton(onClick = {}) {
            //     Icon(
            //         contentDescription = "Settings",
            //         imageVector = Icons.Default.Settings,
            //     )
            // }

            firstAction()

            IconButton(onClick = {
                // Navigate to a logout screen + clear stack? Nah, don't clear stack. Have a yes/no option popup compared to doing this at the logout screen
                navController.navigate(NavSubGraphs.LogoutSubGraph.route) {
                    popUpTo(NavSubGraphs.HomeSubGraph.route) {
                        inclusive = true // Clear whole backstack. Repository instances remain, but no VM instances will persist via Dagger.
                    }
                }
                // This logout screen should prevent back presses (done in the nav controller declaration: https://stackoverflow.com/questions/70376070/disable-back-button-in-jetpack-compose)
                // It should load for at least X ms, before redirecting the user to the start page
            }) {
                Icon(
                    contentDescription = "Logout",
                    imageVector = Icons.Default.ExitToApp, // TODO -> Find a better icon for logout
                )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
    )

}