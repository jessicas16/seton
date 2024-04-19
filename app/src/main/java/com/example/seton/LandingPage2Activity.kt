package com.example.seton

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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

class LandingPage2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column{
                Logo()
                Manage()
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NextButton()
                }
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
private fun Manage(){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Manage your business with Seton",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W600,
            fontSize = 28.sp,
            color = Color(0xFF0E9794),
            modifier = Modifier.padding(
                top = 30.dp,
                start=20.dp,
                end=20.dp,
            )
        )

        Column(
            modifier = Modifier
                .padding(20.dp, 10.dp)
        ) {
            boxFitur(1, "Increase productivity", "Unlock your full productivity potential by embracing a dynamic to-do list approach that empowers you to effortlessly track deadlines and conquer your tasks with confidence.")
            boxFitur(2, "Connect with others", "Ignite seamless collaboration and unleash creativity with user-friendly project management tools that simplify communication, enabling effortless teamwork and idea exchange.")
            boxFitur(3, "Plan and track projects", "Harness the power of insightful project management tools to make smart decisions, strategically plan, and stay on track, ensuring successful goal achievement.")
        }

    }
}

@Composable
private fun boxFitur(noFitur : Int, text: String, penjelasan: String){
    Box(
        modifier = Modifier
            .padding(top = 10.dp)
            .background(Color(0xFFD8FDFF),
        shape = RoundedCornerShape(10.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (
                modifier = Modifier
                    .padding(10.dp)
            ) {
                if (noFitur == 1) {
                    var iconPainter = painterResource(R.drawable.fitur1)
                    Icon(
                        painter = iconPainter,
                        contentDescription = "Feature 1",
                        Modifier.size(25.dp) ,
                        Color(0xFF0E9794)
                    )
                } else if (noFitur == 2) {
                    var iconPainter = painterResource(R.drawable.fitur2)
                    Icon(
                        painter = iconPainter,
                        contentDescription = "Feature 2",
                        Modifier.size(25.dp),
                        Color(0xFF0E9794)
                    )
                } else if (noFitur == 3) {
                    var iconPainter = painterResource(R.drawable.fitur3)
                    Icon(
                        painter = iconPainter,
                        contentDescription = "Feature 3",
                        Modifier.size(25.dp),
                        Color(0xFF0E9794)
                    )
                }
            }
            Column {
                Text(
                    text = text,
                    fontSize = 18.sp,
                    color = Color(0xFF0E9794),
                    modifier = Modifier.padding(10.dp),
                    fontWeight = FontWeight.W600
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(0.dp, 35.dp, 0.dp, 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = penjelasan,
                fontSize = 15.sp,
                modifier = Modifier.padding(10.dp),
            )
        }
    }
}

@Composable
private fun NextButton(){
    val context = LocalContext.current
    Button(
        onClick = {
            //pindah ke landingpage3
            val intent = Intent(context, LandingPage3Activity::class.java)
            context.startActivity(intent)
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