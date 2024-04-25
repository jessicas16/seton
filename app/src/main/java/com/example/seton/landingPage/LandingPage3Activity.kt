package com.example.seton.landingPage

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seton.R

class LandingPage3Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxHeight()
            ) {
                ListFitur()
            }
        }
    }
}

@Composable
private fun Logo(){
    Image(
        painter = painterResource(R.drawable.seton_logo),
        contentDescription = "Seton Logo",
        modifier = Modifier
            .padding(20.dp)
    )
}

@Composable
private fun ListFitur() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "What you’ll find",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W600,
            fontSize = 28.sp,
            color = Color(0xFF0E9794),
            modifier = Modifier.padding(
                top = 60.dp,
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp
            )
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            FeatureCard(
                title = "Kanban Board",
                description = "Unleash productivity with a Kanban board—visualize goals, streamline workflow, and achieve success effortlessly."
            )
            FeatureCard(
                title = "Time Tracking Calender",
                description = "Seize control of your time and track deadlines with a time tracking calendar that maximizes productivity."
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            FeatureCard(
                title = "AI Generated Checklists",
                description = "Streamline task management with one-click using auto-generated checklists, simplifying task organization."
            )
            FeatureCard(
                title = "Mobile Friendly",
                description = "Access our user-friendly project management tools software over the net, anywhere, anytime, even on mobile."
            )
        }

        NextButton()
    }
}

@Composable
private fun FeatureCard(
    title: String,
    description: String
) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(210.dp)
            .padding(10.dp)
            .background(
                Color(0xFFD8FDFF),
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(5.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
private fun NextButton(){
    val context = LocalContext.current
    Button(
        onClick = {
            //pindah ke login page
//            val intent = Intent(context, MainActivity::class.java)
//            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E9794)),
        modifier = Modifier
            .size(width = 150.dp, height = 80.dp)
            .padding(top = 30.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Next",
                fontSize = 18.sp
            )
            Icon(
                painter = painterResource(R.drawable.baseline_arrow_right_alt_24),
                contentDescription = "Next Button",
                Modifier
                    .size(40.dp),
                Color.White
            )
        }
    }
}