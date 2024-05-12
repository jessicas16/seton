package com.example.seton.projectPage

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.seton.R
import com.example.seton.entity.ProjectDetailDRO
import com.example.seton.entity.Users


class ProjectDetailsActivity : ComponentActivity() {
    private val vm: ProjectDetailsViewModel by viewModels<ProjectDetailsViewModel>()
    private lateinit var projectId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectId = intent.getStringExtra("projectId").toString()
        setContent {
            ProjectDetail()
        }
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
            val (projectName, detailsOrBoard, projectOverview, Stats, RecentChanges) = createRefs()

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
                Box(
                    modifier = Modifier
                        .padding(0.dp, 10.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(10.dp))
                        .shadow(10.dp, MaterialTheme.shapes.medium)
                        .background(Color.White)
                        .constrainAs(projectOverview) {
                            top.linkTo(detailsOrBoard.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
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

                    }
                }

                Box(
                    modifier = Modifier
                        .padding(0.dp, 10.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(10.dp))
                        .shadow(10.dp, MaterialTheme.shapes.medium)
                        .background(Color.White)
                        .constrainAs(Stats) {
                            top.linkTo(projectOverview.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
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

    override fun onResume() {
        super.onResume()
        vm.getProjectById(projectId)
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

            val color = if (text2 == "Ongoing") Color(0xFFFFDD60) else if (text2 == "Completed") Color(0xFF0E9794) else Color.Black
            Text(
                text = text2,
                fontSize = 14.sp,
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

}
