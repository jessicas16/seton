package com.example.seton

sealed class Screens(val route: String) {
    object Dashboard : Screens("dashboard")
    object Projects : Screens("projects")
    object Tasks : Screens("tasks")
    object Calendar : Screens("calendar")
    object Report : Screens("report")
    object Logout : Screens("logout")

}