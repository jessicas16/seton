package com.example.seton.loginRegister

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seton.R
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.seton.calendarPage.CalendarActivity
import com.example.seton.entity.userLoginDTO
import com.example.seton.mainPage.DashboardActivity
import com.example.seton.projectPage.ListProjectActivity
import com.example.seton.taskPage.TaskActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    val vm: loginRegisterViewModel by viewModels<loginRegisterViewModel>()
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                LoginPage()
            }
        }
    }

    @Preview
    @Composable
    fun LoginPage() {
        val context = LocalContext.current
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (bg, et, etEmail, etPassword, forgotPass, btnLogin, btnSignUp, textOr, btnLoginGoogle) = createRefs()
            Image(
                painter = painterResource(R.drawable.login_page),
                contentDescription = "login page",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(bg) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )

            Column(modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxSize()
                .constrainAs(et){
                    end.linkTo(parent.end, margin = 46.dp)
                    start.linkTo(parent.start, margin = 46.dp)
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ConstraintLayout {
                    val email = remember { mutableStateOf("ivan.s21@mhs.istts.ac.id") }
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
                            imeAction = ImeAction.Done,
                        ),
                        modifier = Modifier
                            .width(282.dp)
                            .constrainAs(etEmail) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )
                    val password = remember { mutableStateOf("123") }
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
                                    contentDescription = "",
                                    modifier = Modifier.scale(0.7f)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = if(passwordVisibility.value) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        modifier = Modifier
                            .width(282.dp)
                            .constrainAs(etPassword) {
                                top.linkTo(etEmail.bottom, margin = 8.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                    Row(
                        modifier = Modifier
                            .constrainAs(forgotPass) {
                                top.linkTo(etPassword.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .width(282.dp),
                        horizontalArrangement = Arrangement.End
                    ){
                        Row(
                            modifier = Modifier.padding(start = 30.dp)
                        ) {
                            Text(
                                text = "Forgot Password?",
                                modifier = Modifier.padding(top = 4.dp),
                                color = Color(0xFF0E9794),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (email.value.isEmpty() || password.value.isEmpty()) {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val user = userLoginDTO(
                                email = email.value,
                                password = password.value,
                            )
                            ioScope.launch {
                                vm.loginUser(user)
                                val res = vm.response.value
                                runOnUiThread{
                                    if(res != null){
                                        if(res.status == "200"){
                                            val intent = Intent(context, DashboardActivity::class.java)
                                            intent.putExtra("userEmail", email.value)
                                            context.startActivity(intent)
                                        } else {
                                            Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .constrainAs(btnLogin) {
                                top.linkTo(forgotPass.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .width(282.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E9794)),
                        shape = RoundedCornerShape(size = 4.dp)
                    ) {
                        Text(
                            text = "Log In",
                            fontSize = 18.sp,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .constrainAs(btnSignUp) {
                                top.linkTo(btnLogin.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .width(282.dp),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = "Don't have an account?",
                        )
                        Text(
                            text = "Sign Up",
                            color = Color(0xFF0E9794),
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .clickable {
                                    val intent = Intent(context, RegisterActivity::class.java)
                                    context.startActivity(intent)

                                },
                            textDecoration = TextDecoration.Underline,
                        )
                    }
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .width(282.dp)
                            .fillMaxWidth()
                            .constrainAs(textOr) {
                                top.linkTo(btnSignUp.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
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
                        onClick = {

                        },
                        modifier = Modifier
                            .constrainAs(btnLoginGoogle) {
                                top.linkTo(textOr.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
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
    }
}

