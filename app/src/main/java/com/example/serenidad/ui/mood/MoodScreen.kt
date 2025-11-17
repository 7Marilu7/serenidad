@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.serenidad.ui.mood

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serenidad.data.model.MoodEntity
import com.example.serenidad.data.viewmodel.MoodViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import androidx.compose.foundation.border
import androidx.compose.ui.text.style.TextAlign
import com.example.serenidad.data.viewmodel.AuthViewModel


private val GrayLight = Color(0xFFF0F0F0)
private val GrayMedium = Color(0xFF562B8D)
private val GrayDark = Color(0xFF424242)
private val LavenderAccent = Color(0xFFB398FF)
private val LavenderAccentLight = Color(0xFFEFE6FF)
private val GlassBackground = Color(0x30FFFFFF)
private val GlassBorder = Color(0x50FFFFFF)
private val Lavender = Color(0xFF694AA2)

private val EMOTIONS = listOf(
    "Feliz 😊",
    "Tranquilo 😌",
    "Cansado 😴",
    "Ansioso 😰",
    "Triste 😢",
    "Enojado 😠",
    "Motivado 💪",
    "Aburrido 😐",
    "Orgulloso 🦁",
    "Agradecido 🙏",
    "Estresado 😵",
    "Relajado 🌿",
    "Creativo 🎨",
    "Enamorado ❤️"
)

/* Helpers para fecha / emoji*/
private fun epochToYMD(epoch: Long): Triple<Int, Int, Int> {
    val c = Calendar.getInstance().apply { timeInMillis = epoch }
    return Triple(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH))
}

private fun todayYMD(): Triple<Int, Int, Int> {
    val c = Calendar.getInstance()
    return Triple(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH))
}

private fun daysInMonth(year: Int, month: Int): Int {
    val c = Calendar.getInstance()
    c.set(Calendar.YEAR, year)
    c.set(Calendar.MONTH, month - 1)
    return c.getActualMaximum(Calendar.DAY_OF_MONTH)
}

private fun firstDayIndexMondayBased(year: Int, month: Int): Int {
    val c = Calendar.getInstance()
    c.set(Calendar.YEAR, year)
    c.set(Calendar.MONTH, month - 1)
    c.set(Calendar.DAY_OF_MONTH, 1)
    val dow = c.get(Calendar.DAY_OF_WEEK) // SUN=1
    return when (dow) {
        Calendar.MONDAY -> 0
        Calendar.TUESDAY -> 1
        Calendar.WEDNESDAY -> 2
        Calendar.THURSDAY -> 3
        Calendar.FRIDAY -> 4
        Calendar.SATURDAY -> 5
        Calendar.SUNDAY -> 6
        else -> 0
    }
}

private fun monthNameSpanish(month1to12: Int): String {
    val cal = Calendar.getInstance()
    cal.set(Calendar.MONTH, (month1to12 - 1).coerceIn(0, 11))
    return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("es"))?.replaceFirstChar { it.uppercase() } ?: ""
}

private fun splitEmotionLabel(label: String): Pair<String, String> {
    val idx = label.lastIndexOf(' ')
    return if (idx >= 0 && idx < label.length - 1) {
        val text = label.substring(0, idx).trim()
        val emoji = label.substring(idx + 1).trim()
        text to emoji
    } else {
        label to ""
    }
}

@Composable
fun MoodScreen(
    title: String = "Estado de ánimo",
    viewModel: MoodViewModel = viewModel(),
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    //  ViewModel
    val moodsFlow by viewModel.allMoods.collectAsState(initial = emptyList())

    // UI states
    var selectedMood by remember { mutableStateOf<String?>(null) }
    var noteText by remember { mutableStateOf("") }

    // date selection (defaults to today)
    val (ty, tm, td) = todayYMD()
    var viewYear by remember { mutableStateOf(ty) }
    var viewMonth by remember { mutableStateOf(tm) }
    var selectedDay by remember { mutableStateOf(td) }

    // month map (day -> emoji string) showing the latest mood for that day
    val monthMap = remember(moodsFlow, viewYear, viewMonth) {
        val map = mutableMapOf<Int, String?>()
        moodsFlow.forEach { e ->
            val (y, m, d) = epochToYMD(e.date)
            if (y == viewYear && m == viewMonth) {
                if (map[d] == null) {
                    map[d] = e.emotion.split(" ").lastOrNull() ?: ""
                }
            }
        }
        map
    }

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
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Transparent
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(title, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = GrayDark, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
                // EMOTIONS GRID
                item {
                    Text(
                        "¿Cómo te sientes el día de hoy?",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GrayDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(623.dp)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(EMOTIONS) { label ->
                                val (text, emoji) = splitEmotionLabel(label)
                                val isSelected = selectedMood == label

                                val bgBrush = Brush.verticalGradient(
                                    if (isSelected)
                                        listOf(
                                            LavenderAccentLight.copy(alpha = 0.6f), // Lavanda claro para seleccionado
                                            LavenderAccent.copy(alpha = 0.4f)
                                        )
                                    else
                                        listOf(
                                            Color(0x25FFFFFF),
                                            Color(0x18FFFFFF)
                                        )
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(22.dp))
                                        .background(bgBrush)
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) LavenderAccent else Color(0x30FFFFFF), // Borde lavanda para seleccionado
                                            shape = RoundedCornerShape(22.dp)
                                        )
                                        .clickable { selectedMood = label }
                                        .padding(8.dp)
                                        .size(95.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(emoji, fontSize = 40.sp)
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = GrayDark
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                // NOTE + DAY SELECTOR + SAVE
                item {
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = noteText,
                            onValueChange = { noteText = it },
                            modifier = Modifier.weight(1f),
                            label = { Text("Nota (opcional)", color = GrayMedium) },
                            singleLine = true,
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
                        Spacer(Modifier.width(8.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Día", color = GrayMedium, fontSize = 12.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = {
                                    if (selectedDay > 1) selectedDay -= 1
                                }) { Text("◀️", color = Lavender) }
                                Text(selectedDay.toString(), fontWeight = FontWeight.SemiBold, color = GrayDark) // Número de día oscuro
                                IconButton(onClick = {
                                    val maxD = daysInMonth(viewYear, viewMonth)
                                    if (selectedDay < maxD) selectedDay += 1
                                }) { Text("▶️", color = Lavender)}
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Button(
                            onClick = {
                                if (selectedMood == null) {
                                    scope.launch { snackbarHostState.showSnackbar("Selecciona una emoción antes de guardar") }
                                    return@Button
                                }

                                // crear MoodEntity con fecha seleccionada (a mediodía local para consistencia)
                                val cal = Calendar.getInstance().apply {
                                    set(Calendar.YEAR, viewYear)
                                    set(Calendar.MONTH, viewMonth - 1)
                                    set(Calendar.DAY_OF_MONTH, selectedDay)
                                    set(Calendar.HOUR_OF_DAY, 12)
                                    set(Calendar.MINUTE, 0)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }
                                val entity = MoodEntity(
                                    emotion = selectedMood!!,
                                    note = noteText.ifBlank { null },
                                    date = cal.timeInMillis
                                )

                                // insertar via ViewModel (Room)
                                viewModel.addMood(entity)

                                // feedback + limpiar UI
                                scope.launch {
                                    snackbarHostState.showSnackbar("Estado guardado")
                                }
                                noteText = ""
                                selectedMood = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LavenderAccentLight, contentColor = GrayDark), // Botón lavanda claro, texto oscuro
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Guardar", fontWeight = FontWeight.SemiBold) // Texto del botón
                        }
                    }
                }

                // Recent bubbles
                item {
                    Spacer(Modifier.height(8.dp))
                    Text("Tus emociones recientes", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = GrayDark) // Título oscuro
                    Spacer(Modifier.height(8.dp))
                    RecentBubbles(moodsFlow = moodsFlow)
                }

                // Month picker + calendar
                item {
                    Spacer(Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            if (viewMonth == 1) { viewMonth = 12; viewYear -= 1 } else viewMonth -= 1
                            val max = daysInMonth(viewYear, viewMonth)
                            if (selectedDay > max) selectedDay = max
                        }) { Text("◀️", color = Lavender) }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(monthNameSpanish(viewMonth), fontWeight = FontWeight.Bold, color = GrayDark) // Mes oscuro
                            Text(viewYear.toString(), fontSize = 12.sp, color = GrayMedium) // Año gris medio
                        }

                        IconButton(onClick = {
                            if (viewMonth == 12) { viewMonth = 1; viewYear += 1 } else viewMonth += 1
                            val max = daysInMonth(viewYear, viewMonth)
                            if (selectedDay > max) selectedDay = max
                        }) { Text("▶️", color = Lavender) }
                    }

                    Spacer(Modifier.height(8.dp))

                    MoodMonthGridModern(
                        year = viewYear,
                        month = viewMonth,
                        monthMap = monthMap,
                        selectedDay = selectedDay,
                        onDayClick = { day -> selectedDay = day }
                    )
                }

                // Frequency
                item {
                    Spacer(Modifier.height(12.dp))
                    Text("Frecuencia de emociones", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = GrayDark) // Título oscuro
                    Spacer(Modifier.height(8.dp))
                    EmotionFrequency(moodsFlow)
                }

                // Saved notes
                item {
                    Spacer(Modifier.height(12.dp))
                    Text("Notas guardadas", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = GrayDark) // Título oscuro
                    Spacer(Modifier.height(8.dp))
                    SavedNotesList(moodsFlow)
                    Spacer(Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun RecentBubbles(moodsFlow: List<MoodEntity>) {
    val recent = moodsFlow.take(12)
    if (recent.isEmpty()) {
        Text("Aún no hay registros", color = GrayMedium) // Texto gris medio
        return
    }
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        recent.forEach { entry ->
            val emoji = entry.emotion.split(" ").lastOrNull() ?: ""
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(6.dp)) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(LavenderAccentLight, CircleShape), // Fondo de burbuja lavanda claro
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, fontSize = 26.sp)
                }
                Spacer(Modifier.height(6.dp))
                val (y, m, d) = epochToYMD(entry.date)
                Text("$d/$m", fontSize = 12.sp, color = GrayMedium) // Fecha gris medio
            }
        }
    }
}

@Composable
fun EmotionFrequency(moodsFlow: List<MoodEntity>) {
    val counts = moodsFlow.groupingBy { it.emotion }.eachCount().toList().sortedByDescending { it.second }
    if (counts.isEmpty()) {
        Text("Aún no hay estadísticas.", color = GrayMedium) // Texto gris medio
        return
    }
    Column {
        counts.forEach { (emotion, count) ->
            val parts = emotion.split(" ")
            val emoji = parts.lastOrNull() ?: ""
            val title = parts.dropLast(1).joinToString(" ")
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(emoji, fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
                    Text(title, fontWeight = FontWeight.Medium, color = GrayDark) // Título de emoción oscuro
                }
                Text("×$count", color = LavenderAccent, fontWeight = FontWeight.Bold) // Contador lavanda
            }
            Divider(color = Color(0xFFE0E0E0)) // Divisor gris muy claro
        }
    }
}

@Composable
fun SavedNotesList(moodsFlow: List<MoodEntity>) {
    val items = moodsFlow.filter { it.note != null && it.note.isNotBlank() }
    if (items.isEmpty()) {
        Text("Aún no hay notas guardadas", color = GrayMedium) // Texto gris medio
        return
    }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.forEach { entry ->
            val parts = entry.emotion.split(" ")
            val emoji = parts.lastOrNull() ?: ""
            val title = parts.dropLast(1).joinToString(" ")
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = LavenderAccentLight.copy(alpha = 0.4f)), // Fondo de tarjeta lavanda claro semitransparente
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(42.dp).background(LavenderAccentLight, CircleShape), contentAlignment = Alignment.Center) { // Burbuja de emoji lavanda claro
                            Text(emoji, fontSize = 20.sp)
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(title, fontWeight = FontWeight.Bold, color = GrayDark) // Título de emoción oscuro
                            val (y, m, d) = epochToYMD(entry.date)
                            Text("$d/$m/$y", fontSize = 12.sp, color = GrayMedium) // Fecha gris medio
                        }
                    }
                    entry.note?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(it, fontSize = 14.sp, color = GrayDark) // Texto de nota oscuro
                    }
                }
            }
        }
    }
}

@Composable
fun MoodMonthGridModern(
    year: Int,
    month: Int,
    monthMap: Map<Int, String?>,
    selectedDay: Int,
    onDayClick: (Int) -> Unit
) {
    val firstIndex = firstDayIndexMondayBased(year, month)
    val totalDays = daysInMonth(year, month)
    val totalCells = ((firstIndex + totalDays + 6) / 7) * 7
    val cells = (0 until totalCells).map { idx ->
        val day = idx - firstIndex + 1
        if (day in 1..totalDays) day else null
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach {
                Text(it, fontWeight = FontWeight.SemiBold, color = GrayMedium) // Días de la semana gris medio
            }
        }
        Spacer(Modifier.height(8.dp))

        cells.chunked(7).forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                row.forEach { dayNullable ->
                    if (dayNullable == null) {
                        Box(modifier = Modifier.size(44.dp)) {}
                    } else {
                        val emoji = monthMap[dayNullable]
                        val isSelected = dayNullable == selectedDay
                        val bg = when {
                            isSelected -> Brush.verticalGradient(listOf(LavenderAccentLight, LavenderAccent.copy(alpha = 0.6f))) // Seleccionado: lavanda
                            emoji != null -> Brush.verticalGradient(listOf(Color(0x10FFFFFF), Color(0x08FFFFFF))) // Con emoji: muy sutil blanco
                            else -> Brush.verticalGradient(listOf(Color(0x05FFFFFF), Color(0x02FFFFFF))) // Vacío: casi transparente
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                            .padding(vertical = 6.dp)
                            .clickable { onDayClick(dayNullable) }) {
                            Text("$dayNullable", fontSize = 12.sp, color = if (isSelected) GrayDark else GrayMedium) // Número de día oscuro si seleccionado, si no gris medio
                            Spacer(Modifier.height(6.dp))
                            Box(modifier = Modifier
                                .size(44.dp)
                                .shadow(if (isSelected) 6.dp else 0.dp, shape = CircleShape, ambientColor = LavenderAccent.copy(alpha = 0.3f), spotColor = LavenderAccent.copy(alpha = 0.5f)) // Sombra con color lavanda
                                .background(bg, shape = CircleShape),
                                contentAlignment = Alignment.Center) {
                                if (emoji != null) {
                                    Text(emoji, fontSize = 18.sp)
                                } else {
                                    Box(modifier = Modifier.size(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}