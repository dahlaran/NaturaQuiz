package com.dahlaran.naturaquiz.presentation.home.navigation_bar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Bottom navigation bar screen for the app
 */
sealed class BottomNavScreen(val route: String, val title: String, val icon: Int) {
    data object Quiz : BottomNavScreen("quiz", "Quiz", android.R.drawable.ic_menu_view)
    data object List : BottomNavScreen("list", "List", android.R.drawable.ic_dialog_dialer)
    // TODO: Implement a search screen
}

/**
 * Bottom navigation bar composable
 */
@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        listOf(BottomNavScreen.Quiz, BottomNavScreen.List).forEach { screen ->
            NavigationBarItem(icon = {
                Icon(painterResource(id = screen.icon), contentDescription = screen.title)
            },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                })
        }
    }
}

@Preview
@Composable
fun BottomNavBarPreview() {
    BottomNavBar(navController = NavController(LocalContext.current))
}