package com.example.seton.projectPage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout

class ListProjectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListProjectPreview()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListProjectPreview() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize()) {
            val data = listOf<String>("Budi", "Amir", "Yanto")
            items(data) {
                ListItem(it)
            }
        }
    }
}

@Composable
fun ListItem(data: String, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth()) {
        Text(text = data)
    }
}