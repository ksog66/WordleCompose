package com.example.wordle_compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val keyIdleBackgroundLight = Color(0XFFD3D6DA)
val keyIdleBackgroundDark = Color(0XFF818384)
val keyMissingBg = Color(0XFF3A3A3C)
val keyWrongPlaceBg = Color(0XFFB59F3B)
val keyCorrectPlaceBg = Color(0XFF538D4E)

val keyFontColorLight = Color.White
val keyFontColorDark = Color.Black

val colorTone1: Color get() = Color(if (isSystemInDarkTheme) 0xFFD7DADC else 0xFF1A1A1B)
val colorTone7: Color get() = Color(if (isSystemInDarkTheme) 0xFF121213 else 0xFFFFFFFF)