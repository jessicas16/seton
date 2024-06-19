package id.ac.istts.seton

import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.activity
import androidx.navigation.compose.composable
import id.ac.istts.seton.loginRegister.LoginActivity
import id.ac.istts.seton.mainPage.DashboardActivity
import id.ac.istts.seton.projectPage.ListProjectActivity

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = Screens.Projects.route){
        composable(Screens.Dashboard.route){
            val intent = Intent(context, DashboardActivity::class.java)
            context.startActivity(intent)
        }
        composable(Screens.Projects.route){
            val intent = Intent(context, ListProjectActivity::class.java)
            context.startActivity(intent)
        }
        composable(Screens.Tasks.route){

        }
        composable(Screens.Settings.route){
            LoginActivity()
        }
    }
}