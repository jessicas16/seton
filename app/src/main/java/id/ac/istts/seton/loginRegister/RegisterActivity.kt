package id.ac.istts.seton.loginRegister

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import id.ac.istts.seton.R
import id.ac.istts.seton.entity.userDTO
import id.ac.istts.seton.mainPage.DashboardActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {
    val vm:loginRegisterViewModel by viewModels<loginRegisterViewModel>()
    lateinit var scope: CoroutineScope
    companion object {
        private const val RC_SIGN_IN = 9001
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            auth = FirebaseAuth.getInstance()
            scope = rememberCoroutineScope()
            Surface {
                RegisterPage()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun RegisterPage(){
        val name = remember { mutableStateOf("aa") }
        val email = remember { mutableStateOf("aa") }
        val confirm_password = remember { mutableStateOf("aa") }
        val password = remember { mutableStateOf("aa") }
        Surface {
            Register(
                name = name,
                email = email,
                password = password,
                confirm_password = confirm_password
            )
        }
    }

    @Composable
    fun Register(
        name: MutableState<String>,
        email: MutableState<String>,
        password: MutableState<String>,
        confirm_password: MutableState<String>
    ) {
        val context = LocalContext.current
        ConstraintLayout(modifier = Modifier.fillMaxSize()){
            val (bg, et, etEmail, etName, etPassword, etConfPass, btnRegister, btnSignIn, textOr, btnLoginGoogle) = createRefs()

            Image(
                painter = painterResource(R.drawable.login_page) ,
                contentDescription = "Register page",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(bg) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )

            Column(modifier = Modifier
                .padding(top = 250.dp)
                .constrainAs(et) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ConstraintLayout {
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
                            .width(282.dp)
                            .constrainAs(etName) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

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
                            .width(282.dp)
                            .constrainAs(etEmail) {
                                top.linkTo(etName.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

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
                                top.linkTo(etEmail.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

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
                                    contentDescription = "",
                                    modifier = Modifier.scale(0.7f)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        visualTransformation = if(confirm_passwordVisibility.value) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        modifier = Modifier
                            .width(282.dp)
                            .constrainAs(etConfPass) {
                                top.linkTo(etPassword.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                    Button(
                        onClick = {
                            //check if fields are empty
                            if (name.value.isEmpty() || email.value.isEmpty() || password.value.isEmpty() || confirm_password.value.isEmpty()) {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            //check if password and confirm password match
                            if (password.value != confirm_password.value) {
                                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val user = userDTO(
                                name = name.value,
                                email = email.value,
                                password = password.value,
                            )
                            scope.launch {
                                vm.registerUser(user)
                                Thread.sleep(750)
                                val res = vm.response.value
                                runOnUiThread{
                                    if(res != null){
                                        if(res.status == "201"){
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
                            .constrainAs(btnRegister) {
                                top.linkTo(etConfPass.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .width(282.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E9794)),
                        shape = RoundedCornerShape(size = 4.dp)
                    ) {
                        Text(
                            text = "Register",
                            fontSize = 18.sp
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .constrainAs(btnSignIn) {
                                top.linkTo(btnRegister.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .fillMaxWidth()
                    ){
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
                            .width(282.dp)
                            .fillMaxWidth()
                            .constrainAs(textOr) {
                                top.linkTo(btnSignIn.bottom, margin = 16.dp)
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
                        onClick = { signIn() },
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
                                text = "Register With Google",
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

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    //masuk ke db
                    scope.launch {
                        val userLogin = authUser(
                            email = user?.email.toString(),
                            name = user?.displayName.toString()
                        )
                        vm.registerUserWithGoogle(userLogin)
                        Thread.sleep(750)
                        val res = vm.response.value
                        runOnUiThread{
                            if(res != null){
                                if(res.status == "201"){
                                    Toast.makeText(this@RegisterActivity, "Register success", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@RegisterActivity, DashboardActivity::class.java)
                                    intent.putExtra("userEmail", user?.email)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this@RegisterActivity, res.message, Toast.LENGTH_SHORT).show()
                                    if(auth.currentUser != null){
                                        auth.signOut()
                                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                            .requestIdToken(getString(R.string.default_web_client_id))
                                            .requestEmail()
                                            .build()
                                        val googleSignInClient = GoogleSignIn.getClient(this@RegisterActivity, gso)
                                        googleSignInClient.signOut()
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}