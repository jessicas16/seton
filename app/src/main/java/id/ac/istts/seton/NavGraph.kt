package id.ac.istts.seton
//
//import android.content.Intent
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.runtime.Composable
//import androidx.navigation.compose.NavHost
//import androidx.navigation.NavHostController
//import androidx.navigation.activity
//import androidx.navigation.compose.composable
//import com.example.seton.loginRegister.LoginActivity
////import com.example.seton.mainPage.Dashboard
//import com.example.seton.mainPage.DashboardActivity
//import com.example.seton.projectPage.ListProjectActivity
//
//@Composable
//fun SetUpNavGraph(
//    navController: NavHostController,
//    innerPadding: PaddingValues
//) {
//    NavHost(navController = navController, startDestination = Screens.Projects.route){
//        composable(Screens.Dashboard.route){
//            Dashboard()
//        }
//        composable(Screens.Projects.route){
//            ListProjectActivity()
//        }
//        composable(Screens.Tasks.route){
//
//        }
//        composable(Screens.Logout.route){
//            LoginActivity()
//        }
//    }
//}