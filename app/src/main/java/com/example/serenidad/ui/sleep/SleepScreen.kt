package com.example.serenidad.ui.sleep

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // <-- IMPORTANTE
import androidx.compose.foundation.shape.RoundedCornerShape
// --- Importaciones de iconos ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.History // <-- NUEVO ICONO
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NoFood
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Smartphone
// -----------------------------
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import java.text.DecimalFormat

// --- Importaciones de ViewModel y Corutinas ---
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serenidad.data.viewmodel.AuthViewModel
import com.example.serenidad.data.viewmodel.SleepViewModel
import kotlinx.coroutines.launch
// --- NUEVAS IMPORTACIONES ---
import com.example.serenidad.data.model.SleepEntity
import kotlinx.coroutines.flow.emptyFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
// ---------------------------


private val GrayMedium = Color(0xFF562B8D)
private val GrayDark = Color(0xFF424242)
private val LavenderAccent = Color(0xFF896ADA)
private val LavenderAccentLight = Color(0xFFEFE6FF)
private val GlassBackground = Color(0x30FFFFFF)
private val GlassBorder = Color(0x50FFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepScreen(
    title: String = "Registro de Sueño",
    sleepViewModel: SleepViewModel = viewModel(),
    authViewModel: AuthViewModel
) {

    // --- Estados para guardar el registro de sueño ---
    var hoursSlept by remember { mutableStateOf(7.5f) }
    var selectedQuality by remember { mutableStateOf<String?>(null) }
    var sleepNote by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentUser by authViewModel.currentUser.collectAsState()
    val currentUserId = currentUser?.uid


    // --- 4. OBTENCIÓN DE DATOS ANTERIORES ---
    // 'remember' recalcula este Flow solo si 'currentUserId' cambia
    val sleepRecordsFlow = remember(currentUserId) {
        currentUserId?.let { userId ->
            sleepViewModel.getSleepRecords(userId)
        } ?: emptyFlow<List<SleepEntity>>() // <-- 1. CORRECCIÓN AQUÍ
    }
    // 'collectAsState' convierte el Flow en una lista que la UI puede usar
    val sleepRecords by sleepRecordsFlow.collectAsState(initial = emptyList<SleepEntity>()) // <-- 2. CORRECCIÓN AQUÍ
    // --------------------------------------
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
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { scaffoldPadding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GlassBackground)
                    .border(
                        1.dp,
                        GlassBorder,
                        RoundedCornerShape(24.dp)
                    )
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
                        "Registra tus horas y calidad de sueño para ver patrones.",
                        color = GrayMedium,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                }

                // --- Categoría 1: Registra tu Noche ---
                item {
                    CategoryHeader(
                        title = "Registra tu Noche",
                        icon = Icons.Default.Bedtime
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // Card para el registro
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = LavenderAccentLight.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            // ... (Slider, Chips, OutlinedTextField... todo se queda igual)

                            // --- Selector de Horas (Slider) ---
                            Text("Horas de sueño", fontWeight = FontWeight.SemiBold, color = GrayDark)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Slider(
                                    value = hoursSlept,
                                    onValueChange = { hoursSlept = it },
                                    valueRange = 0f..12f,
                                    steps = 23, // 12 horas * 2 (medios pasos) - 1
                                    modifier = Modifier.weight(1f),
                                    colors = SliderDefaults.colors(
                                        thumbColor = LavenderAccent,
                                        activeTrackColor = LavenderAccent,
                                        inactiveTrackColor = LavenderAccentLight
                                    )
                                )
                                // Formateador para mostrar "7.0" o "7.5"
                                val df = DecimalFormat("#.#")
                                Text(
                                    "${df.format(hoursSlept)}h",
                                    fontWeight = FontWeight.Bold,
                                    color = GrayMedium,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                            Spacer(Modifier.height(16.dp))

                            // --- Selector de Calidad (Chips) ---
                            Text("Calidad del sueño", fontWeight = FontWeight.SemiBold, color = GrayDark)
                            Spacer(Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                val qualities = listOf("Mala", "Regular", "Buena")
                                qualities.forEach { quality ->
                                    FilterChip(
                                        selected = selectedQuality == quality,
                                        onClick = { selectedQuality = quality },
                                        label = { Text(quality, fontWeight = FontWeight.Medium) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = LavenderAccent,
                                            selectedLabelColor = Color.White,
                                            containerColor = LavenderAccentLight,
                                            labelColor = GrayDark
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                }
                            }
                            Spacer(Modifier.height(20.dp))

                            // --- Diario de Sueño (Nota) ---
                            OutlinedTextField(
                                value = sleepNote,
                                onValueChange = { sleepNote = it },
                                label = { Text("Diario de sueño (opcional)", color = GrayMedium) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = GrayDark,
                                    unfocusedTextColor = GrayDark,
                                    focusedBorderColor = LavenderAccent,
                                    unfocusedBorderColor = GrayMedium,
                                    focusedLabelColor = LavenderAccent,
                                    unfocusedLabelColor = GrayMedium,
                                    cursorColor = LavenderAccent
                                )
                            )
                            Spacer(Modifier.height(20.dp))


                            // --- Botón de Guardar (Sin cambios) ---
                            Button(
                                onClick = {
                                    val userId = currentUserId
                                    if (userId != null && selectedQuality != null) {
                                        sleepViewModel.addSleepRecord(
                                            hours = hoursSlept,
                                            quality = selectedQuality!!,
                                            note = sleepNote.ifBlank { null },
                                            userId = userId
                                        )
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Registro de sueño guardado")
                                        }
                                        selectedQuality = null
                                        sleepNote = ""
                                        hoursSlept = 7.5f
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Error: Faltan datos o no se pudo identificar al usuario.")
                                        }
                                    }
                                },
                                enabled = selectedQuality != null && currentUserId != null,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LavenderAccentLight,
                                    contentColor = GrayDark,
                                    disabledContainerColor = Color.White.copy(alpha = 0.5f)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Guardar Registro", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }

                // --- 5. NUEVA CATEGORÍA: Registros Anteriores ---
                item {
                    CategoryHeader(
                        title = "Registros Anteriores",
                        icon = Icons.Default.History // Icono de historial
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // Si no hay registros, muestra un mensaje
                if (sleepRecords.isEmpty()) {
                    item {
                        Text(
                            "Aún no tienes registros de sueño.",
                            color = GrayMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                } else {
                    // Si hay registros, muéstralos en una lista
                    items(sleepRecords) { record ->
                        SleepRecordItem(record = record)
                        Spacer(Modifier.height(8.dp)) // Espacio entre cada registro
                    }
                }

                item {
                    Spacer(Modifier.height(24.dp)) // Espacio antes de la siguiente categoría
                }
                // --- FIN DE LA NUEVA CATEGORÍA ---


                // --- Categoría 2: Higiene del Sueño (Se mantiene igual) ---
                item {
                    CategoryHeader(
                        title = "Higiene del Sueño",
                        icon = Icons.Default.Info
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    InfoCard(
                        title = "Mantén un Horario Regular",
                        description = "Intenta acostarte y despertarte a la misma hora todos los días, incluso los fines de semana.",
                        icon = Icons.Default.Schedule
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    InfoCard(
                        title = "Crea un Ambiente Relajante",
                        description = "Tu habitación debe ser oscura, silenciosa y fresca. Considera usar cortinas opacas o un antifaz.",
                        icon = Icons.Default.DarkMode
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    InfoCard(
                        title = "Evita las Pantallas",
                        description = "La luz azul de teléfonos y TVs interfiere con la melatonina. Deja de usarlos al menos 1 hora antes de dormir.",
                        icon = Icons.Default.Smartphone
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    InfoCard(
                        title = "Limita Cafeína y Comidas",
                        description = "Evita comidas pesadas, cafeína y alcohol cerca de la hora de dormir, ya que pueden interrumpir tu ciclo de sueño.",
                        icon = Icons.Default.NoFood
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}


/**
 * Composable para la cabecera de cada categoría.
 * (Sin cambios)
 */
@Composable
fun CategoryHeader(title: String, icon: ImageVector) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = GrayMedium,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = GrayDark
            )
        }
        Spacer(Modifier.height(4.dp))
        Divider(color = LavenderAccentLight, thickness = 1.dp)
    }
}


//Composable reutilizable para tips de información.

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
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = GrayMedium,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
            )
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = GrayDark,
                    fontSize = 17.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = description,
                    color = GrayMedium,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

// --- 2. NUEVA FUNCIÓN HELPER (Formateador de fecha) ---
/**
 * Convierte un timestamp Long (milisegundos) a una fecha legible.
 * Ej: "14 Nov, 2025"
 */
private fun formatTimestamp(timestamp: Long): String {
    // Usamos Locale("es", "ES") para que ponga "Nov" en vez de "Nov" (ej. "Abr" vs "Apr")
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale("es", "ES"))
    return sdf.format(Date(timestamp))
}

// --- 3. NUEVO COMPOSABLE (Tarjeta para cada registro) ---
/**
 * Un Composable que muestra una fila de un registro de sueño anterior.
 */
@Composable
fun SleepRecordItem(record: SleepEntity) {
    // Asigna un color basado en la calidad
    val qualityColor = when (record.quality) {
        "Buena" -> Color(0xFF4CAF50) // Verde
        "Regular" -> Color(0xFFFFA000) // Ámbar
        "Mala" -> Color(0xFFD32F2F) // Rojo
        else -> GrayMedium
    }
    // Formateador para las horas
    val df = DecimalFormat("#.#")

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.7f) // Un poco más opaco para legibilidad
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna para Fecha y Nota
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = formatTimestamp(record.date), // Ej: "14 Nov, 2025"
                    fontWeight = FontWeight.Bold,
                    color = GrayDark,
                    fontSize = 16.sp
                )
                // Muestra la nota solo si existe
                record.note?.let {
                    if (it.isNotBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = it,
                            color = GrayMedium,
                            fontSize = 14.sp,
                            maxLines = 1 // Solo muestra la primera línea
                        )
                    }
                }
            }

            // Horas
            Text(
                text = "${df.format(record.hours)}h",
                fontWeight = FontWeight.Bold,
                color = GrayDark,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Calidad (Texto con color)
            Text(
                text = record.quality,
                fontWeight = FontWeight.Bold,
                color = qualityColor,
                fontSize = 16.sp
            )
        }
    }
}