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
    val fontSemiBold = FontFamily(
        Font(R.font.open_sans_semi_bold, FontWeight.SemiBold)
    )
    val fontExtraBold = FontFamily(
        Font(R.font.open_sans_extra_bold, FontWeight.ExtraBold)
    )
    val fontMedium = FontFamily(
        Font(R.font.open_sans_medium, FontWeight.Medium)
    )
}