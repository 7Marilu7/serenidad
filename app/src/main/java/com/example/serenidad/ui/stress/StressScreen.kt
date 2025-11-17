package com.example.serenidad.ui.stress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
// --- Importaciones de iconos ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Quiz // <-- NUEVO ICONO
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

// --- Importaciones de ViewModel ---
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serenidad.data.model.StressEntity
import com.example.serenidad.data.viewmodel.AuthViewModel
import com.example.serenidad.data.viewmodel.StressViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
// --- NUEVA IMPORTACIÓN ---
import kotlin.math.roundToInt
// ------------------------

// --- Colores del tema ---
private val GrayMedium = Color(0xFF562B8D)
private val GrayDark = Color(0xFF424242)
private val LavenderAccent = Color(0xFF896ADA)
private val LavenderAccentLight = Color(0xFFEFE6FF)
private val GlassBackground = Color(0x30FFFFFF)
private val GlassBorder = Color(0x50FFFFFF)
private val RedStress = Color(0xFFD32F2F)
private val GreenCompleted = Color(0xFF388E3C) // Verde
private val AmberMedium = Color(0xFFFFA000) // Ámbar

// Lista de síntomas disponibles
private val physicalSymptoms = listOf("Fatiga", "Problemas de sueño", "Malestar estomacal", "Tensión muscular", "Dolor de cabeza")
private val emotionalSymptoms = listOf("Abrumado/a", "Mal humor", "Tristeza", "Ansiedad", "Inquietud")

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class) // Añadido ExperimentalLayoutApi
@Composable
fun StressScreen(
    title: String = "Registro de Estrés",
    authViewModel: AuthViewModel,
    stressViewModel: StressViewModel = viewModel()
) {

    // --- Estados para el formulario ---
    var stressLevel by remember { mutableStateOf(5f) }
    val selectedSymptoms = remember { mutableStateSetOf<String>() }
    var stressNote by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentUser by authViewModel.currentUser.collectAsState()
    val currentUserId = currentUser?.uid


    // --- Obtener registros anteriores ---
    val stressRecordsFlow = remember(currentUserId) {
        currentUserId?.let { userId ->
            stressViewModel.getStressRecords(userId)
        } ?: emptyFlow()
    }
    val stressRecords by stressRecordsFlow.collectAsState(initial = emptyList<StressEntity>())

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
                        "Registra tus niveles de estrés y síntomas asociados.",
                        color = GrayMedium,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                }

                // --- Categoría 1: Registra tu Nivel de Estrés ---
                item {
                    CategoryHeader(
                        title = "Registro Diario",
                        icon = Icons.Default.Edit
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

                            // --- Nivel de Estrés (Slider) ---
                            Text("Nivel de Estrés Percibido (0-10)", fontWeight = FontWeight.SemiBold, color = GrayDark)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Slider(
                                    value = stressLevel,
                                    onValueChange = { stressLevel = it },
                                    valueRange = 0f..10f,
                                    steps = 9,
                                    modifier = Modifier.weight(1f),
                                    colors = SliderDefaults.colors(
                                        thumbColor = LavenderAccent,
                                        activeTrackColor = LavenderAccent,
                                        inactiveTrackColor = LavenderAccentLight
                                    )
                                )
                                Text(
                                    "${stressLevel.toInt()}",
                                    fontWeight = FontWeight.Bold,
                                    color = if (stressLevel > 7) RedStress else GrayMedium,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                            Spacer(Modifier.height(16.dp))

                            // --- Síntomas Físicos (Chips) ---
                            Text("Síntomas Físicos", fontWeight = FontWeight.SemiBold, color = GrayDark)
                            Spacer(Modifier.height(8.dp))
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                physicalSymptoms.forEach { symptom ->
                                    SymptomChip(
                                        text = symptom,
                                        isSelected = symptom in selectedSymptoms,
                                        onSelected = {
                                            if (symptom in selectedSymptoms) selectedSymptoms.remove(symptom)
                                            else selectedSymptoms.add(symptom)
                                        }
                                    )
                                }
                            }
                            Spacer(Modifier.height(16.dp))

                            // --- Síntomas Emocionales (Chips) ---
                            Text("Síntomas Emocionales", fontWeight = FontWeight.SemiBold, color = GrayDark)
                            Spacer(Modifier.height(8.dp))
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                emotionalSymptoms.forEach { symptom ->
                                    SymptomChip(
                                        text = symptom,
                                        isSelected = symptom in selectedSymptoms,
                                        onSelected = {
                                            if (symptom in selectedSymptoms) selectedSymptoms.remove(symptom)
                                            else selectedSymptoms.add(symptom)
                                        }
                                    )
                                }
                            }
                            Spacer(Modifier.height(20.dp))

                            // --- Notas Adicionales ---
                            OutlinedTextField(
                                value = stressNote,
                                onValueChange = { stressNote = it },
                                label = { Text("¿Qué causó este estrés? (Opcional)", color = GrayMedium) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = defaultTextFieldColors()
                            )
                            Spacer(Modifier.height(20.dp))

                            // --- Botón de Guardar ---
                            Button(
                                onClick = {
                                    val userId = currentUserId
                                    if (userId != null) {
                                        stressViewModel.addStressRecord(
                                            stressLevel = stressLevel.toInt(),
                                            symptoms = selectedSymptoms.toList(),
                                            note = stressNote.ifBlank { null },
                                            userId = userId
                                        )
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Registro de estrés guardado")
                                        }
                                        selectedSymptoms.clear()
                                        stressNote = ""
                                        stressLevel = 5f
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Error: No se pudo identificar al usuario.")
                                        }
                                    }
                                },
                                enabled = currentUserId != null,
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

                // --- Categoría 2: Registros Anteriores ---
                item {
                    CategoryHeader(
                        title = "Registros Anteriores",
                        icon = Icons.Default.History
                    )
                    Spacer(Modifier.height(16.dp))
                }

                if (stressRecords.isEmpty()) {
                    item {
                        Text(
                            "Aún no tienes registros de estrés.",
                            color = GrayMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                } else {
                    items(stressRecords) { record ->
                        StressRecordItem(record = record)
                        Spacer(Modifier.height(8.dp))
                    }
                }

                item {
                    Spacer(Modifier.height(24.dp))
                }

                // --- Categoría 3: Herramientas ---
                item {
                    CategoryHeader(
                        title = "Herramientas de Evaluación",
                        icon = Icons.Default.Analytics
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    InfoCard(
                        title = "Escala de Estrés Percibido (PSS)",
                        description = "Una escala psicológica reconocida. Pregunta sobre sentimientos y pensamientos durante el último mes. Ej: '¿Con qué frecuencia te has sentido nervioso o estresado?', '¿...has sentido que no podías controlar las cosas importantes?'",
                        icon = Icons.Default.QuestionAnswer
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // --- ¡NUEVO COMPONENTE AÑADIDO AQUÍ! ---
                item {
                    PSS10QuizCard()
                    Spacer(Modifier.height(16.dp))
                }
                // --- FIN DEL NUEVO COMPONENTE ---
            }
        }
    }
}

// --- ¡NUEVO COMPONENTE DEL CUESTIONARIO! ---

private val pss10Questions = listOf(
    "Me he sentido incapaz de controlar las cosas importantes en mi vida.",
    "Me he sentido nervioso(a) o estresado(a).",
    "He sentido que no puedo controlar los eventos importantes.",
    "Me he sentido confiado(a) en mi capacidad para manejar problemas personales.",
    "He sentido que las cosas me superan.",
    "He podido controlar la irritación en mi vida.",
    "He sentido que no tengo suficiente control sobre mi vida.",
    "He sentido que las dificultades se acumulan demasiado para mí.",
    "He podido afrontar los problemas de manera efectiva.",
    "He sentido que no puedo manejar todas las responsabilidades que tengo."
)
private val pss10InvertedIndices = setOf(3, 5, 8) // Índices 0-based de las preguntas 4, 6, 9

/**
 * Un Composable que contiene el cuestionario PSS-10 completo.
 */
@Composable
fun PSS10QuizCard() {
    // --- Estado del Cuestionario ---
    var answers by remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }
    var showResult by remember { mutableStateOf(false) }
    var calculatedScore by remember { mutableStateOf<Int?>(null) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LavenderAccentLight.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Quiz, contentDescription = "Cuestionario", tint = GrayMedium, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text("Cuestionario PSS-10", fontWeight = FontWeight.Bold, color = GrayDark, fontSize = 17.sp)
            }
            Spacer(Modifier.height(12.dp))

            if (!showResult) {
                // --- FASE 1: PREGUNTAS ---
                Text(
                    "En las últimas 4 semanas, ¿con qué frecuencia...",
                    color = GrayDark,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))

                // Lista de 10 preguntas
                pss10Questions.forEachIndexed { index, questionText ->
                    PSSQuestionItem(
                        question = "Ítem ${index + 1}: $questionText",
                        score = answers[index], // Puede ser null si no está respondido
                        onScoreChange = { newScore ->
                            // Actualiza el mapa de respuestas
                            answers = answers.toMutableMap().apply { put(index, newScore) }
                        }
                    )
                    Spacer(Modifier.height(12.dp))
                }

                Spacer(Modifier.height(16.dp))

                // Botón para Calcular
                Button(
                    onClick = {
                        var totalScore = 0
                        answers.forEach { (index, score) ->
                            totalScore += if (index in pss10InvertedIndices) (4 - score) else score
                        }
                        calculatedScore = totalScore
                        showResult = true
                    },
                    // Solo se activa si todas las 10 preguntas están respondidas
                    enabled = answers.size == 10,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LavenderAccent,
                        contentColor = Color.White,
                        disabledContainerColor = LavenderAccentLight
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Calcular Puntuación", fontWeight = FontWeight.SemiBold)
                }

            } else {
                // --- FASE 2: RESULTADO ---
                val score = calculatedScore ?: 0
                val (interpretation, interpretationColor) = when (score) {
                    in 0..13 -> "Estrés Bajo" to GreenCompleted
                    in 14..26 -> "Estrés Moderado" to AmberMedium
                    else -> "Estrés Alto" to RedStress
                }

                Text("Tu puntuación PSS-10 es:", color = GrayDark, fontSize = 16.sp)
                Text(
                    "$score",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = interpretationColor
                )
                Text(
                    interpretation,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = interpretationColor
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "0–13: Estrés Bajo\n14–26: Estrés Moderado\n27–40: Estrés Alto",
                    color = GrayMedium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                Spacer(Modifier.height(16.dp))

                // Botón para reiniciar el cuestionario
                Button(
                    onClick = {
                        showResult = false
                        answers = emptyMap()
                        calculatedScore = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LavenderAccentLight,
                        contentColor = GrayDark
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Volver a tomar", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

/**
 * Un Composable para cada pregunta del PSS-10.
 */
@Composable
fun PSSQuestionItem(
    question: String,
    score: Int?, // 0-4 o null
    onScoreChange: (Int) -> Unit
) {
    // Etiquetas para la escala 0-4
    val scaleLabels = listOf("Nunca", "Casi Nunca", "A veces", "Bastante", "Muy")

    Column(Modifier.fillMaxWidth()) {
        Text(question, fontSize = 14.sp, color = GrayDark, lineHeight = 18.sp)
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(24.dp) // Altura fija para el slider
        ) {
            Slider(
                value = score?.toFloat() ?: -1f, // -1f para que el slider aparezca al inicio si es null
                onValueChange = {
                    // No permitas -1f, si el slider se toca, va a 0
                    if (it == -1f) {
                        onScoreChange(0)
                    } else {
                        onScoreChange(it.roundToInt())
                    }
                },
                valueRange = 0f..4f,
                steps = 3, // 3 pasos intermedios (para 5 valores: 0, 1, 2, 3, 4)
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(
                    thumbColor = if (score != null) LavenderAccent else GrayMedium,
                    activeTrackColor = LavenderAccent,
                    inactiveTrackColor = LavenderAccentLight
                )
            )
            Text(
                text = if (score != null) scaleLabels[score] else "?",
                color = if (score != null) GrayMedium else GrayMedium.copy(alpha=0.5f),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 12.dp).width(55.dp) // Ancho fijo para alinear
            )
        }
    }
}

// --- Helper Composables (Copiados de pantallas anteriores) ---

@OptIn(ExperimentalLayoutApi::class) // Añadido ExperimentalLayoutApi
@Composable
fun SymptomChip(
    text: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onSelected,
        label = { Text(text, fontWeight = FontWeight.Medium) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = LavenderAccent,
            selectedLabelColor = Color.White,
            containerColor = LavenderAccentLight.copy(alpha = 0.6f),
            labelColor = GrayDark
        ),
        shape = RoundedCornerShape(16.dp)
    )
}

@OptIn(ExperimentalLayoutApi::class) // Añadido ExperimentalLayoutApi
@Composable
fun StressRecordItem(record: StressEntity) {
    val symptomsList = record.symptoms.split(",").filter { it.isNotBlank() }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.7f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTimestamp(record.date),
                    fontWeight = FontWeight.Bold,
                    color = GrayDark,
                    fontSize = 16.sp
                )
                Text(
                    text = "Nivel: ${record.stressLevel}/10",
                    fontWeight = FontWeight.Bold,
                    color = if (record.stressLevel > 7) RedStress else GrayMedium,
                    fontSize = 16.sp
                )
            }

            if (symptomsList.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    symptomsList.forEach { symptom ->
                        Chip(text = symptom)
                    }
                }
            }

            record.note?.let {
                if (it.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Nota: $it",
                        color = GrayDark.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        style = androidx.compose.ui.text.TextStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    )
                }
            }
        }
    }
}

@Composable
fun Chip(text: String) {
    Box(
        modifier = Modifier
            .background(LavenderAccentLight.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, color = GrayDark, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

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

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale("es", "ES"))
    return sdf.format(Date(timestamp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun defaultTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = GrayDark,
        unfocusedTextColor = GrayDark,
        focusedBorderColor = LavenderAccent,
        unfocusedBorderColor = GrayMedium,
        focusedLabelColor = LavenderAccent,
        unfocusedLabelColor = GrayMedium,
        cursorColor = LavenderAccent
    )
}