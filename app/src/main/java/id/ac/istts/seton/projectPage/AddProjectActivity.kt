package id.ac.istts.seton.projectPage

//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import com.example.seton.SetUpNavGraph
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import id.ac.istts.seton.AppBar
import id.ac.istts.seton.DrawerBody
import id.ac.istts.seton.DrawerHeader
import id.ac.istts.seton.MenuItem
import id.ac.istts.seton.R
import id.ac.istts.seton.Screens
import id.ac.istts.seton.calendarPage.CalendarActivity
import id.ac.istts.seton.component.CustomDateTimePicker
import id.ac.istts.seton.entity.addProjectDTO
import id.ac.istts.seton.env
import id.ac.istts.seton.mainPage.DashboardActivity
import id.ac.istts.seton.reportPage.ReportActivity
import id.ac.istts.seton.settingPage.SettingActivity
import id.ac.istts.seton.taskPage.TaskActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AddProjectActivity : ComponentActivity() {
    private val vm: AddProjectViewModel by viewModels<AddProjectViewModel>()
    private lateinit var scope: CoroutineScope
    lateinit var userEmail : String
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userEmail = intent.getStringExtra("userEmail").toString()
        setContent {
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
            scope = rememberCoroutineScope()
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

                                        val intent = Intent(this@AddProjectActivity, SettingActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Dashboard.route -> {
                                        val intent = Intent(this@AddProjectActivity, DashboardActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Projects.route -> {
                                        val intent = Intent(this@AddProjectActivity, ListProjectActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Tasks.route -> {
                                        val intent = Intent(this@AddProjectActivity, TaskActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Calendar.route -> {
                                        val intent = Intent(this@AddProjectActivity, CalendarActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Report.route -> {
                                        val intent = Intent(this@AddProjectActivity, ReportActivity::class.java)
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
                            name = "Projects",
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                    }
                ) {
                    val hai = it
                    AddNewProject()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Preview(showBackground = true)
    @Composable
    fun AddNewProject() {
        val context = LocalContext.current
        val invitedUser = mutableListOf<String>()
        val invitedUserProjects by vm.invitedUsers.observeAsState(emptyList())
        LaunchedEffect(key1 = Unit) {
            vm.invitedUsers.value
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            val (judul, etProjectName, etProjectDesc, startTime, deadline, addTeamMember, searchField, listUser, btnCreate) = createRefs()
            Text(
                text = "Create Project",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(
                    Font(R.font.open_sans_bold, FontWeight.Bold)
                ),
                modifier = Modifier
                    .constrainAs(judul) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )

            val projectName = remember { mutableStateOf("") }
            OutlinedTextField(
                value = projectName.value ,
                onValueChange = {
                    projectName.value = it
                },
                placeholder = {
                    Text(text = "Project Name")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(etProjectName) {
                        top.linkTo(judul.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            val projectDesc = remember { mutableStateOf("") }
            OutlinedTextField(
                value = projectDesc.value,
                onValueChange = {
                    projectDesc.value = it
                },
                placeholder = {
                    Text(text = "Project Description")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                ),
                maxLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .constrainAs(etProjectDesc) {
                        top.linkTo(etProjectName.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            val startDateTimeState = remember { mutableStateOf(LocalDateTime.now()) }
            CustomDateTimePicker(
                title = "Start Time",
                state = startDateTimeState,
                modifier = Modifier
                    .constrainAs(startTime) {
                        top.linkTo(etProjectDesc.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            val deadlineState = remember { mutableStateOf(LocalDateTime.now()) }
            CustomDateTimePicker(
                title = "Deadline",
                state = deadlineState,
                modifier = Modifier
                    .constrainAs(deadline) {
                        top.linkTo(startTime.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Text(
                text = "Add Team Member",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(
                    Font(R.font.open_sans_bold, FontWeight.Bold)
                ),
                modifier = Modifier
                    .constrainAs(addTeamMember) {
                        top.linkTo(deadline.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                    }
            )

            val email = remember { mutableStateOf("") }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(searchField) {
                        top.linkTo(addTeamMember.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            )
            {
                OutlinedTextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                    },
                    placeholder = {
                        Text(text = "Email User")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if(userEmail == email.value){
                            Toast.makeText(this@AddProjectActivity, "You cannot invite yourself", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (email.value.isEmpty()) {
                            Toast.makeText(this@AddProjectActivity, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        scope.launch {
                            vm.checkEmailUser(email.value)
                            delay(1000)
                            val res = vm.checkEmail.value
                            Log.i("RES2", res.toString())
                            runOnUiThread{
                                if(res != null){
                                    if(res.status == "200"){
                                        invitedUser.add(email.value)
                                        Log.i("INVITED_USER", invitedUser.toString())
                                    }
                                    Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                                    email.value = ""
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E9794)),
                    modifier = Modifier
                        .weight(.4f)
                        .height(55.dp),
                    shape = RoundedCornerShape(30),
                ) {
                    Text(
                        text = "Invite",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_bold, FontWeight.Normal)
                        ),
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .constrainAs(listUser) {
                        top.linkTo(searchField.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = true,
            ) {
                items(invitedUserProjects) { user ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (user.profile_picture == null){
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .size(40.dp)
                                )
                            } else {
                                //tampilkan profile..
                                val pp = user.profile_picture
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
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = user.name)
                        }

                        Row {
                            IconButton(
                                onClick = {
                                    vm.removeUser(user.email)
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
            Button(
                onClick = {
                    if (projectName.value.isEmpty() || projectDesc.value.isEmpty()) {
                        Toast.makeText(this@AddProjectActivity, "Project Name and Description cannot be empty", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if(startDateTimeState.value.isAfter(deadlineState.value)){
                        Toast.makeText(this@AddProjectActivity, "Start Time cannot be after Deadline", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if(startDateTimeState.value == deadlineState.value){
                        Toast.makeText(this@AddProjectActivity, "Start Time cannot be same as Deadline", Toast.LENGTH_SHORT).show()
                        return@Button
                    }


                    val dto = addProjectDTO(
                        name = projectName.value,
                        description = projectDesc.value,
                        startTime = startDateTimeState.value.toString(),
                        deadline = deadlineState.value.toString(),
                        members_email = invitedUser,
                        pm_email = userEmail
                    )

                    Log.i("DTO", dto.toString())
                    scope.launch {
                        vm.addNewProject(dto)
                        delay(1000)
                        val res = vm.response.value
                        Log.i("RES", res.toString())
                        runOnUiThread {
                            if (res != null) {
                                Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                                if (res.status == "201") {
                                    finish()
                                }
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E9794)),
                modifier = Modifier
                    .constrainAs(btnCreate) {
                        top.linkTo(listUser.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxWidth(),
                shape = RoundedCornerShape(30),
            ) {
                Text(
                    text = "Create Project",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}