package com.example.classificationsupport.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.classificationsupport.ui.components.CustomAlertDialog
import com.example.classificationsupport.ui.task.TaskDetail
import com.example.classificationsupport.ui.task.TaskInfo
import com.example.classificationsupport.ui.task.TaskScan

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "info") {
        composable("info") { TaskInfo(navController) }
        composable(
            route = "detail/{seq}/{date}/{num}",
            arguments = listOf(
                navArgument("seq") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("num") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val seq = backStackEntry.arguments?.getString("seq")
            val date = backStackEntry.arguments?.getString("date")
            val num = backStackEntry.arguments?.getString("num")
            TaskDetail(navController, seq, date, num)
        }

        composable(
            route = "scan/{seq}/{date}",
            arguments = listOf(
                navArgument("seq") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val seq = backStackEntry.arguments?.getString("seq")
            val date = backStackEntry.arguments?.getString("date")
            TaskScan(navController, seq, date)
        }

    }

    // alert dialog
    CustomAlertDialog()
}