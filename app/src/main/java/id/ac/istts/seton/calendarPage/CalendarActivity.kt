package id.ac.istts.seton.calendarPage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import id.ac.istts.seton.AppBar
import id.ac.istts.seton.AppFont
import id.ac.istts.seton.CalendarFont
import id.ac.istts.seton.DrawerBody
import id.ac.istts.seton.DrawerHeader
import id.ac.istts.seton.MenuItem
import id.ac.istts.seton.R
import id.ac.istts.seton.Screens
import id.ac.istts.seton.loginRegister.LoginActivity
import id.ac.istts.seton.mainPage.DashboardActivity
import id.ac.istts.seton.projectPage.ListProjectActivity
import id.ac.istts.seton.reportPage.ReportActivity
import id.ac.istts.seton.taskPage.TaskActivity
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class CalendarActivity : ComponentActivity() {
    private val vm: CalendarViewModel by viewModels<CalendarViewModel>()
    lateinit var userEmail : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userEmail = intent.getStringExtra("userEmail").toString()
        setContent {
            val items = listOf(
                MenuItem(
                    title = "Dashboard",
                    route = Screens.Dashboard.route,
                    selectedIcon = Icons.Filled.Dashboard,
                    unSelectedIcon = Icons.Outlined.Dashboard
                ),
                MenuItem(
                    title = "Projects",
                    route = Screens.Projects.route,
                    selectedIcon = Icons.Filled.ListAlt,
                    unSelectedIcon = Icons.Outlined.ListAlt
                ),
                MenuItem(
                    title = "Tasks",
                    route = Screens.Tasks.route,
                    selectedIcon = Icons.Filled.Task,
                    unSelectedIcon = Icons.Outlined.Task
                ),
                MenuItem(
                    title = "Calendar",
                    route = Screens.Calendar.route,
                    selectedIcon = Icons.Filled.CalendarToday,
                    unSelectedIcon = Icons.Outlined.CalendarToday
                ),
                MenuItem(
                    title = "Report",
                    route = Screens.Report.route,
                    selectedIcon = Icons.Filled.Report,
                    unSelectedIcon = Icons.Outlined.Report
                ),
                MenuItem(
                    title = "Settings",
                    route = Screens.Settings.route,
                    selectedIcon = Icons.Filled.Settings,
                    unSelectedIcon = Icons.Outlined.Settings
                ),
            )

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
//            val navController = rememberNavController()
//            val navBackStackEntry by navController.currentBackStackEntryAsState()
//            val currentRoute = navBackStackEntry?.destination?.route

//            val topBarTitle =
//                if (currentRoute != null){
//                    items[items.indexOfFirst {
//                        it.route == currentRoute
//                    }].title
//                }
//                else {
//                    items[0].title
//                }

            ModalNavigationDrawer(
                gesturesEnabled = drawerState.isOpen,
                drawerContent = {
                    ModalDrawerSheet {
                        DrawerHeader()
                        Spacer(modifier = Modifier.height(8.dp))
                        DrawerBody(
                            items = items,
                            onItemClick = { currentMenuItem ->
                                when (currentMenuItem.route){
                                    Screens.Settings.route -> {
//                                        val ioScope = CoroutineScope(Dispatchers.Main)
//                                        ioScope.launch {
//                                            ApiConfiguration.defaultRepo.logoutUser()
//                                        }
//
//                                        if(mAuth.currentUser != null){
//                                            mAuth.signOut()
//                                            mGoogleSignInClient.signOut()
//                                        }

                                        val intent = Intent(this@CalendarActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Dashboard.route -> {
                                        val intent = Intent(this@CalendarActivity, DashboardActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Projects.route -> {
                                        val intent = Intent(this@CalendarActivity, ListProjectActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Tasks.route -> {
                                        val intent = Intent(this@CalendarActivity, TaskActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Calendar.route -> {
                                        val intent = Intent(this@CalendarActivity, CalendarActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Screens.Report.route -> {
                                        val intent = Intent(this@CalendarActivity, ReportActivity::class.java)
                                        intent.putExtra("userEmail", userEmail)
                                        startActivity(intent)
                                        finish()
                                    }

                                }
                            }
                        )
                    }
                }, drawerState = drawerState
            ){
                Scaffold(
                    topBar = {
                        AppBar (
                            name = "Calendar",
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                    }
                ) {
                    val hai = it
                    CalendarPreview()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun CalendarPreview() {
        val listCalendar by vm.listCalendar.observeAsState(emptyList())
        val calendar by vm.cal.observeAsState(Calendar.getInstance(TimeZone.getTimeZone("GMT+7")))
        val selected by vm.selected.observeAsState(Calendar.getInstance(TimeZone.getTimeZone("GMT+7")))

        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F2F2))
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(Modifier.padding(bottom = 16.dp)) {
                    Image(
                        painter = painterResource(R.drawable.prev_icon),
                        contentDescription = "previous",
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                vm.updateCalendar(-1)
                            }
                    )
                    Text(
                        text = "${
                            calendar.getDisplayName(
                                Calendar.MONTH,
                                Calendar.LONG,
                                Locale.ENGLISH
                            )
                        } ${calendar.get(Calendar.YEAR)}",
                        fontFamily = CalendarFont.fontBold,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Image(
                        painter = painterResource(R.drawable.next_icon),
                        contentDescription = "next",
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                vm.updateCalendar(1)
                            }
                    )
                }
                Row(Modifier.fillMaxWidth()) {
                    for (cal in listCalendar) {
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row {
                                Text(
                                    text = cal.day,
                                    fontFamily = CalendarFont.fontBold,
                                    fontSize = 10.sp,
                                    color = Color(0xFF626262),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            for (date in cal.date) {
                                Row {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(45.dp)
                                            .padding(4.dp)
                                            .background(
                                                if (
                                                    date.second &&
                                                    calendar
                                                        .get(Calendar.MONTH)
                                                        .toString() == selected
                                                        .get(Calendar.MONTH)
                                                        .toString() &&
                                                    calendar
                                                        .get(Calendar.YEAR)
                                                        .toString() == selected
                                                        .get(Calendar.YEAR)
                                                        .toString() &&
                                                    date.first == selected
                                                        .get(Calendar.DATE)
                                                        .toString()
                                                ) Color(0xFF0E9794)
                                                else if (cal.day == "SAT" || cal.day == "SUN") Color(
                                                    0xFFD8FDFF
                                                )
                                                else Color.White,
                                                RoundedCornerShape(6.dp)
                                            )
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                if (date.second) vm.changeSelected(
                                                    date.first.toInt(),
                                                    true
                                                )
                                                else vm.changeSelected(date.first.toInt(), false)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column {
                                            Row(
                                                Modifier
                                                    .weight(1f)
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = date.first,
                                                    fontFamily = CalendarFont.fontBold,
                                                    fontSize = 12.sp,
                                                    color = Color.Black,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .align(Alignment.Bottom)
                                                        .alpha(
                                                            if (!date.second) 0.5f
                                                            else 1f
                                                        )
                                                )
                                            }
                                            Row(
                                                Modifier
                                                    .weight(1f)
                                                    .fillMaxWidth()
                                                    .padding(top = 3.dp),
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                for (i in 0..<cal.tasks.size.coerceAtMost(2)) {
                                                    if (cal.tasks[i].deadline.split("T")[0].split("-")[2] == date.first) {
                                                        Spacer(modifier = Modifier.padding(1.dp))
                                                        Box(
                                                            modifier = Modifier
                                                                .size(6.dp)
                                                                .background(
                                                                    Color(
                                                                        when (cal.tasks[i].status) {
                                                                            0 -> 0xFFFFDD60
                                                                            1 -> 0xFFF4976C
                                                                            2 -> 0xFF87CBCA
                                                                            3 -> 0xFFFACBB6
                                                                            else -> 0xFF2DA4A2
                                                                        }
                                                                    ).copy(
                                                                        if (!date.second) 0.5f
                                                                        else 1f
                                                                    ),
                                                                    RoundedCornerShape(6.dp)
                                                                )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.padding(2.dp))
                            }
                        }
                        if (cal.day != "SUN") {
                            Spacer(modifier = Modifier.padding(2.dp))
                        }
                    }
                }
                Row(
                    Modifier
                        .background(Color(0xFFE5E5E5), RoundedCornerShape(12.dp))
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Column (Modifier.padding(16.dp)) {
                        for (cal in listCalendar) {
                            if (cal.tasks.isNotEmpty()) {
                                val arrDeadline = cal.tasks[0].deadline.split("T")[0].split("-")
                                if (
                                    arrDeadline[2].toInt().toString() == selected.get(Calendar.DATE).toString() &&
                                    arrDeadline[1].toInt().toString() == (selected.get(Calendar.MONTH) + 1).toString() &&
                                    arrDeadline[0].toInt().toString() == selected.get(Calendar.YEAR).toString()
                                ) {
                                    Text(
                                        text = "${selected.get(Calendar.DATE)} ${
                                            calendar.getDisplayName(
                                                Calendar.MONTH,
                                                Calendar.LONG,
                                                Locale.ENGLISH
                                            )
                                        } ${selected.get(Calendar.YEAR)}",
                                        fontFamily = CalendarFont.fontMedium,
                                        fontSize = 22.sp,
                                        color = Color.Black,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                    LazyColumn(
                                        Modifier
                                            .fillMaxSize()
                                            .padding(vertical = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(cal.tasks){ task ->
                                            Column(
                                                Modifier
                                                    .background(Color.White, RoundedCornerShape(8.dp))
                                                    .padding(top = 16.dp)
                                                    .fillMaxWidth()
                                            ) {
                                                Row(
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = task.title,
                                                        fontFamily = AppFont.fontBold,
                                                        fontSize = 16.sp,
                                                        color = Color.Black,
                                                        modifier = Modifier.padding(end = 6.dp)
                                                    )
                                                    Text(
                                                        text = "in ${task.project.name}",
                                                        fontFamily = AppFont.fontBold,
                                                        fontSize = 10.sp,
                                                        color = Color.Black
                                                    )
                                                }
                                                Row(
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 16.dp)
                                                ) {
                                                    Text(
                                                        text = task.description,
                                                        fontFamily = AppFont.fontNormal,
                                                        fontSize = 12.sp,
                                                        color = Color.Black,
                                                        modifier = Modifier.padding(bottom = 12.dp)
                                                    )
                                                }
                                                Row(
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .height(8.dp)
                                                        .background(Color(
                                                            when (task.status) {
                                                                0 -> 0xFFFFDD60
                                                                1 -> 0xFFF4976C
                                                                2 -> 0xFF87CBCA
                                                                3 -> 0xFFFACBB6
                                                                else -> 0xFF2DA4A2
                                                            }
                                                        ), RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp))
                                                ) {}
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}