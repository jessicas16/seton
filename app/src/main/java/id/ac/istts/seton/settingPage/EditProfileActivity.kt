package id.ac.istts.seton.settingPage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import id.ac.istts.seton.AppFont
import id.ac.istts.seton.R
import id.ac.istts.seton.env
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class EditProfileActivity : ComponentActivity() {
    val vm: SettingViewModel by viewModels<SettingViewModel>()
    lateinit var scope: CoroutineScope
    lateinit var userEmail : String
    lateinit var userName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userEmail = intent.getStringExtra("userEmail").toString()
        userName = intent.getStringExtra("userName").toString()
        setContent {
            scope = rememberCoroutineScope()
            EditProfilePreview()
        }
    }

    fun uriToMultipartBodyPart(context: Context, uri: Uri, partName: String): MultipartBody.Part {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, getFileName(context, uri) ?: "tempFile")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        val requestBody = RequestBody.create(
            context.contentResolver.getType(uri)?.toMediaTypeOrNull(),
            file
        )
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null
        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    fileName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        } else if (uri.scheme == ContentResolver.SCHEME_FILE) {
            fileName = uri.path?.let { path ->
                val cut = path.lastIndexOf('/')
                if (cut != -1) {
                    path.substring(cut + 1)
                } else null
            }
        }
        return fileName
    }

    @Preview(showBackground = true)
    @Composable
    fun EditProfilePreview() {
        val user by vm.user.observeAsState(null)
        val scrollState = rememberScrollState()
        val context = LocalContext.current
        LaunchedEffect(key1 = Unit) {
            vm.getUser(userEmail)
        }
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
                        text = "Edit Profile",
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
                    .zIndex(0f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Picture
                var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

                val imagePickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = { uri: Uri? ->
                        selectedImageUri = uri
                    }
                )

                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clickable { imagePickerLauncher.launch("image/*") }
                ) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberImagePainter(
                                data = selectedImageUri,
                                builder = {
                                    crossfade(true)
                                    transformations(CircleCropTransformation())
                                }
                            ),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(72.dp)
                        )
                    } else if (user?.profile_picture.toString() == "null") {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(72.dp)
                        )
                    } else {
                        val url = env.prefixStorage + user?.profile_picture
                        Image(
                            painter = rememberImagePainter(
                                data = url,
                                builder = {
                                    crossfade(true)
                                    transformations(CircleCropTransformation())
                                }
                            ),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(72.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Black, shape = CircleShape)
                            .align(Alignment.BottomEnd)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile Picture",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Email
                val email = remember { mutableStateOf(userEmail) }
                Text(
                    modifier = Modifier.fillMaxWidth().padding(start = 4.dp, bottom = 2.dp, top = 16.dp),
                    text = "Email",
                    fontFamily = AppFont.fontNormal,
                    textAlign = TextAlign.Start
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    value = email.value,
                    onValueChange = {
                        email.value = it
                    },
                    enabled = false
                )

                // Name
                val name = remember { mutableStateOf(userName) }
                Text(
                    modifier = Modifier.fillMaxWidth().padding(start = 4.dp, bottom = 2.dp),
                    text = "Name",
                    fontFamily = AppFont.fontNormal,
                    textAlign = TextAlign.Start
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    value = name.value,
                    onValueChange = {
                        name.value = it
                    }
                )

                // Save
                Button(
                    onClick = {
                        if (name.value.isEmpty()) {
                            Toast.makeText(context, "Please your name", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        scope.launch {
                            val filePart = if (selectedImageUri != null) uriToMultipartBodyPart(context, selectedImageUri!!, "file") else null
                            val response = vm.updateProfile(userEmail, name.value, filePart)
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
                        text = "Save",
                        fontSize = 18.sp,
                        fontFamily = AppFont.fontNormal,
                        color = Color.White
                    )
                }
            }
        }
    }
}