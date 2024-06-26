package id.ac.istts.seton.taskPage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ac.istts.seton.AppBar
import id.ac.istts.seton.AppFont
import id.ac.istts.seton.DrawerBody
import id.ac.istts.seton.DrawerHeader
import id.ac.istts.seton.MenuItem
import id.ac.istts.seton.R
import id.ac.istts.seton.Screens
import id.ac.istts.seton.calendarPage.CalendarActivity
import id.ac.istts.seton.config.ApiConfiguration
import id.ac.istts.seton.loginRegister.LoginActivity
import id.ac.istts.seton.mainPage.DashboardActivity
import id.ac.istts.seton.projectPage.ListProjectActivity
import id.ac.istts.seton.reportPage.ReportActivity
import id.ac.istts.seton.settingPage.SettingActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class TaskActivity : ComponentActivity() {
    private val vm: TaskViewModel by viewModels<TaskViewModel>()
    lateinit var userEmail : String
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userEmail = intent.getStringExtra("userEmail").toString()
        setContent {
            mAuth = FirebaseAuth.getInstance()
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
            val auth = Firebase.auth
            val items = listOf(
                MenuItem(
                    title = "Dashboard",
                    route = Screens.Dashboard.route,
                    selectedIcon = Icons.Filled.Dashboard,
                    unSelectedIcon = Icons.Outlined.Dashboard
                ),
                MenuItem(
                    title = "Projects",
                    route = Screens.Projects.route,
                    selectedIcon = Icons.Filled.ListAlt,
                    unSelectedIcon = Icons.Outlined.ListAlt
                ),
                MenuItem(
                    title = "Tasks",
                    route = Screens.Tasks.route,
                    selectedIcon = Icons.Filled.Task,
                    unSelectedIcon = Icons.Outlined.Task
                ),
                MenuItem(
                    title = "Calendar",
                    route = Screens.Calendar.route,
                    selectedIcon = Icons.Filled.CalendarToday,
                    unSelectedIcon = Icons.Outlined.CalendarToday
                ),
                MenuItem(
                    title = "Report",
                    route = Screens.Report.route,
                    selectedIcon = Icons.Filled.Report,
                    unSelectedIcon = Icons.Outlined.Report
                ),
                MenuItem(
                    title = "Settings",
                    route = Screens.Settings.route,
                    selectedIcon = Icons.Filled.Settings,
                    unSelectedIcon = Icons.Outlined.Settings
                ),
            )

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
//            val navController = rememberNavController()
//            val navBackStackEntry by navController.currentBackStackEntryAsState()
//            val currentRoute = navBackStackEntry?.destination?.route

//            val topBarTitle =
//                if (currentRoute != null){
//                    items[items.indexOfFirst {
//                        it.route == currentRoute
//                    }].title
//                }
//                else {
//                    items[0].title
//                }

            ModalNavigationDrawer(
                gesturesEnabled = drawerState.isOpen,
                drawerContent = {
                    ModalDrawerSheet {
                        DrawerHeader()
                        Spacer(modifier = Modifier.height(8.dp))
                        DrawerBody(
                            items = items,
                            onItemClick = { currentMenuItem ->
                                when (currentMenuItem.route){
                                    Screens.Settings.route -> {
//                                        val ioScope = CoroutineScope(Dispatchers.Main)
//                                        ioScope.launch {
//                                            ApiConfiguration.defaultRepo.logoutUser()
//                                        }
//
//                                        if(mAuth.currentUser != null){
//                                            mAuth.signOut()
//                                            mGoogleSignInClient.signOut()
//                                        }

                                        val intent = Intent(this@TaskActivity, SettingActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Dashboard.route -> {
                                        val intent = Intent(this@TaskActivity, DashboardActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Projects.route -> {
                                        val intent = Intent(this@TaskActivity, ListProjectActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Tasks.route -> {
                                        val intent = Intent(this@TaskActivity, TaskActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Calendar.route -> {
                                        val intent = Intent(this@TaskActivity, CalendarActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Report.route -> {
                                        val intent = Intent(this@TaskActivity, ReportActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }
                        )
                    }
                }, drawerState = drawerState
            ){
                Scaffold(
                    topBar = {
                        AppBar (
                            name = "Tasks",
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                    }
                ) {
                    val hai = it
                    TaskPreview()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun TaskPreview() {
        val userTasks by vm.tasks.observeAsState(emptyList())
        LaunchedEffect(key1 = Unit) {
            vm.getUserTasks(email = userEmail)
        }
        ConstraintLayout(modifier = Modifier.fillMaxSize().background(Color.White)) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp, 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(userTasks) {
                    RowPart(it.first, it.second)
                }
            }
        }
    }

    @Composable
    fun RowPart(title: String, data: List<DataTask>) {
        Row {
            Column {
                Row(Modifier.padding(top = 16.dp)) {
                    Text(
                        modifier = Modifier.padding(end = 16.dp, bottom = 16.dp),
                        text = title,
                        fontFamily = AppFont.fontSemiBold,
                        fontSize = 24.sp
                    )
                    Text(
                        modifier = Modifier
                            .weight(6f),
                        text = data.size.toString(),
                        fontFamily = AppFont.fontSemiBold,
                        fontSize = 24.sp,
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                }
                if (data.isEmpty()) {
                    Row(
                        Modifier.fillMaxWidth().height(100.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(6f),
                            text = "You have no $title Task",
                            fontFamily = AppFont.fontSemiBold,
                            fontSize = 24.sp,
                            color = Color.Black.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    PageSlider(data)
                }
            }
        }
    }

    @Composable
    fun PageSlider(data: List<DataTask>) {
        val lazyListState = rememberLazyListState()
        val selectedIndex = remember { mutableIntStateOf(0) }

        LazyRow(
            modifier = Modifier.padding(bottom = 16.dp),
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(data) {
                Card(
                    modifier = Modifier
                        .width(320.dp)
                        .height(140.dp),
                    colors = CardColors(Color.White, Color.Black, Color.White, Color.White),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Column(modifier = Modifier.padding(16.dp, 12.dp)) {
                        Row {
                            Column {
                                Row {
                                    Text(
                                        text = it.title,
                                        fontFamily = AppFont.fontBold,
                                        fontSize = 20.sp
                                    )
                                }
                                Row(Modifier.padding(vertical = 4.dp)) {
                                    Text(
                                        text = it.project.name,
                                        fontSize = 16.sp,
                                        fontFamily = AppFont.fontLight
                                    )
                                }
                                Row (Modifier.padding(vertical = 2.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = Color(0xFFECFFFF),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(4.dp)
                                    ) {
                                        fun formatDate(date: String): String {
                                            val monthMap = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
                                            return "${date.substring(8, 10)} ${monthMap[date.substring(5, 7).toInt() - 1]} ${date.substring(0, 4)}"
                                        }
                                        Text(
                                            text = formatDate(it.deadline),
                                            fontSize = 12.sp,
                                            fontFamily = AppFont.fontNormal,
                                            color = Color(0xFF0E9794)
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = when (it.priority) {
                                                    0 -> Color(0xFF9CDFDF)
                                                    1 -> Color(0xFFFDF4D3)
                                                    else -> Color(0xFFFACBB6)
                                                },
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        val priority = listOf("Low", "Medium", "High")
                                        Text(
                                            text = priority[it.priority],
                                            fontSize = 12.sp,
                                            fontFamily = AppFont.fontNormal
                                        )
                                    }
                                }
                            }
                            if (it.status == 1) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp, end = 8.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        val percentage = it.checklists.filter { it.is_checked == 1 }.size.toFloat() / it.checklists.size.toFloat()
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(50.dp),
                                            progress = { percentage },
                                            color = Color(0xFF0E9794),
                                            strokeWidth = 5.dp,
                                            trackColor = Color(0xFFECFFFF)
                                        )
                                        Text(
                                            text = "${(percentage * 100).toInt()}%",
                                            color = Color.Black,
                                            fontFamily = AppFont.fontBold,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Row(modifier = Modifier.clickable {
                                // Go to Task Details
                                val intent = Intent(this@TaskActivity, TaskDetailActivity::class.java)
                                intent.putExtra("userEmail", userEmail)
                                intent.putExtra("taskId", it.id.toString())
                                startActivity(intent)
                            }, verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "See Details",
                                    fontSize = 14.sp,
                                    fontFamily = AppFont.fontNormal,
                                    color = Color(0xFF0E9794)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.icon_arrow_right),
                                    contentDescription = "Plus Icon",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.End
                            ) {
                                for (i in 0..it.teams.lastIndex) {
                                    val arrName = it.teams[i].name.split(" ")
                                    val alias = arrName[0].first().uppercaseChar().toString() +
                                        if (arrName.size > 1) arrName[1].first().uppercaseChar().toString() else ""

                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(
                                                color = Color(0xFFECFFFF),
                                                shape = RoundedCornerShape(18.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (i < 2) alias else "+2",
                                            fontSize = 16.sp,
                                            fontFamily = AppFont.fontBold,
                                            color = Color(0xFF0E9794)
                                        )
                                    }
                                    if (i > 1) break
                                }
                            }
                        }
                    }
                }
            }
        }

        LaunchedEffect(lazyListState) {
            snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
                .mapNotNull { items ->
                    val firstVisibleItem = items.firstOrNull { it.index != -1 }
                    val lastVisibleItem = items.lastOrNull { it.index != -1 }
                    if (firstVisibleItem != null && lastVisibleItem != null) {
                        Pair(firstVisibleItem.index, lastVisibleItem.index)
                    } else {
                        null
                    }
                }
                .collect { (firstVisibleItemIndex, lastVisibleItemIndex) ->
                    selectedIndex.intValue =
                        if (lastVisibleItemIndex == lazyListState.layoutInfo.totalItemsCount - 1 && lastVisibleItemIndex - firstVisibleItemIndex != 2) lastVisibleItemIndex
                        else (firstVisibleItemIndex + lastVisibleItemIndex) / 2
                }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(data.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(10.dp, 4.dp)
                        .background(
                            color = if (index == selectedIndex.intValue) Color.Gray else Color.LightGray,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        vm.getUserTasks(userEmail)
    }
}