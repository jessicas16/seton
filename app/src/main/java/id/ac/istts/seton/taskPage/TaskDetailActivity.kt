package id.ac.istts.seton.taskPage

import android.content.Intent
import android.graphics.Color.parseColor
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import id.ac.istts.seton.AppBar
import id.ac.istts.seton.DrawerBody
import id.ac.istts.seton.DrawerHeader
import id.ac.istts.seton.MenuItem
import id.ac.istts.seton.R
import id.ac.istts.seton.Screens
import id.ac.istts.seton.entity.Projects
import id.ac.istts.seton.entity.TaskDRO
import id.ac.istts.seton.entity.Users
import id.ac.istts.seton.loginRegister.LoginActivity
import id.ac.istts.seton.mainPage.DashboardActivity
import id.ac.istts.seton.projectPage.ListProjectActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TaskDetailActivity : ComponentActivity() {
    private val vm: TaskDetailViewModel by viewModels<TaskDetailViewModel>()
    private lateinit var scope: CoroutineScope
    private lateinit var taskId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskId = intent.getStringExtra("taskId").toString()
        Toast.makeText(this, taskId, Toast.LENGTH_SHORT).show()
        setContent {
            scope = rememberCoroutineScope()

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
                    title = "Logout",
                    route = Screens.Logout.route,
                    selectedIcon = Icons.Filled.Logout,
                    unSelectedIcon = Icons.Outlined.Logout
                ),
            )

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//            val navController = rememberNavController()
//            val navBackStackEntry by navController.currentBackStackEntryAsState()
//            val currentRoute = navBackStackEntry?.destination?.route
//
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
                                    Screens.Logout.route -> {
                                        startActivity(Intent(this@TaskDetailActivity, LoginActivity::class.java))
                                    }
                                    Screens.Dashboard.route -> {
                                        startActivity(Intent(this@TaskDetailActivity, DashboardActivity::class.java))
                                    }
                                    Screens.Tasks.route -> {
                                        startActivity(Intent(this@TaskDetailActivity, TaskActivity::class.java))
                                    }
                                    Screens.Projects.route -> {
                                        startActivity(Intent(this@TaskDetailActivity, ListProjectActivity::class.java))
                                    }
                                }
                            }
                        )
                    }
                }, drawerState = drawerState
            ) {
                Scaffold(
                    topBar = {
                        AppBar (
                            name = "Task Detail",
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                    }
                ) {
                    val hai = it
                    Detail()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        vm.getTaskById(taskId)
    }

    @Preview(showBackground = true)
    @Composable
    fun Detail() {
        val context = LocalContext.current
        val taskDetail by vm.task.observeAsState(
            TaskDRO(
                status = "500",
                message = "an error occurred",
                data = DataTask(
                    id = -1,
                    title = "",
                    deadline = "",
                    description = "",
                    priority = 0,
                    status = 0,
                    pic = Users(
                        email = "",
                        name = "",
                        profile_picture = "",
                        password = "",
                        auth_token = "",
                        status = 0
                    ),
                    project = Projects(
                        id = -1,
                        name = "",
                        description = "",
                        start = "",
                        deadline = "",
                        pm_email = "",
                        status = 0
                    ),
                    teams = listOf(),
                    comments = listOf(),
                    attachments = listOf(),
                    checklists = listOf(),
                    labels = listOf()
                )
            )
        )
        LaunchedEffect(key1 = Unit) {
            vm.getTaskById(taskId)
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            val (title) = createRefs()

            Row(
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                horizontalArrangement = Arrangement.Start
            ) {
                Column {
                    Text(
                        text = taskDetail.data.title,
                        fontSize = 24.sp,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_bold, FontWeight.Bold)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "in ${taskDetail.data.project.name}",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_regular, FontWeight.Normal)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    for (label in taskDetail.data.labels) {
                        Labels(
                            text = label.title,
                            color2 = "#${label.color}"
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        val monthMap = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
                        var tgl = ""
                        if(taskDetail.data.deadline != ""){
                            tgl =  "${taskDetail.data.deadline.substring(8, 10)} ${monthMap[taskDetail.data.deadline.substring(5, 7).toInt() - 1]} ${taskDetail.data.deadline.substring(0, 4)}"
                        }


                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFFECFFFF),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = tgl,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(
                                        Font(R.font.open_sans_regular, FontWeight.Normal)
                                    ),
                                )
                            }


                        var status=""
                        when(taskDetail.data.status){
                            0 -> status = "Upcoming"
                            1 -> status = "Ongoing"
                            2 -> status = "Submitted"
                            3 -> status = "Revision"
                            4 -> status = "Completed"
                        }
                        var expanded by remember { mutableStateOf(false) }
                        var selectedText by remember { mutableStateOf(status) }
                        val items = listOf(
                            "Upcoming" to Color(0xFFFFF176),
                            "Ongoing" to Color(0xFFFF8A65),
                            "Submitted" to Color(0xFF4DD0E1),
                            "Revision" to Color(0xFFFFCC80),
                            "Completed" to Color(0xFF00897B)
                        )

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        ){
                            Text(
                                text = selectedText,
                                modifier = Modifier
                                    .clickable{ expanded = true }
                                    .padding(horizontal = 0.dp, vertical = 8.dp),
                                color = items.first { it.first == selectedText }.second,
                                fontSize = 16.sp
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                            ) {
                                items.forEach { (text, color) ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedText = text
                                            expanded = false
                                        }
                                    ) {
                                        Text(text = text, color = color, fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                Column {
                    Text(
                        text = "asdfasdasdasdasd"
                    )
                }
            }
        }
    }

    @Composable
    private fun Labels(
        text: String,
        color2: String
    ) {
        Row{
            Icon(
                imageVector = Icons.Filled.Discount,
                contentDescription = "Tag",
                modifier = Modifier
                    .size(25.dp),
                tint = color2.toColor(),
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = text,
                color = color2.toColor(),
                fontSize = 16.sp,
                fontFamily = FontFamily(
                    Font(R.font.open_sans_regular, FontWeight.Normal)
                ),
            )
        }
    }




    private fun String.toColor() = Color(parseColor(this))
}
