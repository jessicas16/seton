package com.example.seton.landingPage

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seton.R

class LandingPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column{
//                Logo()
                Surface(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Jumbotron()
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
private fun Jumbotron(){
    Box(modifier = Modifier.fillMaxHeight()){
        Image(
            painter = painterResource(R.drawable.landing_jumbotron),
            contentDescription = "Jumbotron",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            val context = LocalContext.current
            Text(
                text = "Empower your modern team with powerful project management tools.",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W600,
                fontSize = 30.sp,
                color = Color(0xFF0E9794),
                modifier = Modifier.padding(bottom = 70.dp)
            )
            Text(
                text = "The systematic practice of planning, organizing, and controlling resources and activities to achieve project goals within a specified timeframe.",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 80.dp)
            )
            Button(
                onClick = {
                    //pindah ke landingpage2
                    val intent = Intent(context, LandingPage2Activity::class.java)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E9794)),
                modifier = Modifier.size(width = 170.dp, height = 50.dp)
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 18.sp
                )
            }
        }
    }
}