package id.ac.istts.seton.taskPage

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color.parseColor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
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
import id.ac.istts.seton.entity.AddCommentDTO
import id.ac.istts.seton.entity.Projects
import id.ac.istts.seton.entity.TaskDRO
import id.ac.istts.seton.entity.Users
import id.ac.istts.seton.env
import id.ac.istts.seton.loginRegister.LoginActivity
import id.ac.istts.seton.mainPage.DashboardActivity
import id.ac.istts.seton.projectPage.ListProjectActivity
import id.ac.istts.seton.reportPage.ReportActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskDetailActivity : ComponentActivity() {
    private val vm: TaskDetailViewModel by viewModels<TaskDetailViewModel>()
    private lateinit var scope: CoroutineScope
    private lateinit var taskId : String
    private lateinit var userEmail : String
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskId = intent.getStringExtra("taskId").toString()
        userEmail = intent.getStringExtra("userEmail").toString()
        setContent{
            scope = rememberCoroutineScope()
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

                                        val intent = Intent(this@TaskDetailActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Dashboard.route -> {
                                        val intent = Intent(this@TaskDetailActivity, DashboardActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Projects.route -> {
                                        val intent = Intent(this@TaskDetailActivity, ListProjectActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Tasks.route -> {
                                        val intent = Intent(this@TaskDetailActivity, TaskActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Calendar.route -> {
                                        val intent = Intent(this@TaskDetailActivity, CalendarActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Report.route -> {
                                        val intent = Intent(this@TaskDetailActivity, ReportActivity::class.java)
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
                    DetailTask()
                }
            }

        }
    }

    @Composable
    private fun DetailTask() {
        val allAttachment by vm.getAttachment.observeAsState(emptyList())
        LaunchedEffect(key1 = Unit) {
            vm.getAttachments(taskId)
        }
        val allChecklist by vm.getChecklist.observeAsState(emptyList())
        LaunchedEffect(key1 = Unit) {
            vm.getChecklist(taskId)
        }
        val allComment by vm.getComment.observeAsState(emptyList())
        LaunchedEffect(key1 = Unit) {
            vm.getAllComment(taskId)
        }
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
            ){
                showLabelDialog.value = false
                vm.getTaskById(taskId)
            }
        }

        if (showAttachmentsDialog.value) {
            ModalforAttachments(
                taskId = taskId
            ){
                showAttachmentsDialog.value = false
                vm.getTaskById(taskId)
            }
        }

        if (showCheckListDialog.value) {
            ModalForChecklists(
                taskId = taskId
            ){
                showCheckListDialog.value = false
                vm.getTaskById(taskId)
            }
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
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
                                taskDetail.data.deadline.substring( 8, 10)
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
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, end = 8.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            val percentage = allChecklist.filter { it.is_checked == 1 }.size.toFloat() / allChecklist.size.toFloat()
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

            //attach
            Row(
                modifier = Modifier.constrainAs(attach) {
                    top.linkTo(add.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Attachments",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_bold, FontWeight.Bold)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(0.dp, 100.dp),
                    ){
                        items(allAttachment){ attachment ->
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .height(25.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val fileName = attachment.file_name
                                val arrName = fileName.split('-')
                                Text(
                                    text = arrName[2],
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(
                                        Font(R.font.open_sans_regular, FontWeight.Normal)
                                    ),
                                    modifier = Modifier.weight(2f)
                                )
                                val monthMap = listOf(
                                    "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
                                )
                                var tgl = ""
                                if (attachment.upload_time != "") {
                                    tgl = "${attachment.upload_time.substring( 8, 10)} ${ monthMap[attachment.upload_time.substring(5, 7).toInt() - 1]} ${attachment.upload_time.substring(0, 4)}"
                                }

                                Text(
                                    text = tgl,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(
                                        Font(R.font.open_sans_regular, FontWeight.Normal)
                                    ),
                                    modifier = Modifier.weight(1f)
                                )

                                Box(modifier = Modifier.weight(0.5f)){
                                    IconButton(
                                        onClick = {
                                            //download file
                                            var isSuccess = false
                                            runBlocking {
                                                withContext(Dispatchers.IO){
                                                    isSuccess = downloadFile(attachment.file_name)
                                                }
                                            }

                                            if(isSuccess){
                                                Toast.makeText(this@TaskDetailActivity, "Download success", Toast.LENGTH_SHORT).show()
                                            }else{
                                                Toast.makeText(this@TaskDetailActivity, "Download failed", Toast.LENGTH_SHORT).show()
                                            }
//                                            try{
//
//                                            }catch(e: Exception){
//                                                Log.d("Error Download", e.toString())
//                                            }
//                                            url.httpDownload().fileDestination{ _, _, _ ->
//                                                File(folder.path)
//                                            }.response{ _, _, result ->
//                                                when(result){
//                                                    result.Failure -> {
//                                                        val error = result.getException()
//                                                        println("Failed to download file: $error")
//                                                    }
//                                                    result.Success -> {
//                                                        println("File downloaded to $outputPath")
//                                                    }
//                                                }
//                                            }

//                                            url.httpDownload().fileDestination { response, request ->
//                                                File(filePath)
//                                            }.response()


                                        },
                                    ){
                                        Icon(
                                            imageVector = Icons.Default.FileDownload,
                                            contentDescription = "Download",
                                            modifier = Modifier
                                                .size(20.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //goal
            Row(
                modifier = Modifier.constrainAs(goal) {
                    top.linkTo(attach.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Goals",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_bold, FontWeight.Bold)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(0.dp, 120.dp),
                    ){
                        items(allChecklist){ checklist ->
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp, vertical = 4.dp)
                                    .height(25.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val status = if (checklist.is_checked == 1) "checked" else "unchecked"
                                Box(modifier = Modifier.weight(0.5f)){
                                    IconButton(
                                        onClick = {
                                            //update checklist
                                            scope.launch {
                                                vm.updateChecklistStatus(taskId, checklist.id.toString())
                                            }
                                        },
                                    ){
                                        if(status == "checked"){
                                            Icon(
                                                imageVector = Icons.Default.CheckBox,
                                                contentDescription = "checkbox",
                                                modifier = Modifier
                                                    .size(20.dp),
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.CheckBoxOutlineBlank,
                                                contentDescription = "checkbox",
                                                modifier = Modifier
                                                    .size(20.dp),
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = checklist.title,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(
                                        Font(R.font.open_sans_regular, FontWeight.Normal)
                                    ),
                                    modifier = Modifier.weight(2f),
                                    style = if(status == "checked") TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle()
                                )

                                Box(modifier = Modifier.weight(0.5f)){
                                    IconButton(
                                        onClick = {
                                            //delete checklist
                                            scope.launch {
                                                vm.deleteChecklist(taskId, checklist.id.toString())
                                            }
                                        },
                                    ){
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove",
                                            modifier = Modifier
                                                .size(18.dp),
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }

            //comment
            Row(
                modifier = Modifier.constrainAs(comment) {
                    top.linkTo(goal.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Comments",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_bold, FontWeight.Bold)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(0.dp, 250.dp),
                    ){
                        items(allComment) {comment ->
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp, vertical = 4.dp)
                                    .height(50.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box{
                                    if (comment.user_email.profile_picture == null){
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Profile Picture",
                                            modifier = Modifier
                                                .size(40.dp)
                                        )
                                    } else {
                                        //tampilkan profile..
                                        val pp = comment.user_email.profile_picture
                                        val url = env.prefixStorage + pp
                                        Image(
                                            painter = rememberImagePainter(
                                                data = url,
                                                builder = {
                                                    crossfade(true)
                                                    transformations(CircleCropTransformation())
                                                }
                                            ),
                                            contentDescription = "profile picture",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(40.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text(
                                            text = comment.user_email.name,
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(
                                                Font(R.font.open_sans_semi_bold, FontWeight.SemiBold)
                                            ),
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        var waktu = ""
                                        var tgl = ""
                                        val monthMap = listOf(
                                            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
                                        )
                                        if (comment.time != "") {
                                            tgl = comment.time.substring(0, 10)
                                            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                                Date()
                                            )

                                            if(tgl != currentDate){
                                                tgl = "${comment.time.substring( 8, 10)} ${ monthMap[comment.time.substring(5, 7).toInt() - 1]} ${comment.time.substring(0, 4)}"
                                            } else {
                                                tgl = ""
                                            }

                                            val jam = comment.time.substring(11, 13).toInt()
                                            val menit = comment.time.substring(14, 16)
                                            waktu = if (jam > 12) "${jam - 12}:$menit PM" else "$jam:$menit AM"
                                        }
                                        Text(
                                            text = "$tgl $waktu",
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily(
                                                Font(R.font.open_sans_regular, FontWeight.Normal)
                                            ),
                                            color = Color.Gray
                                        )
                                    }
                                    Text(
                                        text = comment.value,
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(
                                            Font(R.font.open_sans_regular, FontWeight.Normal)
                                        ),
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }

                    val value = remember { mutableStateOf("") }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        TextField(
                            value = value.value,
                            onValueChange = {value.value = it},
                            modifier = Modifier
                                .padding(horizontal = 8.dp),
                            label = { Text("Type Here") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done,
                            ),
                        )
                        IconButton(
                            onClick = {
                                val dto = AddCommentDTO(
                                    task_id = taskId,
                                    email = userEmail,
                                    value = value.value
                                )
                                Log.i("CommentDTO", dto.toString())
                                scope.launch {
                                    vm.addCommentTask(taskId, dto)
                                    delay(1000)
                                    val res = vm.addComment.value
                                    if (res != null){
                                        if (res.status == "201"){
                                            Toast.makeText(this@TaskDetailActivity, "Success add new Comment", Toast.LENGTH_SHORT).show()
                                            value.value = ""
                                        } else {
                                            Toast.makeText(this@TaskDetailActivity, res.message, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                        ){
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "send",
                                modifier = Modifier
                                    .fillMaxHeight(),
                                Color(0xFF0E9794),
                            )
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

    fun downloadFile(file_name: String): Boolean{
        val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "SETON")
        if (!folder.exists()) {
            folder.mkdirs()
        }

        val filePath = folder.path.toString() + "/" + file_name

        val url = env.prefixStorage + file_name

        val client = OkHttpClient()

        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use{ response ->
            if (!response.isSuccessful){
                return false
            }else{
                val inputStream: InputStream? = response.body?.byteStream()
                val outputFile = File(filePath)

                inputStream?.use { input ->
                    FileOutputStream(outputFile).use { output ->
                        input.copyTo(output)
                    }
                }
                return true
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
                                        val res = vm.checklist.value
                                        runOnUiThread{
                                            if(res != null){
                                                if(res.status == "201"){
                                                    Toast.makeText(context, "Success add new Checklist", Toast.LENGTH_SHORT).show()
                                                    onDismiss()
                                                } else {
                                                    Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                                                }
                                                goal = ""
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

    private @Composable
    fun ModalforAttachments(
        taskId : String,
        onDismiss:() -> Unit
    ) {
        val context = LocalContext.current
        val result = remember { mutableStateOf<List<Uri>?>(null)}
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenMultipleDocuments(),
            onResult = {
                result.value = it
            }
        )
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
                        text = "Add New Attachments",
                        modifier = Modifier.padding(4.dp),
                        fontSize = 20.sp
                    )

                    result.value?.forEach { uri ->
                        val fileName = getFileName(context, uri) ?: "Unknown file"
                        Text(
                            text = "File Name: $fileName",
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        onClick = {
                            launcher.launch(arrayOf("image/*", "application/pdf", "application/zip", "text/markdown"))
                        }
                    ) {
                        Text(text = "Pick file")
                    }

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
                                if (result.value != null) {
                                    for (uri in result.value!!) {
                                        val filePart = uriToMultipartBodyPart(context, uri, "file")
                                        scope.launch {
                                            vm.addAttachment(taskId, filePart)
                                            delay(1000)
                                            val res = vm.postAttachment.value
                                            withContext(Dispatchers.Main) {
                                                if (res != null) {
                                                    if (res.status == "201") {
                                                        Toast.makeText(context, "Success add new Attachment", Toast.LENGTH_SHORT).show()
                                                        onDismiss()
                                                    } else {
                                                        Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "Please pick a file", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD8FDFF)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Submit",
                                color = Color(0xFF0E9794)
                            )
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null
        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    fileName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        } else if (uri.scheme == ContentResolver.SCHEME_FILE) {
            fileName = uri.path?.let { path ->
                val cut = path.lastIndexOf('/')
                if (cut != -1) {
                    path.substring(cut + 1)
                } else null
            }
        }
        return fileName
    }

    fun uriToMultipartBodyPart(context: Context, uri: Uri, partName: String): MultipartBody.Part {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, getFileName(context, uri) ?: "tempFile")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        val requestBody = RequestBody.create(
            context.contentResolver.getType(uri)?.toMediaTypeOrNull(),
            file
        )
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }

    private fun String.toColor() = Color(parseColor(this))
}
