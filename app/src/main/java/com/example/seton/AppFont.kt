package com.example.seton

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

object AppFont {
    val fontBold = FontFamily(
        Font(R.font.open_sans_bold, FontWeight.Bold)
    )
    val fontLight = FontFamily(
        Font(R.font.open_sans_light, FontWeight.Light)
    )
    val fontNormal = FontFamily(
        Font(R.font.open_sans_regular, FontWeight.Normal)
    )
}