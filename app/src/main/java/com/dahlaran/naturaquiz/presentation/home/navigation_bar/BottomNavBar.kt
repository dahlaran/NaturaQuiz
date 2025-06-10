package com.dahlaran.naturaquiz.presentation.home.navigation_bar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import com.dahlaran.naturaquiz.presentation.navigation.Screen

/**
 * Bottom navigation bar screen for the app
 */
sealed class BottomNavScreen(val navKey: NavKey, val title: String, val icon: Int) {
    data object Quiz : BottomNavScreen(Screen.QuizHomeScreenKey, "Quiz", android.R.drawable.ic_menu_view)
    data object List : BottomNavScreen(Screen.HomeListScreenKey, "List", android.R.drawable.ic_dialog_dialer)
    // TODO: Implement a search screen
}

/**
 * Bottom navigation bar composable
 */
@Composable
fun BottomNavBar(
    currentNavKey: NavKey,
    onNavigate: (NavKey) -> Unit
) {
    NavigationBar {
        listOf(BottomNavScreen.Quiz, BottomNavScreen.List).forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(painterResource(id = screen.icon), contentDescription = screen.title)
                },
                label = { Text(screen.title) },
                selected = currentNavKey == screen.navKey,
                onClick = {
                    if (currentNavKey != screen.navKey) {
                        onNavigate(screen.navKey)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun BottomNavBarPreview() {
    BottomNavBar(
        currentNavKey = Screen.QuizHomeScreenKey,
        onNavigate = {}
    )
}