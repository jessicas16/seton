package id.ac.istts.seton.projectPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.binayshaw7777.kotstep.ui.vertical.VerticalIconStepper
import id.ac.istts.seton.AppBar
import id.ac.istts.seton.AppFont
import id.ac.istts.seton.DrawerBody
import id.ac.istts.seton.DrawerHeader
import id.ac.istts.seton.MenuItem
import id.ac.istts.seton.R
import id.ac.istts.seton.Screens
import id.ac.istts.seton.calendarPage.CalendarActivity
import id.ac.istts.seton.entity.ProjectDetailDRO
import id.ac.istts.seton.entity.Users
import id.ac.istts.seton.reportPage.ReportActivity
import id.ac.istts.seton.settingPage.SettingActivity
import id.ac.istts.seton.taskPage.DataTask
import id.ac.istts.seton.taskPage.TaskActivity
import id.ac.istts.seton.taskPage.TaskDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class ProjectDetailsActivity : ComponentActivity() {
    private val vm: ProjectDetailsViewModel by viewModels<ProjectDetailsViewModel>()
    private lateinit var scope: CoroutineScope
    private lateinit var projectId : String
    private lateinit var userEmail : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectId = intent.getStringExtra("projectId").toString()
        userEmail = intent.getStringExtra("userEmail").toString()
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
                    title = "Settings",
                    route = Screens.Settings.route,
                    selectedIcon = Icons.Filled.Settings,
                    unSelectedIcon = Icons.Outlined.Settings
                ),
            )

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
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

                                        val intent = Intent(this@ProjectDetailsActivity, SettingActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Projects.route -> {
                                        val intent = Intent(this@ProjectDetailsActivity, ListProjectActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Tasks.route -> {
                                        val intent = Intent(this@ProjectDetailsActivity, TaskActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Calendar.route -> {
                                        val intent = Intent(this@ProjectDetailsActivity, CalendarActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Report.route -> {
                                        val intent = Intent(this@ProjectDetailsActivity, ReportActivity::class.java)
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
                            name = "Project Details",
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                    }
                ) {
                    val hai = it
                    ProjectDetail()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        vm.getProjectById(projectId)
    }

    @Preview(showBackground = true)
    @Composable
    fun ProjectDetail() {
        val page = remember { mutableStateOf("Details") }
        val context = LocalContext.current
        val projectDetail by vm.projects.observeAsState(
            ProjectDetailDRO(
                status = "500",
                message = "an error occurred",
                data = DetailProject(
                    projectName = "",
                    projectDescription = "",
                    projectStart = "",
                    projectDeadline = "",
                    projectManager = Users(email = "",name = "",profile_picture = "",password = "",auth_token = "",status = 0),
                    members = listOf(),
                    tasks = listOf(),
                    upcomingTask = 0,
                    ongoingTask = 0,
                    submittedTask = 0,
                    revisionTask = 0,
                    completedTask = 0,
                    projectStatus = ""
                )
            )
        )
        LaunchedEffect(key1 = Unit) {
            vm.getProjectById(projectId)
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            val (projectName, detailsOrBoard, projectOverview) = createRefs()

            Text(
                text = projectDetail.data.projectName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(
                    Font(R.font.open_sans_bold, FontWeight.Bold)
                ),
                modifier = Modifier
                    .constrainAs(projectName) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(2f)
                    .shadow(10.dp, MaterialTheme.shapes.medium)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .constrainAs(detailsOrBoard) {
                        top.linkTo(projectName.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    if(page.value == "Details") {
                        Modifier
                            .background(Color(0xFFD8FDFF))
                            .padding(10.dp)
                            .weight(1f)
                            .clickable { page.value = "Details" }
                    } else {
                        Modifier
                            .background(Color.White)
                            .padding(10.dp)
                            .weight(1f)
                            .clickable { page.value = "Details" }
                    }
                ){
                    val color = if (page.value == "Details") Color(0xFF0E9794) else Color.Black

                    Text(
                        text = "Details",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_bold, FontWeight.Bold)
                        ),
                        color = color
                    )
                }
                Box(
                    if(page.value == "Boards") {
                        Modifier
                            .background(Color(0xFFD8FDFF))
                            .padding(10.dp)
                            .weight(1f)
                            .clickable { page.value = "Boards" }
                    } else {
                        Modifier
                            .background(Color.White)
                            .padding(10.dp)
                            .weight(1f)
                            .clickable { page.value = "Boards" }
                    }

                ){
                    val color = if (page.value == "Boards") Color(0xFF0E9794) else Color.Black

                    Text(
                        text = "Boards",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_bold, FontWeight.Bold)
                        ),
                        color = color
                    )
                }
            }

            if(page.value == "Details") {
                val invitedUser = mutableListOf<String>()
                val invitedUserTask by vm.invitedUsers.observeAsState(emptyList())
                LaunchedEffect(key1 = Unit) {
                    vm.invitedUsers.value
                }

                Log.i("INVITED_USER", invitedUserTask.toString())

                val shouldShowDialog = remember { mutableStateOf(false) }
                if (shouldShowDialog.value) {
                    MyDialog(
                        invitedUser = invitedUser,
                        pm_email = projectDetail.data.projectManager.email
                    ){ shouldShowDialog.value = false }
                }

                Column(modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .constrainAs(projectOverview) {
                        top.linkTo(detailsOrBoard.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                ){
                    Box(
                        modifier = Modifier
                            .padding(0.dp, 10.dp)
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(10.dp))
                            .shadow(10.dp, MaterialTheme.shapes.medium)
                            .background(Color.White)
                    ){
                        Column {
                            Text(
                                text = "Project Overview",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(10.dp),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(
                                    Font(R.font.open_sans_bold, FontWeight.Bold)
                                ),
                                color = Color.Black
                            )
                            ProjectDetails(
                                text1 = "Project Manager",
                                text2 = projectDetail.data.projectManager.name,
                            )
                            ProjectDetails(
                                text1 = "Deadline",
                                text2 = projectDetail.data.projectDeadline,
                            )
                            ProjectDetails(
                                text1 = "Status",
                                text2 = projectDetail.data.projectStatus,
                            )
                            ProjectDetails(
                                text1 = "Description",
                                text2 = projectDetail.data.projectDescription,
                            )

                            Row (
                                modifier = Modifier
                                    .padding(8.dp, 4.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Members : ",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(
                                        Font(R.font.open_sans_regular, FontWeight.Normal)
                                    ),
                                    color = Color.Gray,
                                    modifier = Modifier.weight(1.5f)
                                )

                                Button(
                                    onClick = {
                                        shouldShowDialog.value = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD8FDFF)),
                                ) {
                                    Text(
                                        text = "Invite",
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(
                                            Font(R.font.open_sans_regular, FontWeight.Normal)
                                        ),
                                        color = Color(0xFF0E9794)
                                    )
                                }
                            }

                            LazyColumn(
                                Modifier
                                    .fillMaxWidth()
                                    .heightIn(0.dp, 150.dp)
                                    .padding(8.dp, 16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ){
                                items(invitedUserTask){ member ->
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            //inisial
                                            val arrName = member.name.split(" ")
                                            val nama = arrName[0].first().uppercaseChar().toString() + if (arrName.size > 1) arrName[1].first().uppercaseChar().toString() else ""
                                            Box(
                                                modifier = Modifier
                                                    .size(48.dp)
                                                    .background(
                                                        color = Color(0xFFECFFFF),
                                                        shape = RoundedCornerShape(24.dp)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = nama,
                                                    fontSize = 20.sp,
                                                    fontFamily = FontFamily(
                                                        Font(R.font.open_sans_bold, FontWeight.Bold)
                                                    ),
                                                    color = Color(0xFF0E9794)
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(4.dp))

                                            //nama
                                            Text(
                                                text = member.name,
                                                fontSize = 14.sp,
                                                fontFamily = FontFamily(
                                                    Font(R.font.open_sans_regular, FontWeight.Normal)
                                                ),
                                                color = Color.Black
                                            )
                                        }
                                        Row{
                                            IconButton(
                                                onClick = {
                                                    scope.launch {
                                                        vm.removeMember(projectId, member.email)
                                                        delay(1000)
                                                        val res = vm.response.value
                                                        Log.i("RES", res.toString())
                                                        runOnUiThread {
                                                            if (res != null) {
                                                                Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                                                            }
                                                        }
                                                    }
                                                },
                                            ){
                                                Icon(
                                                    imageVector = Icons.Default.HorizontalRule,
                                                    contentDescription = "Remove",
                                                    modifier = Modifier
                                                        .size(40.dp),
                                                    Color.Red,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(0.dp, 10.dp)
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(10.dp))
                            .shadow(10.dp, MaterialTheme.shapes.medium)
                            .background(Color.White)
                    ){
                        Column {
                            Text(
                                text = "Stats",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(10.dp),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(
                                    Font(R.font.open_sans_bold, FontWeight.Bold)
                                ),
                                color = Color.Black
                            )

                            Row (
                                modifier = Modifier
                                    .padding(8.dp, 4.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                ProjectStats(
                                    text = "Project Members",
                                    content = projectDetail.data.members.size,
                                    uom = "members"
                                )
                                ProjectStats(
                                    text = "Upcoming Tasks",
                                    content = projectDetail.data.upcomingTask,
                                    uom = "tasks"
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .padding(8.dp, 4.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ProjectStats(
                                    text = "Ongoing Tasks",
                                    content = projectDetail.data.ongoingTask,
                                    uom = "tasks"
                                )
                                ProjectStats(
                                    text = "Completed Tasks",
                                    content = projectDetail.data.completedTask,
                                    uom = "tasks"
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(0.dp, 10.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
                            .shadow(10.dp, MaterialTheme.shapes.medium)
                            .background(Color.White)

                    ){
                        Column (
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                        ){
                            Text(
                                text = "Recent Changes",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(10.dp),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(
                                    Font(R.font.open_sans_bold, FontWeight.Bold)
                                ),
                                color = Color.Black
                            )

                            val upcoming = mutableListOf<String>()
                            val ongoing = mutableListOf<String>()
                            val submitted = mutableListOf<String>()
                            val revision = mutableListOf<String>()
                            val completed = mutableListOf<String>()
                            for (i in 0 until projectDetail.data.tasks.size) {
                                when (projectDetail.data.tasks[i].status) {
                                    0 -> upcoming.add(projectDetail.data.tasks[i].title)
                                    1 -> ongoing.add(projectDetail.data.tasks[i].title)
                                    2 -> submitted.add(projectDetail.data.tasks[i].title)
                                    3 -> revision.add(projectDetail.data.tasks[i].title)
                                    4 -> completed.add(projectDetail.data.tasks[i].title)
                                }
                            }

                            Column {
                                if(upcoming.isNotEmpty()){
                                    Stepper(
                                        list = upcoming,
                                        warna = Color(0xFFFFDD60)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                                if(ongoing.isNotEmpty()){
                                    Stepper(
                                        list = ongoing,
                                        warna = Color(0xFFF4976C)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                                if(submitted.isNotEmpty()){
                                    Stepper(
                                        list = submitted,
                                        warna = Color(0xFF6AC0BE)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                                if(revision.isNotEmpty()){
                                    Stepper(
                                        list = revision,
                                        warna = Color(0xFFF8C5AE)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                                if(completed.isNotEmpty()){
                                    Stepper(
                                        list = completed,
                                        warna = Color(0xFF0E9794)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(75.dp))

                }
            } else {
//                klo dipilih board
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                        .padding(top = 24.dp)
                        .heightIn(0.dp, 700.dp)
                        .constrainAs(projectOverview) {
                            top.linkTo(detailsOrBoard.bottom, margin = 24.dp)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        }
                ){
                    TaskPreview(userEmail = userEmail)
                }
                FloatingButton()
            }
        }
    }

    @Composable
    fun FloatingButton() {
        val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp, 32.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Button(
                onClick = {
                    val intent = Intent(context, AddTaskActivity::class.java)
                    intent.putExtra("projectId", projectId)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF0E9794)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                modifier = Modifier.size(72.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_plus),
                    contentDescription = "Plus Icon"
                )
            }
        }
    }

    @Composable
    fun ProjectDetails(
        text1: String,
        text2: String,
    ) {
        var tgl = ""
        if(text1 == "Deadline"){
            val monthMap = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
            if(text2 != ""){
                tgl =  "${text2.substring(8, 10)} ${monthMap[text2.substring(5, 7).toInt() - 1]} ${text2.substring(0, 4)}"
            }
        }

        Row (
            modifier = Modifier
                .padding(8.dp, 4.dp)
                .fillMaxWidth(),
        ){
            Text(
                text = text1,
                fontSize = 14.sp,
                fontFamily = FontFamily(
                    Font(R.font.open_sans_regular, FontWeight.Normal)
                ),
                color = Color.Gray,
                modifier = Modifier.weight(1.5f)
            )

            val color = if (text2 == "Ongoing") Color(0xFFF4976C) else if (text2 == "Completed") Color(0xFF0E9794) else Color.Black
            val font = if (text2 == "Ongoing" || text2 == "Completed") FontWeight.Bold else FontWeight.Normal
            Text(
                text = if(text1 == "Deadline") tgl else text2,
                fontSize = 14.sp,
                fontWeight = font,
                fontFamily = FontFamily(
                    Font(R.font.open_sans_regular, FontWeight.Normal)
                ),
                color = color,
                modifier = Modifier.weight(2f)
            )
        }
    }

    @Composable
    fun ProjectStats(
        text : String,
        content : Int,
        uom : String
    ){
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp, 4.dp)
        ) {
            Text(text = text,
                fontSize = 14.sp,
                fontFamily = FontFamily(
                    Font(R.font.open_sans_regular, FontWeight.Normal)
                ),
            )

            Text(text = content.toString(),
                fontSize = 18.sp,
                fontFamily = FontFamily(
                    Font(R.font.open_sans_bold, FontWeight.Bold)
                ),
            )

            Text(text = uom,
                fontSize = 14.sp,
                fontFamily = FontFamily(
                    Font(R.font.open_sans_regular, FontWeight.Normal)
                ),
                color = Color.Gray
            )
        }
    }

    @Composable
    fun Stepper (
        list : MutableList<String>,
        warna : Color
    ){
        Box {
            VerticalIconStepper(
                modifier = Modifier
                    .padding(10.dp),
                totalSteps = list.size,
                currentStep = 0,
                stepSize = 25.dp,
                stepIconsList = list.map { Icons.Default.Check },
                incompleteColor = warna,
                checkMarkColor = warna
            )
            for (i in 0..< list.size){
                Column (
                    modifier = Modifier
                        .padding(top = 12.dp + (i * 43.dp))
                        .padding(start = 45.dp)
                ) {
                    Text(
                        text = list[i],
                        fontSize = 16.sp,
                        fontFamily = FontFamily( Font(R.font.open_sans_semi_bold, FontWeight.SemiBold)),
                        color = warna
                    )
                }
            }
        }
    }

    @Composable
    fun MyDialog(
        invitedUser : MutableList<String>,
        pm_email : String,
        onDismiss:() -> Unit
    ) {
        val context = LocalContext.current
        var email by remember { mutableStateOf("")}
        Dialog(onDismissRequest = { onDismiss() }) {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(12.dp),
            ) {
                Column(
                    Modifier
                        .background(Color.White)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Invite New Member",
                        modifier = Modifier.padding(4.dp),
                        fontSize = 20.sp
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.padding(8.dp),
                        label = { Text("Enter an Email") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done,
                        ),
                    )

                    Row {
                        OutlinedButton(
                            onClick = { onDismiss() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Cancel",
                                color = Color(0xFF0E9794)
                            )
                        }

                        Button(
                            onClick = {
                                if (email.isEmpty()){
                                    Toast.makeText(context, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                                } else {
                                    if(email == pm_email){
                                        Toast.makeText(context, "You cannot invite project manager", Toast.LENGTH_SHORT).show()
                                        email = ""
                                    } else {
                                        scope.launch {
                                            vm.checkEmailUser(projectId, email)
                                            delay(1000)
                                            val res = vm.checkEmail.value
                                            Log.i("RES2", res.toString())
                                            runOnUiThread{
                                                if(res != null){
                                                    if(res.status == "200"){
                                                        invitedUser.add(res.data.name)
                                                        Log.i("INVITED_USER", invitedUser.toString())

                                                        Toast.makeText(context, "Success invite new member", Toast.LENGTH_SHORT).show()
                                                        onDismiss()
                                                    } else {
                                                        Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                                                    }
                                                    email = ""
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD8FDFF)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Invite",
                                color = Color(0xFF0E9794)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TaskPreview(
        userEmail: String
    ) {
        val userTasks by vm.tasksProject.observeAsState(emptyList())
        LaunchedEffect(key1 = Unit) {
            vm.getUserTasks(projectId)
        }
        LazyColumn(
            Modifier
                .padding(16.dp, 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(userTasks) {
                RowPart(it.first, it.second)
            }
        }
    }

    @Composable
    fun RowPart(title: String, data: List<DataTask>) {
        Row {
            Column {
                Row(Modifier.padding(top = 16.dp)) {
                    androidx.compose.material3.Text(
                        modifier = Modifier.padding(),
                        text = title,
                        fontFamily = AppFont.fontSemiBold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    androidx.compose.material3.Text(
                        modifier = Modifier
                            .weight(6f),
                        text = data.size.toString(),
                        fontFamily = AppFont.fontSemiBold,
                        fontSize = 20.sp,
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                }
                if (data.isEmpty()) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.Text(
                            modifier = Modifier
                                .weight(6f),
                            text = "You have no $title Task",
                            fontFamily = AppFont.fontSemiBold,
                            fontSize = 16.sp,
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
                                    androidx.compose.material3.Text(
                                        text = it.title,
                                        fontFamily = AppFont.fontBold,
                                        fontSize = 16.sp
                                    )
                                }
                                Row(Modifier.padding(vertical = 4.dp)) {
                                    androidx.compose.material3.Text(
                                        text = it.project.name,
                                        fontSize = 12.sp,
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
                                        androidx.compose.material3.Text(
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
                                        androidx.compose.material3.Text(
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
                                        androidx.compose.material3.Text(
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
                                val intent = Intent(this@ProjectDetailsActivity, TaskDetailActivity::class.java)
                                intent.putExtra("userEmail", userEmail)
                                intent.putExtra("taskId", it.id.toString())
                                startActivity(intent)
                            }, verticalAlignment = Alignment.CenterVertically) {
                                androidx.compose.material3.Text(
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
                                        androidx.compose.material3.Text(
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
}
