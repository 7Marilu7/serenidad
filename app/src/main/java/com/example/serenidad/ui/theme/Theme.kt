package com.example.serenidad.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🎨 Colores pastel personalizados
val PastelLavender = Color(0xFFE6E6FA) // lavanda claro
val PastelPink = Color(0xFFFFC1CC)     // rosa suave
val PastelMint = Color(0xFFB8EBD0)     // menta
val PastelPeach = Color(0xFFFFE5B4)    // durazno claro
val PastelBlue = Color(0xFFB5D8EB)     // azul cielo suave
val PastelLilac = Color(0xFFDCC6E0)    // lila
val TextGray = Color(0xFF3E3E3E)       // texto neutro

// 🌿 Esquema de colores pastel
val PastelColorScheme = lightColorScheme(
    primary = PastelBlue,
    onPrimary = Color.White,
    primaryContainer = PastelLavender,
    onPrimaryContainer = TextGray,
    secondary = PastelPink,
    onSecondary = Color.White,
    tertiary = PastelMint,
    onTertiary = TextGray,
    background = Color(0xFFFFF8F5),
    surface = Color(0xFFFFF2EE),
    onSurface = TextGray,
    error = Color(0xFFD32F2F)
)

// 💅 Tema principal
@Composable
fun SerenidadTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PastelColorScheme,
        typography = Typography(),
        content = content
    )
}
