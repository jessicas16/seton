package id.ac.istts.seton.projectPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.binayshaw7777.kotstep.ui.vertical.VerticalIconStepper
import id.ac.istts.seton.R
import id.ac.istts.seton.entity.ProjectDetailDRO
import id.ac.istts.seton.entity.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProjectDetailsActivity : ComponentActivity() {
    private val vm: ProjectDetailsViewModel by viewModels<ProjectDetailsViewModel>()
    private lateinit var scope: CoroutineScope
    private lateinit var projectId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectId = intent.getStringExtra("projectId").toString()
        setContent {
            scope = rememberCoroutineScope()
            ProjectDetail()
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
                Text(
                    text = "Boards",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(
                        Font(R.font.open_sans_bold, FontWeight.Bold)
                    ),
                    modifier = Modifier
                        .constrainAs(projectOverview) {
                            top.linkTo(detailsOrBoard.bottom, margin = 10.dp)
                            start.linkTo(parent.start)
                        }
                )

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
}
