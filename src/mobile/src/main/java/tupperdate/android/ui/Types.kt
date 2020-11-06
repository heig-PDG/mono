package tupperdate.android.ui

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.unit.sp
import tupperdate.android.R

private val Archivo = fontFamily(
    font(R.font.archivo_bold, FontWeight.Bold)
)

private val Mulish = fontFamily(
    font(R.font.mulish_bold, FontWeight.Bold),
    font(R.font.mulish_semibold, FontWeight.SemiBold),
    font(R.font.mulish_regular, FontWeight.Normal),
)

val TupperdateTypography = Typography(
    defaultFontFamily = Mulish,
    h1 = TextStyle(fontFamily = Archivo, fontSize = 96.sp, fontWeight = FontWeight.Bold),
    h2 = TextStyle(fontFamily = Archivo, fontSize = 60.sp, fontWeight = FontWeight.Bold),
    h3 = TextStyle(fontFamily = Archivo, fontSize = 48.sp, fontWeight = FontWeight.Bold),
    h4 = TextStyle(fontFamily = Archivo, fontSize = 34.sp, fontWeight = FontWeight.Bold),
    h5 = TextStyle(fontFamily = Archivo, fontSize = 24.sp, fontWeight = FontWeight.Bold),
    h6 = TextStyle(fontFamily = Archivo, fontSize = 20.sp, fontWeight = FontWeight.Bold),
    subtitle1 = TextStyle(fontFamily = Mulish, fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
    subtitle2 = TextStyle(fontFamily = Mulish, fontSize = 14.sp, fontWeight = FontWeight.Bold),
    body1 = TextStyle(fontFamily = Mulish, fontSize = 16.sp, fontWeight = FontWeight.Normal),
    body2 = TextStyle(fontFamily = Mulish, fontSize = 14.sp, fontWeight = FontWeight.Normal),
    button = TextStyle(fontFamily = Mulish, fontSize = 14.sp, fontWeight = FontWeight.Bold),
    caption = TextStyle(fontFamily = Mulish, fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
    overline = TextStyle(fontFamily = Mulish, fontSize = 10.sp, fontWeight = FontWeight.Bold),
)