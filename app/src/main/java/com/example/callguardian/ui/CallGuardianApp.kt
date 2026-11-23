package com.example.callguardian.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.callguardian.ui.screens.ActivityScreen
import com.example.callguardian.ui.screens.EventDetailScreen
import com.example.callguardian.ui.screens.HomeScreen
import com.example.callguardian.ui.screens.LabScreen
import com.example.callguardian.ui.screens.ListsScreen
import com.example.callguardian.ui.screens.ReceiptDetailScreen
import com.example.callguardian.ui.screens.ReceiptsScreen
import com.example.callguardian.ui.screens.SettingsScreen

private data class Tab(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    Tab("home", "Home", Icons.Default.Home),
    Tab("activity", "Activity", Icons.Default.Shield),
    Tab("receipts", "Receipts", Icons.Default.ReceiptLong),
    Tab("lab", "Lab", Icons.Default.Science),
    Tab("settings", "Settings", Icons.Default.Settings),
)

data class SetupActions(
    val requestScreeningRole: () -> Unit,
    val requestSms: () -> Unit,
    val requestContacts: () -> Unit,
    val requestNotifications: () -> Unit,
    val openAppSettings: () -> Unit,
)

@Composable
fun CallGuardianApp(vm: AppViewModel, actions: SetupActions, openEventId: Long?, onEventOpened: () -> Unit) {
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val route = entry?.destination?.route

    LaunchedEffect(openEventId) {
        if (openEventId != null && openEventId > 0) { nav.navigate("event/$openEventId"); onEventOpened() }
    }

    Scaffold(
        bottomBar = {
            if (tabs.any { it.route == route }) NavigationBar {
                tabs.forEach { t ->
                    NavigationBarItem(
                        selected = route == t.route,
                        onClick = {
                            nav.navigate(t.route) {
                                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true; restoreState = true
                            }
                        },
                        icon = { Icon(t.icon, null) }, label = { Text(t.label) },
                    )
                }
            }
        },
    ) { pad ->
        NavHost(nav, startDestination = "home", modifier = Modifier.padding(pad)) {
            composable("home") { HomeScreen(vm, actions, onEvent = { nav.navigate("event/$it") }, onTab = { nav.navigate(it) }) }
            composable("activity") { ActivityScreen(vm, onEvent = { nav.navigate("event/$it") }) }
            composable("receipts") { ReceiptsScreen(vm, onReceipt = { nav.navigate("receipt/$it") }) }
            composable("lab") { LabScreen(vm, onEvent = { nav.navigate("event/$it") }) }
            composable("settings") { SettingsScreen(vm, actions, onLists = { nav.navigate("lists") }) }
            composable("lists") { ListsScreen(vm, onBack = { nav.popBackStack() }) }
            composable("event/{id}", arguments = listOf(navArgument("id") { type = NavType.LongType })) {
                EventDetailScreen(vm, it.arguments?.getLong("id") ?: -1, onBack = { nav.popBackStack() }, onReceipt = { r -> nav.navigate("receipt/$r") })
            }
            composable("receipt/{id}", arguments = listOf(navArgument("id") { type = NavType.LongType })) {
                ReceiptDetailScreen(vm, it.arguments?.getLong("id") ?: -1, onBack = { nav.popBackStack() }, onEvent = { e -> nav.navigate("event/$e") })
            }
        }
    }
}
