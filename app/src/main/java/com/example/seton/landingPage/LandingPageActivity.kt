package com.example.seton.landingPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.seton.loginRegister.LoginActivity
import com.example.seton.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.seton.config.ApiConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LandingPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiConfiguration.getApiService(baseContext)
        installSplashScreen()
        setContent {
            Surface(
                modifier = Modifier.fillMaxHeight()
            ) {
                fragment()
            }
        }
    }
}

@Preview(showBackground = true)
@OptIn(ExperimentalPagerApi::class)
@Composable
private fun fragment(){
    val context = LocalContext.current
    val openAlertDialog = remember { mutableStateOf(false) }

    Log.d("RESPONSE LAGI", Greeting().toString())
    openAlertDialog.value = Greeting()

    ConstraintLayout(modifier = Modifier.fillMaxSize()){
        val (bg, pager, pageIdx, getStarted) = createRefs()

        Image(
            painter = painterResource(R.drawable.landing_jumbotron),
            contentDescription = "Jumbotron",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(bg){
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
        )

        val pagerState = rememberPagerState(0)
        HorizontalPager(
            state = pagerState,
            count = 4,
            modifier = Modifier.constrainAs(pager){
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(pageIdx.top)
                start.linkTo(parent.start)
            }
        ) { page ->
            when(page){
                0 -> Jumbotron()
                1 -> fitur1()
                2 -> fitur2()
                3 -> fitur3()
            }
        }

        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .constrainAs(pageIdx){
                    bottom.linkTo(getStarted.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ,
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }

        Button(
            onClick = {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E9794)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .constrainAs(getStarted){
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Text(
                text = "Get Started",
                fontSize = 20.sp
            )
        }

        if(openAlertDialog.value){
            AlertDialog(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Icon"
                    )
                },
                title = {Text(text = "Connection Error")},
                text = {Text(text = "Please check your internet connection and try again.")},
                onDismissRequest = {openAlertDialog.value = false},
                confirmButton = {
                    TextButton(
                        onClick = {openAlertDialog.value = false}
                    ) {
                        Text("Confirm")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Jumbotron(){
    Box(){
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .height(680.dp)
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
                modifier = Modifier.padding(top=150.dp, bottom = 100.dp)
            )
            Text(
                text = "The systematic practice of planning, organizing, and controlling resources and activities to achieve project goals within a specified timeframe.",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 130.dp)
            )
        }
    }
}

@Composable
private fun fitur1(){
    Box()
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .height(680.dp)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.fitur_web1),
                contentDescription = "Seton Logo",
                modifier = Modifier
                    .padding(top = 150.dp)
                    .scale(2f)
            )
            Column(
                modifier = Modifier
                    .padding(top = 125.dp, start= 20.dp, end = 20.dp, bottom = 70.dp)
            ) {
                Text(
                    text = "KANBAN BOARD",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0E9794),
                    modifier = Modifier.padding(5.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Unleash productivity with a Kanban board—visualize goals, streamline workflow, and achieve success effortlessly.",
                    fontSize = 17.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}

@Composable
private fun fitur2(){
    Box()
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .height(680.dp)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.fitur_web2),
                contentDescription = "Seton Logo",
                modifier = Modifier
                    .padding(top = 150.dp)
                    .scale(2f)
            )
            Column(
                modifier = Modifier
                    .padding(top = 125.dp, start= 20.dp, end = 20.dp, bottom = 70.dp)
            ) {
                Text(
                    text = "TIME TRACKING CALENDER",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0E9794),
                    modifier = Modifier.padding(5.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Seize control of your time and track deadlines with a time tracking calendar that maximizes productivity.",
                    fontSize = 17.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}

@Composable
private fun fitur3(){
    Box()
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .height(680.dp)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.fitur_web3),
                contentDescription = "Seton Logo",
                modifier = Modifier
                    .padding(top = 150.dp)
                    .scale(2f)
            )
            Column(
                modifier = Modifier
                    .padding(top = 125.dp, start= 20.dp, end = 20.dp, bottom = 70.dp)
            ) {
                Text(
                    text = "AI Generated Checklists",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0E9794),
                    modifier = Modifier.padding(5.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Streamline task management with one-click using auto-generated checklists, simplifying task organization.",
                    fontSize = 17.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}

fun Greeting():Boolean {
    var repo = ApiConfiguration.defaultRepo
    val ioScope = CoroutineScope(Dispatchers.IO)
    var returnMsg = true

    ioScope.launch(Dispatchers.IO) {
        try {
            val res = repo.checkConnection()
            Log.d("RESPONSE", res.toString())

            returnMsg = false
        }catch (e: Exception){
            Log.e("ERROR", e.message.toString())
        }
    }

    return returnMsg
}