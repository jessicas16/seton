package com.example.seton.projectPage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.seton.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ListProjectActivity : ComponentActivity() {
    val vm: ListProjectViewModel by viewModels<ListProjectViewModel>()
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
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
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(8.dp, 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val data = listOf("Budi", "Amir", "Yanto")
            items(data) {
                ExpandableCard(it, description = "Ini Deskripsi")
            }
        }
    }
    FloatingButton(
        onClick = {

        }
    )
}

@Composable
fun FloatingButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp, 32.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Button(
            onClick = onClick,
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
fun ExpandableCard(
    title: String,
    description: String,
    padding: Dp = 12.dp
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f, label = ""
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(6f),
                    text = title,
                    fontFamily = FontFamily(
                        Font(R.font.open_sans_bold, FontWeight.Bold)
                    ),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(0.2f)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (expandedState) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Description",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_light, FontWeight.Light)
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(0.6f)) {
                    Box(
                        modifier = Modifier
                            .background(color = Color(0xFFECFFFF), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "31 July 2024",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(
                                Font(R.font.open_sans_regular, FontWeight.Normal)
                            ),
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF0E9794)
                        )
                    }
                }

                Text(
                    text = "Josh Bariston",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(
                        Font(R.font.open_sans_regular, FontWeight.Normal)
                    ),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Ongoing",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(
                        Font(R.font.open_sans_bold, FontWeight.Bold)
                    ),
                    modifier = Modifier.padding(end = 16.dp),
                    color = if (!expandedState) Color(0xFFF4976C) else Color.Transparent
                )
            }
            if (expandedState) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Text(
                        text = "Progress",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_regular, FontWeight.Normal)
                        ),
                        modifier = Modifier.weight(0.4f)
                    )
                    Text(
                        text = "13/50",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_bold, FontWeight.Bold)
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Ongoing",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(
                            Font(R.font.open_sans_bold, FontWeight.Bold)
                        ),
                        modifier = Modifier.padding(end = 16.dp),
                        color = Color(0xFFF4976C)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ){
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        progress = { 0.7f },
                        color = Color(0xFF0E9794),
                        trackColor = Color(0xFFECFFFF)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ){
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
                            text = "JH",
                            fontSize = 20.sp,
                            fontFamily = FontFamily(
                                Font(R.font.open_sans_bold, FontWeight.Bold)
                            ),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0E9794)
                        )
                    }
                }
            }
        }
    }
}