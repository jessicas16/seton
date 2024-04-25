package com.example.seton

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seton.landingPage.LandingPage2Activity

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                Login()
            }
        }
    }
}

@Composable
fun Login() {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.login_page),
            contentDescription = "login page",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier
            .padding(top = 280.dp)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val email = remember { mutableStateOf("") }
            //email
            OutlinedTextField(
                value = email.value,
                onValueChange = {
                    email.value = it
                },
                leadingIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = "Email Icon"
                        )
                    }
                },
                placeholder = {
                    Text(text = "Email")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .width(282.dp)
            )
            val password = remember { mutableStateOf("") }
            val passwordVisibility = remember { mutableStateOf(false) }

            val icon = if (passwordVisibility.value)
                painterResource(id = R.drawable.eye_close_up_63568)
            else
                painterResource(id = R.drawable.visible_7042918)

            //password
            OutlinedTextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                placeholder = {
                    Text(text = "Password")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {
                        Icon(
                            painter = icon,
                            contentDescription = ""
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = if(passwordVisibility.value) VisualTransformation.None
                else PasswordVisualTransformation(),
                modifier = Modifier

                    .width(282.dp)
            )

            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .width(282.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row(
                    modifier = Modifier
                        .padding(0.dp)
                ) {
                    val checkedState = remember { mutableStateOf(false) }
                    Checkbox(
                        checked = checkedState.value,
                        onCheckedChange = { checkedState.value = it },
                        modifier = Modifier
                            .padding(0.dp),
                        colors = CheckboxDefaults.colors(Color(0xFF0E9794))
                    )
                    Text(
                        text ="Remember Me",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                Row(
                    modifier = Modifier.padding(start = 30.dp)
                ) {
                    Text(
                        text = "Forgot Password?",
                        modifier = Modifier.padding(top = 16.dp),
                        color = Color(0xFF0E9794),
                        fontSize = 12.sp
                    )
                }
            }

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(282.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E9794)),
                shape = RoundedCornerShape(size = 4.dp)
            ) {
                Text(
                    text = "Log In",
                    fontSize = 18.sp,
                )
            }
            Row{
                Text(
                    text = "Don't have an account?",
                )
                Text(
                    text = "Sign Up",
                    color = Color(0xFF0E9794),
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable {
//                            val intent = Intent(context, RegisterActivity::class.java)
//                            context.startActivity(intent)
                        },
                    textDecoration = TextDecoration.Underline,
                )
            }
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .width(282.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(1.dp)
                        .background(color = Color.LightGray)
                )

                Text(
                    text = "Or Continue with",
                    Modifier.padding(horizontal = 20.dp)
                )

                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(1.dp)
                        .background(color = Color.LightGray)
                )
            }

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(282.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD8FDFF)),
                shape = RoundedCornerShape(size = 4.dp)
            ) {
                Row {
                    Image(
                        painter = painterResource(R.drawable.icon_google),
                        contentDescription = "Google Icon",
                    )
                    Text(
                        text = "Log In With Google",
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            }
        }
    }
}