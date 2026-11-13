package com.campusmarket.demo.navigation

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.campusmarket.demo.ui.theme.CampusMarketTheme

@Composable
fun AppBottomBar(
    currentRoute: String?,
    unreadChats: Int,
    onNavigate: (TopLevelDestination) -> Unit,
) {
    NavigationBar {
        TopLevelDestination.entries.forEach { destination ->
            val selected = currentRoute == destination.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(destination) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (destination == TopLevelDestination.CHATS && unreadChats > 0) {
                                Badge { Text("$unreadChats") }
                            }
                        },
                    ) {
                        Icon(
                            imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                            contentDescription = destination.label,
                        )
                    }
                },
                label = { Text(destination.label) },
            )
        }
    }
}

@Preview
@Composable
private fun AppBottomBarPreview() {
    CampusMarketTheme {
        AppBottomBar(currentRoute = Routes.HOME, unreadChats = 3, onNavigate = {})
    }
}
