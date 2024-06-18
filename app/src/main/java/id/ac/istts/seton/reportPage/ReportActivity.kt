package id.ac.istts.seton.reportPage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Dashboard
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.jaikeerthick.composable_graphs.composables.pie.PieChart
import com.jaikeerthick.composable_graphs.composables.pie.model.PieData
import com.jaikeerthick.composable_graphs.composables.pie.style.PieChartStyle
import com.jaikeerthick.composable_graphs.composables.pie.style.PieChartVisibility
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.core.cartesian.DefaultPointConnector
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import com.patrykandpatrick.vico.core.common.shape.Shape
import id.ac.istts.seton.AppBar
import id.ac.istts.seton.DrawerBody
import id.ac.istts.seton.DrawerHeader
import id.ac.istts.seton.MenuItem
import id.ac.istts.seton.R
import id.ac.istts.seton.Screens
import id.ac.istts.seton.calendarPage.CalendarActivity
import id.ac.istts.seton.config.ApiConfiguration
import id.ac.istts.seton.landingPage.LandingPageActivity
import id.ac.istts.seton.loginRegister.LoginActivity
import id.ac.istts.seton.projectPage.ListProjectActivity
import id.ac.istts.seton.taskPage.TaskActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReportActivity : ComponentActivity() {
    private val vm: ReportViewModel by viewModels<ReportViewModel>()
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
                    title = "Logout",
                    route = Screens.Logout.route,
                    selectedIcon = Icons.Filled.Logout,
                    unSelectedIcon = Icons.Outlined.Logout
                ),
            )

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

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
                                        val ioScope = CoroutineScope(Dispatchers.Main)
                                        ioScope.launch {
                                            ApiConfiguration.defaultRepo.logoutUser()
                                        }

                                        if(mAuth.currentUser != null){
                                            mAuth.signOut()
                                            mGoogleSignInClient.signOut()
                                        }

                                        val intent = Intent(this@ReportActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Dashboard.route -> {
                                        val intent = Intent(this@ReportActivity, LandingPageActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Projects.route -> {
                                        val intent = Intent(this@ReportActivity, ListProjectActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Tasks.route -> {
                                        val intent = Intent(this@ReportActivity, TaskActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Calendar.route -> {
                                        val intent = Intent(this@ReportActivity, CalendarActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Report.route -> {

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
                            name = "Report",
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                    }
                ) {
                    val hai = it
                    reportPage()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview(showBackground = true)
    @Composable
    fun reportPage() {
        val userProjects by vm.projects.observeAsState(emptyList())
        val memberProjects by vm.members.observeAsState(emptyList())
        LaunchedEffect(key1 = Unit) {
            vm.getUserProjects(userEmail)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
//                .padding(24.dp)
                .padding(24.dp, 24.dp, 24.dp, 0.dp)
        ) {
            item {
                if (userProjects.isNotEmpty()) {
                    var expanded by remember { mutableStateOf(false) }
                    var selectedProject by remember { mutableStateOf(userProjects[0]) }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier
                            .padding(top = 32.dp, bottom = 12.dp)
                    ) {
                        TextField(
                            readOnly = true,
                            value = selectedProject.name,
                            onValueChange = {},
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = OutlinedTextFieldDefaults.colors(),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            userProjects.forEachIndexed { idx, project ->
                                DropdownMenuItem(
                                    text = { Text(text = project.name) },
                                    onClick = {
                                        selectedProject = userProjects[idx]
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }

                    var currentMonth by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH)) }
                    var currentYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }

                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.MONTH, currentMonth)
                    calendar.set(Calendar.YEAR, currentYear)

                    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                    val monthYearString = dateFormat.format(calendar.time)

                    val tasksInMonth = selectedProject.tasks.filter {
                        val taskDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.deadline)
                        taskDate.month == currentMonth && taskDate.year == currentYear - 1900
                    }.groupBy {
                        SimpleDateFormat("d", Locale.getDefault()).format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.deadline)).toInt()
                    }.mapValues { it.value.size }

                    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                    val chartData = (1..daysInMonth).map { day ->
                        tasksInMonth[day] ?: 0
                    }
                    val maxTask = chartData.maxOrNull() ?: 0

                    val taskData = (1..daysInMonth).map { it.toString() }

                    val taskModel = CartesianChartModel(LineCartesianLayerModel.build {
                        series(y = chartData)
                    })

                    Card(
                        elevation = 10.dp,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Tasks",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(onClick = {
                                    if (currentMonth == 0) {
                                        currentMonth = 11
                                        currentYear -= 1
                                    } else {
                                        currentMonth -= 1
                                    }
                                }) {
                                    Icon(painterResource(id = R.drawable.ic_arrow_left), contentDescription = "Previous Month")
                                }

                                Text(
                                    text = monthYearString,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                IconButton(onClick = {
                                    if (currentMonth == 11) {
                                        currentMonth = 0
                                        currentYear += 1
                                    } else {
                                        currentMonth += 1
                                    }
                                }) {
                                    Icon(painterResource(id = R.drawable.ic_arrow_right), contentDescription = "Next Month")
                                }
                            }

                            CartesianChartHost(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    //                                .background(Color.White, RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                chart = rememberCartesianChart(
                                    rememberLineCartesianLayer(
                                        lines = listOf(
                                            rememberLineSpec(
                                                shader = DynamicShader.color(Color(0xFF0E9794)),
                                                backgroundShader = DynamicShader.color(Color.Transparent),
                                                pointConnector = DefaultPointConnector(0f),
                                            )
                                        )
                                    ),
                                    startAxis = rememberStartAxis(
                                        itemPlacer = if (maxTask <= 7)
                                            AxisItemPlacer.Vertical.count(count = { maxTask + 1 }, true)
                                        else
                                            AxisItemPlacer.Vertical.step(step = { 1f }, true)
                                    ),
                                    bottomAxis = rememberBottomAxis(
                                        valueFormatter = { value, _, _ -> taskData[value.toInt()] }
                                    ),

                                ),
                                marker = DefaultCartesianMarker(
                                    label = rememberTextComponent(textSize = 16.sp, margins = Dimensions(0f, 12f, 0f, 12f)),
                                    labelPosition = DefaultCartesianMarker.LabelPosition.AbovePoint,
                                    indicator = rememberShapeComponent(Shape.Pill, strokeWidth = 8.dp)

                                ),

                                model = taskModel,
                            )
                        }

                    }

                    val upcomingTasks = selectedProject.tasks.count { it.status == 0 }
                    val ongoingTasks = selectedProject.tasks.count { it.status == 1 }
                    val completedTasks = selectedProject.tasks.count { it.status == 2 }
                    val revisionTasks = selectedProject.tasks.count { it.status == 3 }

                    val pieChartData = mutableListOf<PieData>()

                    if (upcomingTasks > 0) {
                        pieChartData.add(PieData(value = upcomingTasks.toFloat(), label = "Upcoming Tasks: $upcomingTasks Tasks", color = Color(0xFFF76A6A), labelColor = Color.Black))
                    }
                    if (ongoingTasks > 0) {
                        pieChartData.add(PieData(value = ongoingTasks.toFloat(), label = "Ongoing Tasks: $ongoingTasks Tasks", color = Color(0xFFF0E68C), labelColor = Color.Black))
                    }
                    if (completedTasks > 0) {
                        pieChartData.add(PieData(value = completedTasks.toFloat(), label = "Completed Tasks: $completedTasks Tasks", color = Color(0xFFADD8E6), labelColor = Color.Black))
                    }
                    if (revisionTasks > 0) {
                        pieChartData.add(PieData(value = revisionTasks.toFloat(), label = "Revision Tasks: $revisionTasks Tasks", color = Color(0xFF90EE90), labelColor = Color.Black))
                    }

                    Card(
                        elevation = 10.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Tasks Percentage",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

                            PieChart(
                                modifier = Modifier
                                    .padding(vertical = 20.dp)
                                    .align(Alignment.CenterHorizontally),
                                data = pieChartData,
                                style = PieChartStyle(
                                    visibility = PieChartVisibility(
                                        isLabelVisible = true,
                                        isPercentageVisible = true
                                    ),
                                    labelSize = 10.sp,
                                    percentageSize = 10.sp
                                ),
                                onSliceClick = { pieData ->
                                    Toast.makeText(this@ReportActivity, "${pieData.label}", Toast.LENGTH_SHORT).show()
                                }
                            )

                            Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                LegendItem(color = Color(0xFFF76A6A), label = "Upcoming Tasks: $upcomingTasks Tasks")
                                LegendItem(color = Color(0xFFF0E68C), label = "Ongoing Tasks: $ongoingTasks Tasks")
                                LegendItem(color = Color(0xFFADD8E6), label = "Completed Tasks: $completedTasks Tasks")
                                LegendItem(color = Color(0xFF90EE90), label = "Revision Tasks: $revisionTasks Tasks")
                            }
                        }

                    }

                    val memberTaskData = selectedProject.tasks.groupBy { it.pic_email }.mapValues { entry ->
                        val completedTasks = entry.value.count { it.status == 2 }
                        val remainingTasks = entry.value.count { it.status in 0..1 }
                        completedTasks to remainingTasks
                    }

                    LaunchedEffect(key1 = Unit){
                        vm.getUserPIC(memberTaskData.keys.toList())
                    }
                    val completedTasksProgress = memberTaskData.values.map { it.first }
                    val remainingTasksProgress = memberTaskData.values.map { it.second }

                    val maxProgress = (completedTasksProgress + remainingTasksProgress).maxOrNull() ?: 0

                    val memberProgressModel = CartesianChartModel(ColumnCartesianLayerModel.build {
                        series(y = completedTasksProgress)
                        series(y = remainingTasksProgress)
                    })


                    if(memberProjects.isNotEmpty()){
                        Card(
                            elevation = 10.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        ){
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = "Progress",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )

                                CartesianChartHost(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp)
                                        .background(Color.White, RoundedCornerShape(8.dp))
                                        .padding(10.dp),
                                    chart = rememberCartesianChart(
                                        rememberColumnCartesianLayer(
                                            columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                                                listOf(
                                                    rememberLineComponent(
                                                        Color(0xFF0E9794),
                                                        24f.dp,
                                                        Shape.rounded(0),
                                                    ),
                                                    rememberLineComponent(
                                                        Color(0xFF8CFAFF),
                                                        24f.dp,
                                                        Shape.rounded(0),
                                                    )
                                                )
                                            ),
                                            dataLabel = rememberTextComponent(textSize = 14.sp)
                                        ),
                                        startAxis = rememberStartAxis(
                                            itemPlacer = if (maxProgress < 10)
                                                AxisItemPlacer.Vertical.count(count = { maxProgress + 1 }, true)
                                            else
                                                AxisItemPlacer.Vertical.step(step = { 1f }, true)
                                        ),
                                        bottomAxis = rememberBottomAxis(
                                            valueFormatter = { value, _, _ -> memberProjects[value.toInt()].name }
                                        ),
                                    ),
                                    model = memberProgressModel,
                                )

                                Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                    LegendItem(color = Color(0xFF0E9794), label = "Task Completed")
                                    LegendItem(color = Color(0xFF8CFAFF), label = "Task Remaining")
                                }

                            }

                        }

                    }
                    
                    Spacer(modifier = Modifier.padding(top = 20.dp))
                }
            }
        }
    }

    @Composable
    fun LegendItem(color: Color, label: String) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(color, RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontSize = 12.sp)
        }
    }


}