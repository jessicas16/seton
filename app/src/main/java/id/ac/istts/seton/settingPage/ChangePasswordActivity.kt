package id.ac.istts.seton.settingPage

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import id.ac.istts.seton.AppFont
import id.ac.istts.seton.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ChangePasswordActivity : ComponentActivity() {
    val vm: SettingViewModel by viewModels<SettingViewModel>()
    lateinit var scope: CoroutineScope
    lateinit var userEmail : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userEmail = intent.getStringExtra("userEmail").toString()
        setContent {
            scope = rememberCoroutineScope()
            ChangePasswordPreview()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ChangePasswordPreview() {
        val scrollState = rememberScrollState()
        val context = LocalContext.current
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(5.dp)
                    .background(MaterialTheme.colors.surface)
                    .zIndex(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        contentDescription = "Back",
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(start = 16.dp)
                            .clickable {
                                (context as Activity).finish()
                            }
                    )
                    Text(
                        text = "Change Password",
                        fontFamily = AppFont.fontSemiBold,
                        fontSize = 20.sp,
                        maxLines = 1,
                        color = Color(0xFF0E9794),
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 20.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(top = 68.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .zIndex(0f)
            ) {
                // Old Password
                val opassword = remember { mutableStateOf("") }
                val opasswordVisibility = remember { mutableStateOf(false) }
                val oicon = if (opasswordVisibility.value)
                    painterResource(id = R.drawable.eye_close_up_63568)
                else
                    painterResource(id = R.drawable.visible_7042918)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    value = opassword.value,
                    onValueChange = {
                        opassword.value = it
                    },
                    placeholder = {
                        Text(
                            text = "Old Password",
                            fontFamily = AppFont.fontNormal
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            opasswordVisibility.value = !opasswordVisibility.value
                        }) {
                            Icon(
                                painter = oicon,
                                contentDescription = "",
                                modifier = Modifier.scale(0.7f)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if(opasswordVisibility.value) VisualTransformation.None
                    else PasswordVisualTransformation(),
                )

                // New Password
                val npassword = remember { mutableStateOf("") }
                val npasswordVisibility = remember { mutableStateOf(false) }
                val nicon = if (npasswordVisibility.value)
                    painterResource(id = R.drawable.eye_close_up_63568)
                else
                    painterResource(id = R.drawable.visible_7042918)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    value = npassword.value,
                    onValueChange = {
                        npassword.value = it
                    },
                    placeholder = {
                        Text(
                            text = "New Password",
                            fontFamily = AppFont.fontNormal
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            npasswordVisibility.value = !npasswordVisibility.value
                        }) {
                            Icon(
                                painter = nicon,
                                contentDescription = "",
                                modifier = Modifier.scale(0.7f)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if(npasswordVisibility.value) VisualTransformation.None
                    else PasswordVisualTransformation(),
                )

                // Confirm New Password
                val cpassword = remember { mutableStateOf("") }
                val cpasswordVisibility = remember { mutableStateOf(false) }
                val cicon = if (cpasswordVisibility.value)
                    painterResource(id = R.drawable.eye_close_up_63568)
                else
                    painterResource(id = R.drawable.visible_7042918)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    value = cpassword.value,
                    onValueChange = {
                        cpassword.value = it
                    },
                    placeholder = {
                        Text(
                            text = "Confirm New Password",
                            fontFamily = AppFont.fontNormal
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            cpasswordVisibility.value = !cpasswordVisibility.value
                        }) {
                            Icon(
                                painter = cicon,
                                contentDescription = "",
                                modifier = Modifier.scale(0.7f)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if(cpasswordVisibility.value) VisualTransformation.None
                    else PasswordVisualTransformation(),
                )

                // Change Passowrd
                Button(
                    onClick = {
                        if (opassword.value.isEmpty() || npassword.value.isEmpty() || cpassword.value.isEmpty()) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        } else if (npassword.value != cpassword.value) {
                            Toast.makeText(context, "Confirm New Passwords do not match", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        scope.launch {
                            val response = vm.updatePassword(userEmail, opassword.value, npassword.value)
                            runOnUiThread{
                                Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E9794)),
                    shape = RoundedCornerShape(size = 4.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 4.dp),
                        text = "Change Password",
                        fontSize = 18.sp,
                        fontFamily = AppFont.fontNormal,
                        color = Color.White
                    )
                }
            }
        }
    }
}