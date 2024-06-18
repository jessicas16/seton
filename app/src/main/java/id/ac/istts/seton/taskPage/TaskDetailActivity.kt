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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckBox
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TaskDetailActivity : ComponentActivity() {
    private val vm: TaskDetailViewModel by viewModels<TaskDetailViewModel>()
    private lateinit var scope: CoroutineScope
    private lateinit var taskId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskId = intent.getStringExtra("taskId").toString()
        Toast.makeText(this, taskId, Toast.LENGTH_SHORT).show()
        setContent{
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
            scope = rememberCoroutineScope()
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
                    DetailTask()
                }
            }

        }
    }

    @Composable
    private fun DetailTask() {
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
                    priority = -1,
                    status = -1,
                    pic = Users(
                        email = "",
                        name = "",
                        profile_picture = "",
                        password = "",
                        auth_token = "",
                        status = -1
                    ),
                    project = Projects(
                        id = -1,
                        name = "",
                        description = "",
                        start = "",
                        deadline = "",
                        pm_email = "",
                        status = -1
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

        //DIALOG
        val showLabelDialog = remember { mutableStateOf(false) }
        val showCheckListDialog = remember { mutableStateOf(false) }
        val showAttachmentsDialog = remember { mutableStateOf(false) }
        if (showLabelDialog.value) {
            ModalForLabels(
                taskId = taskId
            ){ showLabelDialog.value = false }
        }

        if (showCheckListDialog.value) {
            ModalForChecklists(
                taskId = taskId
            ){ showCheckListDialog.value = false }
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            val (title, member, desc, add, attach, goal, comment) = createRefs()

            //overview
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
                        val monthMap = listOf(
                            "January",
                            "February",
                            "March",
                            "April",
                            "May",
                            "June",
                            "July",
                            "August",
                            "September",
                            "October",
                            "November",
                            "December"
                        )
                        var tgl = ""
                        if (taskDetail.data.deadline != "") {
                            tgl = "${
                                taskDetail.data.deadline.substring(
                                    8,
                                    10
                                )
                            } ${
                                monthMap[taskDetail.data.deadline.substring(5, 7).toInt() - 1]
                            } ${taskDetail.data.deadline.substring(0, 4)}"
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
                        var status = ""
                        when (taskDetail.data.status) {
                            0 -> status = "Upcoming"
                            1 -> status = "Ongoing"
                            2 -> status = "Submitted"
                            3 -> status = "Revision"
                            4 -> status = "Completed"
                        }

                        val expanded = remember { mutableStateOf(false) }
                        val selectedText = remember { mutableStateOf("asd") }
                        selectedText.value = status

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
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable { expanded.value = true }
                                    .padding(horizontal = 0.dp, vertical = 8.dp)
                            ) {
                                var warna = Color(0xFFFFF176)
                                when (selectedText.value){
                                    "Upcoming" -> warna = Color(0xFFFFF176)
                                    "Ongoing" -> warna = Color(0xFFFF8A65)
                                    "Submitted" -> warna = Color(0xFF4DD0E1)
                                    "Revision" -> warna = Color(0xFFFFCC80)
                                    "Completed" -> warna = Color(0xFF00897B)
                                }
                                Text(
                                    text = selectedText.value,
                                    color = warna,
                                    fontSize = 16.sp
                                )
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = "Arrow Down",
                                    tint = Color.Gray,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = expanded.value,
                                onDismissRequest = { expanded.value = false },
                            ) {
                                items.forEach { (text, color) ->
                                    DropdownMenuItem(
                                        onClick = {
                                            if (selectedText.value != text) {
                                                selectedText.value = text
                                                expanded.value = false

                                                var statusInt = "-1"
                                                when (selectedText.value) {
                                                    "Upcoming" -> statusInt = "0"
                                                    "Ongoing" -> statusInt = "1"
                                                    "Submitted" -> statusInt = "2"
                                                    "Revision" -> statusInt = "3"
                                                    "Completed" -> statusInt = "4"
                                                }
                                                vm.updateTaskStatus(taskId, statusInt)
                                            }
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

            //PIC and Team
            Row(
                modifier = Modifier.constrainAs(member) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                horizontalArrangement = Arrangement.Start
            ) {
                Column (
                    modifier = Modifier.weight(1f)
                ){
                    Text(
                        text = "PIC",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_bold, FontWeight.Bold)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    val picName = taskDetail.data.pic.name
                    if(picName != ""){
                        val arrName = picName.split(' ')
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
                    }
                }
                Column (
                    modifier = Modifier.weight(2f)
                ){
                    Text(
                        text = "Team",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_bold, FontWeight.Bold)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyRow(
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        items(taskDetail.data.teams) { team ->
                            val arrName = team.name.split(' ')
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
                        }
                    }
                }
            }

            //description
            Row(
                modifier = Modifier.constrainAs(desc) {
                    top.linkTo(member.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                horizontalArrangement = Arrangement.Start,
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Description",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_bold, FontWeight.Bold)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = taskDetail.data.description,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_regular, FontWeight.Normal)
                        )
                    )
                }
            }

            //add items
            Row(
                modifier = Modifier.constrainAs(add) {
                    top.linkTo(desc.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                horizontalArrangement = Arrangement.Start,
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Add Items",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_bold, FontWeight.Bold)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        AddItems(
                            text = "Labels",
                            icon = Icons.Filled.Discount
                        ) {
                            showLabelDialog.value = true
                        }
                        AddItems(
                            text = "Checklist",
                            icon = Icons.Filled.CheckBox
                        ) {
                            showCheckListDialog.value = true
                        }
                        AddItems(
                            text = "Attachments",
                            icon = Icons.Filled.Attachment
                        ) {
                            showAttachmentsDialog.value = true
                        }
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        vm.getTaskById(taskId)
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

    @Composable
    private fun AddItems(
        text: String,
        icon: ImageVector,
        onClick: () -> Unit
    ) {
        Box(modifier = Modifier
            .background(
                color = Color(0xFFECFFFF),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
            contentAlignment = Alignment.Center)
        {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Add Items",
                    modifier = Modifier
                        .size(20.dp),
                    tint = Color(0xFF0E9794)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = text,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(
                        Font(R.font.open_sans_regular, FontWeight.Normal)
                    )
                )
            }
        }
    }

    @Composable
    private fun ModalForLabels(
        taskId : String,
        onDismiss:() -> Unit
    ){
        val context = LocalContext.current
        var title by remember { mutableStateOf("")}
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
                    androidx.compose.material.Text(
                        text = "Add New Label",
                        modifier = Modifier.padding(4.dp),
                        fontSize = 20.sp
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.padding(8.dp),
                        label = { Text("Title") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
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
                            androidx.compose.material.Text(
                                text = "Cancel",
                                color = Color(0xFF0E9794)
                            )
                        }

                        Button(
                            onClick = {
                                if (title.isEmpty()){
                                    Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                                } else {
                                    scope.launch {
                                        vm.addNewLabel(taskId, title)
                                        delay(1000)
                                        val res = vm.label.value
                                        runOnUiThread{
                                            if(res != null){
                                                if(res.status == "201"){
                                                    Toast.makeText(context, "Success add new Label", Toast.LENGTH_SHORT).show()
                                                    onDismiss()
                                                } else {
                                                    Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                                                }
                                                title = ""
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
                                text = "Add",
                                color = Color(0xFF0E9794)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ModalForChecklists(
        taskId : String,
        onDismiss:() -> Unit
    ){
        val context = LocalContext.current
        var goal by remember { mutableStateOf("")}
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
                    androidx.compose.material.Text(
                        text = "Add New Goals",
                        modifier = Modifier.padding(4.dp),
                        fontSize = 20.sp
                    )

                    OutlinedTextField(
                        value = goal,
                        onValueChange = { goal = it },
                        modifier = Modifier.padding(8.dp),
                        label = { Text("Goals") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
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
                            androidx.compose.material.Text(
                                text = "Cancel",
                                color = Color(0xFF0E9794)
                            )
                        }

                        Button(
                            onClick = {
                                if (goal.isEmpty()){
                                    Toast.makeText(context, "Goal cannot be empty", Toast.LENGTH_SHORT).show()
                                } else {
                                    scope.launch {
                                        vm.addNewChecklist(taskId, goal)
                                        delay(1000)
                                        val res = vm.label.value
                                        runOnUiThread{
                                            if(res != null){
                                                if(res.status == "201"){
                                                    Toast.makeText(context, "Success add new Checklist", Toast.LENGTH_SHORT).show()
                                                    onDismiss()
                                                } else {
                                                    Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                                                }
                                                title = ""
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
                                text = "Add",
                                color = Color(0xFF0E9794)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun String.toColor() = Color(parseColor(this))
}
