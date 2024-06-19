package id.ac.istts.seton.settingPage

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LockReset
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import id.ac.istts.seton.AppFont
import id.ac.istts.seton.projectPage.AddProjectActivity

class SettingActivity : ComponentActivity() {
    lateinit var userEmail : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userEmail = intent.getStringExtra("userEmail").toString()
        setContent {
            SettingPreview()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun SettingPreview() {
        val context = LocalContext.current
        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp, 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Profile
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (true){
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(36.dp)
                                .weight(1f)
                        )
                    } else {
                        //tampilkan profile..
                    }
                    Column (
                        Modifier
                            .weight(4f)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "Ivan Susanto",
                            fontFamily = AppFont.fontBold,
                            fontSize = 20.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "ivan.s21@mhs.istts.ac.id",
                            fontFamily = AppFont.fontNormal,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        modifier = Modifier
                            .size(24.dp)
                            .weight(1f)
                    )
                }

                Row(
                    Modifier.padding(top = 16.dp)
                ) {
                    Text(
                        text = "Account",
                        fontFamily = AppFont.fontSemiBold,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Change Password
                Row(
                    Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        contentDescription = "Change Password",
                        imageVector = Icons.Rounded.LockReset,
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(
                        text = "Change Password",
                        fontFamily = AppFont.fontSemiBold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Right",
                        modifier = Modifier
                            .size(14.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )

                // Logout
                Row(
                    Modifier.padding(horizontal = 8.dp, vertical = 8.dp).clickable {
                        // Logout
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        contentDescription = "Logout",
                        imageVector = Icons.AutoMirrored.Rounded.Logout,
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(
                        text = "Logout",
                        fontFamily = AppFont.fontSemiBold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Right",
                        modifier = Modifier
                            .size(14.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )

                Row(
                    Modifier.padding(top = 16.dp)
                ) {
                    Text(
                        text = "Others",
                        fontFamily = AppFont.fontSemiBold,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // About Us
                Row(
                    Modifier.padding(horizontal = 8.dp, vertical = 8.dp).clickable {
                        val intent = Intent(context, AboutActivity::class.java)
                        context.startActivity(intent)
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        contentDescription = "About",
                        imageVector = Icons.Rounded.Info,
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(
                        text = "About Us",
                        fontFamily = AppFont.fontSemiBold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Right",
                        modifier = Modifier
                            .size(14.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )

                // Version
                Row(
                    Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        contentDescription = "Version",
                        imageVector = Icons.Rounded.PhoneAndroid,
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(
                        text = "Version",
                        fontFamily = AppFont.fontSemiBold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                    )
                    Text(
                        text = "v 1.0.0",
                        fontFamily = AppFont.fontSemiBold,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.alpha(0.5f)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )
            }
        }
    }
}

