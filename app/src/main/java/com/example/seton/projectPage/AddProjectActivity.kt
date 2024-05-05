package com.example.seton.projectPage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.seton.entity.Users

class AddProjectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            addNewProject()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun addNewProject() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        val (judul, etProjectName, etProjectDesc, addTeamMember, searchField, listTeam, btnCreate) = createRefs()
        Text(
            text = "Create Project",
            fontSize = 30.sp,
            fontWeight = FontWeight.W600,
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
                .height(150.dp)
                .constrainAs(etProjectDesc) {
                    top.linkTo(etProjectName.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = "Add Team Member",
            fontSize = 25.sp,
            fontWeight = FontWeight.W400,
            modifier = Modifier
                .constrainAs(addTeamMember) {
                    top.linkTo(etProjectDesc.bottom, margin = 40.dp)
                    start.linkTo(parent.start)
                }
        )

        val search = remember { mutableStateOf("") }
        OutlinedTextField(
            value = search.value,
            onValueChange = {
                search.value = it
            },
            placeholder = {
                Text(text = "Search")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(onClick = { /*NOTHING*/ }) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(searchField) {
                    top.linkTo(addTeamMember.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E9794)),
            modifier = Modifier
                .constrainAs(btnCreate) {
                    top.linkTo(searchField.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
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