package com.example.serenidad.ui.help

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
// --- Importaciones de iconos ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QuestionAnswer
// -----------------------------
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Colores del tema ---
private val GrayMedium = Color(0xFF562B8D)
private val GrayDark = Color(0xFF424242)
private val LavenderAccentLight = Color(0xFFEFE6FF)
private val GlassBackground = Color(0x30FFFFFF)
private val GlassBorder = Color(0x50FFFFFF)
private val HelpColor = Color(0xFF0277BD) // Un azul para las líneas de ayuda

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(title: String = "Recursos de Ayuda") {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5E1FF),
                        Color(0xFFFFE6FA),
                        Color(0xFFE3F2FD)
                    )
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent
        ) { scaffoldPadding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GlassBackground)
                    .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
                    .padding(16.dp),
            ) {

                // Título principal
                item {
                    Text(
                        text = title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = GrayDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "No estás solo/a. Si necesitas ayuda inmediata, estas líneas están disponibles para ti.",
                        color = GrayMedium,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                }

                // --- Categoría 1: Líneas de Ayuda Inmediata ---
                item {
                    CategoryHeader(
                        title = "Líneas de Ayuda Inmediata (México)",
                        icon = Icons.Default.Phone
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    HelpLineCard(
                        title = "Línea de la Vida",
                        phone = "800 911 2000",
                        description = "Apoyo gratuito y confidencial para crisis emocionales, salud mental y prevención del suicidio. 24/7."
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    HelpLineCard(
                        title = "SAPTEL",
                        phone = "55 5259 8121",
                        description = "Sistema Nacional de Apoyo y Consejo Psicológico por Teléfono. 24/7."
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    HelpLineCard(
                        title = "Consejo Ciudadano",
                        phone = "55 5533 5533",
                        description = "Línea de Seguridad y Chat de Confianza. Apoyo psicológico y legal. 24/7."
                    )
                    Spacer(Modifier.height(24.dp))
                }

                // --- Categoría 2: Información Útil ---
                item {
                    CategoryHeader(
                        title = "Información Útil",
                        icon = Icons.Default.Info
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    InfoCard(
                        title = "¿Qué es la Ansiedad?",
                        description = "La ansiedad es una respuesta natural al estrés. Se vuelve un problema cuando es excesiva, difícil de controlar e interfiere con tu vida diaria. Incluye preocupación, miedo intenso y síntomas físicos.",
                        icon = Icons.Default.HelpOutline
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    InfoCard(
                        title = "¿Qué es la TCC?",
                        description = "La Terapia Cognitivo-Conductual (TCC) es un enfoque que te ayuda a identificar y cambiar patrones de pensamiento y comportamiento negativos. Se centra en cómo tus pensamientos, sentimientos y acciones están conectados.",
                        icon = Icons.Default.Psychology
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}


/**
 * Tarjeta especial para las líneas de ayuda, con un color distintivo.
 */
@Composable
fun HelpLineCard(title: String, phone: String, description: String) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LavenderAccentLight.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = GrayDark,
                fontSize = 17.sp
            )
            Spacer(Modifier.height(4.dp))
            // Teléfono destacado
            Text(
                text = phone,
                fontWeight = FontWeight.ExtraBold,
                color = HelpColor, // Color azul
                fontSize = 20.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = description,
                color = GrayMedium,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

// --- Composables Reutilizados (Copiados de pantallas anteriores) ---

@Composable
fun CategoryHeader(title: String, icon: ImageVector) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Icon(icon, contentDescription = title, tint = GrayMedium, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(8.dp))
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = GrayDark)
        }
        Spacer(Modifier.height(4.dp))
        Divider(color = LavenderAccentLight, thickness = 1.dp)
    }
}

@Composable
fun InfoCard(
    title: String,
    description: String,
    icon: ImageVector
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LavenderAccentLight.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = title, tint = GrayMedium, modifier = Modifier.size(40.dp).padding(end = 12.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, color = GrayDark, fontSize = 17.sp)
                Spacer(Modifier.height(4.dp))
                Text(description, color = GrayMedium, fontSize = 14.sp, lineHeight = 20.sp)
            }
        }
    }
}