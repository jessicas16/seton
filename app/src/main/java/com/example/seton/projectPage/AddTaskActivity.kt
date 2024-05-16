package com.example.seton.projectPage

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.example.seton.R
import com.example.seton.component.CustomDateTimePicker
import com.example.seton.entity.ProjectDRO
import com.example.seton.entity.Projects
import com.example.seton.entity.Users
import com.example.seton.entity.addTaskDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AddTaskActivity : ComponentActivity() {
    private val vm: AddTaskViewModel by viewModels<AddTaskViewModel>()
    private lateinit var projectId : String
    private lateinit var scope: CoroutineScope
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectId = intent.getStringExtra("projectId").toString()
        setContent {
            scope = rememberCoroutineScope()
            AddNewTasks()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Preview(showBackground = true)
    @Composable
    fun AddNewTasks() {
        val context = LocalContext.current
        val invitedUser = mutableListOf<String>()
        val invitedUserTask by vm.invitedUsers.observeAsState(emptyList())
        LaunchedEffect(key1 = Unit) {
            vm.invitedUsers.value
        }

        val projectMember = vm.users.observeAsState(listOf())
        LaunchedEffect(key1 = Unit) {
            vm.getProjectMembers(projectId)
        }

        val project by vm.projects.observeAsState(ProjectDRO(
            status = "500",
            message = "an error occurred",
            data = Projects(id = 0,name = "",description = "",start = "",deadline = "",pm_email = "",status = -1)
        ))
        LaunchedEffect(key1 = Unit) {
            vm.getProjectById(projectId)
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            val (judul, projectName, judulTask, taskDescription, deadline, priority, setPic, addTaksMember, searchField, listUser, btnCreate) = createRefs()
            Text(
                text = "Create Tasks",
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

            Text(
                text = project.data.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(
                    Font(R.font.open_sans_regular)
                ),
                modifier = Modifier
                    .constrainAs(projectName) {
                        top.linkTo(judul.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    }
            )

            val taskTitle = remember { mutableStateOf("") }
            OutlinedTextField(
                value = taskTitle.value ,
                onValueChange = {
                    taskTitle.value = it
                },
                placeholder = {
                    Text(text = "Task Title")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(judulTask) {
                        top.linkTo(projectName.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            val taskDesc = remember { mutableStateOf("") }
            OutlinedTextField(
                value = taskDesc.value,
                onValueChange = {
                    taskDesc.value = it
                },
                placeholder = {
                    Text(text = "Task Description")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                ),
                maxLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .constrainAs(taskDescription) {
                        top.linkTo(judulTask.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            val taskDeadline = remember { mutableStateOf(LocalDateTime.now()) }
            CustomDateTimePicker(
                title = "Deadline",
                state = taskDeadline,
                modifier = Modifier
                    .constrainAs(deadline) {
                        top.linkTo(taskDescription.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            val prioritas = remember { mutableStateOf("Select Priority") }
            DropDownTextFieldPriority(
                selectedValue = prioritas.value,
                options = listOf("Low", "Medium", "High"),
                label = "Priority Level",
                onValueChangedEvent = {
                    prioritas.value = it
                },
                modifier = Modifier
                    .constrainAs(priority) {
                        top.linkTo(deadline.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
            )

            val pic = remember { mutableStateOf("Select PIC") }
            val pic_email = remember { mutableStateOf("") }
            DropDownPIC(
                selectedValue = pic.value,
                options = projectMember.value!!,
                label = "PIC",
                onValueChangedEvent = {
                    pic.value = it.name
                    pic_email.value = it.email
                },
                modifier = Modifier
                    .constrainAs(setPic) {
                        top.linkTo(priority.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
            )

            Text(
                text = "Add Task Member",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(
                    Font(R.font.open_sans_regular, FontWeight.Normal)
                ),
                modifier = Modifier
                    .constrainAs(addTaksMember) {
                        top.linkTo(setPic.bottom, margin = 16.dp)
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
                        top.linkTo(addTaksMember.bottom, margin = 4.dp)
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
                        Text(text = "Invite")
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
                        if(pic_email.value == email.value){
                            Toast.makeText(this@AddTaskActivity, "This email is already a PIC", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (email.value.isEmpty()) {
                            Toast.makeText(this@AddTaskActivity, "Email cannot be empty", Toast.LENGTH_SHORT).show()
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
                items(invitedUserTask) { user ->
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
                    if (taskTitle.value.isEmpty() || taskDesc.value.isEmpty() || prioritas.value == "Select Priority" || pic.value == "Select PIC") {
                        Toast.makeText(this@AddTaskActivity, "Input must not empty", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val dto = addTaskDTO(
                        title = taskTitle.value,
                        description = taskDesc.value,
                        deadline = taskDeadline.value.toString(),
                        taks_team = invitedUser,
                        priority = prioritas.value,
                        pic_email = pic_email.value,
                        project_id = projectId
                    )
                    Log.i("DTO", dto.toString())

                    scope.launch {
                        vm.createTask(dto)
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
                    text = "Create Task",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DropDownTextFieldPriority(
        selectedValue: String,
        options: List<String>,
        label: String,
        onValueChangedEvent: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedValue,
                onValueChange = {},
                label = { Text(text = label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option: String ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            expanded = false
                            onValueChangedEvent(option)
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DropDownPIC(
        selectedValue: String,
        options: List<Users>,
        label: String,
        onValueChangedEvent: (Users) -> Unit,
        modifier: Modifier = Modifier
    ) {
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedValue,
                onValueChange = {},
                label = { Text(text = label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option: Users ->
                    DropdownMenuItem(
                        text = { Text(text = option.name) },
                        onClick = {
                            expanded = false
                            onValueChangedEvent(option)
                        }
                    )
                }
            }
        }
    }
}

