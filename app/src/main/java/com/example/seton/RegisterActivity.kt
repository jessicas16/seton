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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                Register()
            }
        }
    }
}

@Composable
fun Register() {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(R.drawable.login_page) ,
            contentDescription = "Register page",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier
            .padding(top = 250.dp)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val name = remember { mutableStateOf("") }
            //name
            OutlinedTextField(
                value = name.value ,
                onValueChange = {
                    name.value = it
                },
                placeholder = {
                    Text(text = "Display Name")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .width(282.dp)
            )

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
                    .padding(bottom = 16.dp)
            )
            val confirm_password = remember { mutableStateOf("") }
            val confirm_passwordVisibility = remember { mutableStateOf(false) }

            val iconConfirm = if (confirm_passwordVisibility.value)
                painterResource(id = R.drawable.eye_close_up_63568)
            else
                painterResource(id = R.drawable.visible_7042918)

            OutlinedTextField(
                value = confirm_password.value,
                onValueChange = {
                    confirm_password.value = it
                },
                placeholder = {
                    Text(text = "Confirm Password")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        confirm_passwordVisibility.value = !confirm_passwordVisibility.value
                    }) {
                        Icon(
                            painter = iconConfirm,
                            contentDescription = ""
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = if(confirm_passwordVisibility.value) VisualTransformation.None
                else PasswordVisualTransformation(),
                modifier = Modifier
                    .width(282.dp)
            )

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(282.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E9794)),
                shape = RoundedCornerShape(size = 4.dp)
            ) {
                Text(
                    text = "Register",
                    fontSize = 18.sp
                )
            }
            Row{
                Text(
                    text = "Already have an account?",
                )
                Text(
                    text = "Log In",
                    color = Color(0xFF0E9794),
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable {
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
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