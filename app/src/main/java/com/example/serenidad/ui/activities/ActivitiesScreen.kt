package com.example.serenidad.ui.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
// --- Importaciones de iconos ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Style
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
import com.example.serenidad.data.model.ActivityEntity
import com.example.serenidad.data.viewmodel.ActivityViewModel
import com.example.serenidad.data.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
// ---------------------------------

// --- Colores del tema ---
private val GrayMedium = Color(0xFF562B8D)
private val GrayDark = Color(0xFF424242)
private val LavenderAccent = Color(0xFF896ADA)
private val LavenderAccentLight = Color(0xFFEFE6FF)
private val GlassBackground = Color(0x30FFFFFF)
private val GlassBorder = Color(0x50FFFFFF)
private val GreenCompleted = Color(0xFF388E3C)
private val RedDelete = Color(0xFFD32F2F)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ActivitiesScreen(
        title: String = "Actividades Didácticas",
        authViewModel: AuthViewModel,
        activityViewModel: ActivityViewModel = viewModel()
    ) {
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        // Observa el usuario actual
        val currentUser by authViewModel.currentUser.collectAsState()
        val currentUserId = currentUser?.uid // extraemos el UID

        val completedActivities by currentUserId?.let { userId ->
            activityViewModel.getCompletedActivities(userId)
                .collectAsState(initial = emptyList())
        } ?: remember { mutableStateOf(emptyList<ActivityEntity>()) }

        // Filtrar los reencuadres cognitivos
    val reframeLogs = completedActivities.filter { it.activityName == "Reencuadre Cognitivo" }

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
                        "Desarrolla habilidades y encuentra nuevas formas de conectar.",
                        color = GrayMedium,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                }

                // --- Categoría 1: Juegos Cognitivos ---
                item {
                    CategoryHeader(
                        title = "Juegos Cognitivos",
                        icon = Icons.Default.Psychology
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // --- Mini-Juego 1: Reencuadre ---
                item {
                    ReframeGameCard(
                        activityName = "Reencuadre Cognitivo",
                        onAddReframe = { negativeThought, reframeText ->
                            currentUserId?.let { userId ->
                                val combinedNote = "Negativo: $negativeThought\nPositivo: $reframeText"
                                activityViewModel.completeActivity(
                                    activityName = "Reencuadre Cognitivo",
                                    userId = userId,
                                    note = combinedNote
                                )
                                scope.launch {
                                    snackbarHostState.showSnackbar("¡Reencuadre guardado!")
                                }
                            }
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // --- Mini-Juego 2: Rompecabezas de Palabras ---
                item {
                    WordScrambleCard(
                        activityName = "Anagrama de Calma"
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // --- Mini-Juego 3: Juego de Memoria ---
                item {
                    val activityName = "Juego de Memoria"
                    val activityEntity = completedActivities.find { it.activityName == activityName }

                    MemoryGameCard(
                        activityName = activityName,
                        isCompleted = activityEntity != null,
                        onToggleComplete = {
                            currentUserId?.let { userId ->
                                if (activityEntity != null) {
                                    activityViewModel.removeActivity(activityEntity.id)
                                    scope.launch { snackbarHostState.showSnackbar("Actividad desmarcada") }
                                } else {
                                    activityViewModel.completeActivity(activityName, userId)
                                    scope.launch { snackbarHostState.showSnackbar("¡Juego completado!") }
                                }
                            }
                        }
                    )
                    Spacer(Modifier.height(24.dp))
                }


                // --- Categoría 2: Mis Reencuadres Guardados ---
                if (reframeLogs.isNotEmpty()) {
                    item {
                        CategoryHeader(
                            title = "Mis Reencuadres Guardados",
                            icon = Icons.Default.History
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    items(reframeLogs) { reframeEntry ->
                        ReframeLogCard(
                            record = reframeEntry,
                            onDelete = {
                                activityViewModel.removeActivity(reframeEntry.id)
                                scope.launch { snackbarHostState.showSnackbar("Registro borrado") }
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    item {
                        Spacer(Modifier.height(24.dp))
                    }
                }

                // --- Categoría 3: Actividades Guiadas ---
                item {
                    CategoryHeader(
                        title = "Actividades Guiadas",
                        icon = Icons.Default.Lightbulb
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // --- Tarjeta de Asociación de Palabras ---
                item {
                    val activityName = "Asociación de Palabras"
                    val activityEntity = completedActivities.find { it.activityName == activityName }

                    TrackableActivityCard(
                        title = activityName,
                        description = "Relaciona palabras con imágenes, emociones o acciones. Ej: “LUZ” → sol, linterna. “TRISTEZA” → llorar, azul.",
                        icon = Icons.Default.CompareArrows,
                        isCompleted = activityEntity != null,
                        onToggleComplete = {
                            currentUserId?.let { userId ->
                                if (activityEntity != null) {
                                    activityViewModel.removeActivity(activityEntity.id)
                                    scope.launch { snackbarHostState.showSnackbar("Actividad desmarcada") }
                                } else {
                                    activityViewModel.completeActivity(activityName, userId)
                                    scope.launch { snackbarHostState.showSnackbar("¡Actividad completada!") }
                                }
                            }
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // --- Tarjeta de Resolución de Problemas ---
                item {
                    val activityName = "Resolución de Problemas"
                    val activityEntity = completedActivities.find { it.activityName == activityName }

                    TrackableActivityCard(
                        title = activityName,
                        description = "Aprende a dividir un problema grande en pasos pequeños y manejables. Define el problema, genera opciones y elige una.",
                        icon = Icons.Default.Lightbulb,
                        isCompleted = activityEntity != null,
                        onToggleComplete = {
                            currentUserId?.let { userId ->
                                if (activityEntity != null) {
                                    activityViewModel.removeActivity(activityEntity.id)
                                    scope.launch { snackbarHostState.showSnackbar("Actividad desmarcada") }
                                } else {
                                    activityViewModel.completeActivity(activityName, userId)
                                    scope.launch { snackbarHostState.showSnackbar("¡Actividad completada!") }
                                }
                            }
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // --- Tarjeta de Límites Saludables ---
                item {
                    val activityName = "Límites Saludables"
                    val activityEntity = completedActivities.find { it.activityName == activityName }

                    TrackableActivityCard(
                        title = activityName,
                        description = "Practica decir 'no' de forma respetuosa pero firme. Reconoce tus necesidades y comunícalas claramente.",
                        icon = Icons.Default.Edit,
                        isCompleted = activityEntity != null,
                        onToggleComplete = {
                            currentUserId?.let { userId ->
                                if (activityEntity != null) {
                                    activityViewModel.removeActivity(activityEntity.id)
                                    scope.launch { snackbarHostState.showSnackbar("Actividad desmarcada") }
                                } else {
                                    activityViewModel.completeActivity(activityName, userId)
                                    scope.launch { snackbarHostState.showSnackbar("¡Actividad completada!") }
                                }
                            }
                        }
                    )
                    Spacer(Modifier.height(24.dp))
                }

                // --- Categoría 4: Conexión y Propósito ---
                item {
                    CategoryHeader(
                        title = "Conexión y Propósito",
                        icon = Icons.Default.Favorite
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // --- Tarjeta Explora un Hobby ---
                item {
                    val activityName = "Explora un Hobby"
                    val activityEntity = completedActivities.find { it.activityName == activityName }

                    TrackableActivityCard(
                        title = activityName,
                        description = "Dedica 30 minutos a un pasatiempo que disfrutes o a explorar uno nuevo (dibujar, jardinería, aprender un instrumento).",
                        icon = Icons.Default.Extension,
                        isCompleted = activityEntity != null,
                        onToggleComplete = {
                            currentUserId?.let { userId ->
                                if (activityEntity != null) {
                                    activityViewModel.removeActivity(activityEntity.id)
                                } else {
                                    activityViewModel.completeActivity(activityName, userId)
                                }
                            }
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // --- Tarjeta Conexión Social ---
                item {
                    val activityName = "Conexión Social"
                    val activityEntity = completedActivities.find { it.activityName == activityName }

                    TrackableActivityCard(
                        title = activityName,
                        description = "Envía un mensaje positivo a un amigo o familiar con el que no has hablado recientemente.",
                        icon = Icons.Default.Diversity3,
                        isCompleted = activityEntity != null,
                        onToggleComplete = {
                            currentUserId?.let { userId ->
                                if (activityEntity != null) {
                                    activityViewModel.removeActivity(activityEntity.id)
                                } else {
                                    activityViewModel.completeActivity(activityName, userId)
                                }
                            }
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}


// 1. Data class para la tarjeta de memoria (sin 'private')
data class MemoryCard(
    val id: Int,
    val pairId: Int, // Cartas con el mismo pairId son pareja
    val text: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)

// 2. Lista de pares
private val allMemoryPairs = listOf(
    "LUZ" to "SOL",
    "PAZ" to "CALMA",
    "FUERZA" to "VALOR",
    "ALEGRÍA" to "FELIZ",
    "SUEÑO" to "DORMIR",
    "MENTE" to "PENSAR",
    "REÍR" to "SONRISA",
    "AMIGO" to "COMPAÑÍA",
    "TRANQUILO" to "RELAX",
    "SERENIDAD" to "PAZ INTERIOR",
    "CONFIANZA" to "SEGURIDAD",
    "OPTIMISMO" to "POSITIVIDAD",
    "ESPERANZA" to "FE",
    "ENERGÍA" to "VITALIDAD",
    "CREATIVIDAD" to "IMAGINACIÓN",
    "ARMONÍA" to "EQUILIBRIO",
    "FELICIDAD" to "ALEGRÍA",
    "FORTALEZA" to "RESISTENCIA",
    "AMOR" to "CARIÑO",
    "COMPASIÓN" to "EMPATÍA",
    "LIBERTAD" to "AUTONOMÍA",
    "ILUMINAR" to "ENCENDER",
    "MEDITAR" to "REFLEXIONAR",
    "APRENDER" to "CONOCER",
    "TRABAJO" to "ESFUERZO",
    "RESPETO" to "CONSIDERACIÓN",
    "SABIDURÍA" to "CONOCIMIENTO",
    "ENTUSIASMO" to "MOTIVACIÓN",
    "SONRISA" to "FELICIDAD",
    "GRATITUD" to "AGRADECIMIENTO"
)

/**
 * Obtiene 3 pares aleatorios de la lista principal.
 */
private fun getRandomMemoryPairs(): List<Pair<String, String>> {
    return allMemoryPairs.shuffled().take(3)
}

// 3. Composable del juego de memoria (MODIFICADO)
@Composable
fun MemoryGameCard(
    activityName: String,
    isCompleted: Boolean,
    onToggleComplete: () -> Unit
) {
    // --- Lógica del Juego ---
    var restartKey by remember { mutableStateOf(0) } // Para reiniciar el juego

    // Cards como estado observable
    val cardsState = remember(restartKey) {
        mutableStateOf(
            getRandomMemoryPairs().flatMapIndexed { index, pair ->
                listOf(
                    MemoryCard(id = index * 2, pairId = index, text = pair.first),
                    MemoryCard(id = index * 2 + 1, pairId = index, text = pair.second)
                )
            }.shuffled()
        )
    }

    var selectedCards by remember(restartKey) { mutableStateOf(emptyList<MemoryCard>()) }
    var isChecking by remember(restartKey) { mutableStateOf(false) }

    // Extraemos la lista de cards del estado observable
    val cards by cardsState

    // DerivedStateOf correctamente conectado a cards
    val isGameWon by remember(cards) { derivedStateOf { cards.all { it.isMatched } } }

    // Efecto que comprueba las cartas cuando se seleccionan 2
    LaunchedEffect(selectedCards, isChecking) {
        if (isChecking && selectedCards.size == 2) {
            val (card1, card2) = selectedCards

            delay(1000) // Espera 1 segundo

            cardsState.value = if (card1.pairId == card2.pairId) {
                cards.map {
                    if (it.id == card1.id || it.id == card2.id) it.copy(isMatched = true, isFlipped = false) else it
                }
            } else {
                cards.map {
                    if (it.id == card1.id || it.id == card2.id) it.copy(isFlipped = false) else it
                }
            }

            selectedCards = emptyList()
            isChecking = false
        }
    }
    // --- UI de la Tarjeta ---
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LavenderAccentLight.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Título y Botón de Reiniciar
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Style, contentDescription = activityName, tint = GrayMedium, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text(activityName, fontWeight = FontWeight.Bold, color = GrayDark, fontSize = 17.sp)
                Spacer(Modifier.weight(1f)) // Empuja el botón a la derecha
                IconButton(onClick = {
                    restartKey++ // Incrementa la clave para forzar la regeneración del juego
                }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reiniciar Juego", tint = GrayMedium)
                }
            }
            Spacer(Modifier.height(12.dp))

            Text("Encuentra los pares de sinónimos:", color = GrayDark, fontSize = 14.sp)
            Spacer(Modifier.height(12.dp))

            // Cuadrícula del juego
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    // --- CORRECCIÓN DE SUPERPOSICIÓN: 160.dp -> 168.dp ---
                    // (2 filas * 80.dp) + (1 espacio * 8.dp)
                    .height(168.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items(cards) { card ->
                    MemoryCardItem(
                        card = card,
                        onClick = {
                            if (!isChecking && !card.isFlipped && !card.isMatched) {
                                cardsState.value = cards.map  {
                                    if (it.id == card.id) it.copy(isFlipped = true) else it }
                                val newSelected = selectedCards + card
                                if (newSelected.size == 2) {
                                    isChecking = true
                                    selectedCards = newSelected
                                } else {
                                    selectedCards = newSelected
                                }
                            }
                        }
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            }
        }
    }
// 4. Composable para la CARTA individual del juego de memoria
@Composable
fun MemoryCardItem(
    card: MemoryCard,
    onClick: () -> Unit
) {
    val cardColor = when {
        card.isMatched -> GreenCompleted.copy(alpha = 0.4f)
        card.isFlipped -> LavenderAccentLight
        else -> GrayMedium.copy(alpha = 0.3f)
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .size(80.dp) // <-- Tamaño fijo de la carta
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (card.isFlipped || card.isMatched) {
                Text(
                    text = card.text,
                    color = GrayDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ReframeGameCard(
    activityName: String,
    onAddReframe: (negativeThought: String, reframeText: String) -> Unit
) {
    var negativeText by remember { mutableStateOf("") }
    var reframeText by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LavenderAccentLight.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = activityName,
                    tint = GrayMedium,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = activityName,
                    fontWeight = FontWeight.Bold,
                    color = GrayDark,
                    fontSize = 17.sp
                )
            }
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = negativeText,
                onValueChange = { negativeText = it },
                label = { Text("Pensamiento Negativo", color = GrayMedium) },
                placeholder = { Text("Ej: 'Soy un fracaso total'", color = GrayMedium.copy(alpha = 0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = defaultTextFieldColors()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = reframeText,
                onValueChange = { reframeText = it },
                label = { Text("Reencuadra el pensamiento", color = GrayMedium) },
                placeholder = { Text("Ej: 'Fallé, pero aprendí...'", color = GrayMedium.copy(alpha = 0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = defaultTextFieldColors()
            )
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    onAddReframe(negativeText, reframeText)
                    negativeText = ""
                    reframeText = ""
                },
                enabled = negativeText.isNotBlank() && reframeText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LavenderAccentLight,
                    contentColor = GrayDark,
                    disabledContainerColor = Color.White.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Guardar", modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Guardar Reencuadre", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

/**
 * Tarjeta para mostrar un reencuadre ya guardado.
 */
@Composable
fun ReframeLogCard(record: ActivityEntity, onDelete: () -> Unit) {
    val parts = record.note?.split("\n")
    val negativeText = parts?.firstOrNull() ?: "Error al cargar"
    val positiveText = parts?.getOrNull(1) ?: ""

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Text(
                text = formatTimestamp(record.dateCompleted),
                color = GrayMedium,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))

            Text(negativeText, color = GrayDark, fontSize = 14.sp)
            Text(positiveText, color = GreenCompleted, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = RedDelete.copy(alpha = 0.7f))
            }
        }
    }
}


/**
 * Mini-Juego de Anagrama (Rompecabezas de Palabras)
 */
@Composable
fun WordScrambleCard(activityName: String) {

    val wordList = remember {
        listOf(
            "RESPIRA" to "RSEIPRA",
            "CALMA" to "ALAMC",
            "PAZ" to "ZAP",
            "AMOR" to "MORA",
            "LUZ" to "ZUL",
            "ENERGÍA" to "GÍRENEA",
            "TRANQUILO" to "UQILNORTA",
            "SERENIDAD" to "DANIDESER",
            "FUERZA" to "ZARFUE",
            "CONCENTRACIÓN" to "NÓICNARTNEC",
            "SONRISA" to "ASIRNOS",
            "ESPERANZA" to "AZNAREPSE",
            "VITALIDAD" to "DALIDLATIV",
            "POSITIVO" to "OVITISOP",
            "RELAX" to "XALER",
            "FORTALEZA" to "AZELATROF",
            "HARMONÍA" to "AÍNOMRAH",
            "CRECIMIENTO" to "OTNEMIECERC",
            "ÉXITO" to "OTÍXE",
            "FELICIDAD" to "DADICILEF",
            "RESILIENCIA" to "AICNEILISER",
            "OPTIMISMO" to "OMSIMITPO",
            "TRANSMITIR" to "RITIMSNART",
            "CONFIANZA" to "AZNAIFNOC",
            "ILUMINAR" to "RANIMULI",
            "ARMONÍA" to "AÍNOMRA",
            "ENTUSIASMO" to "OMSASIUTNE",
            "APRECIAR" to "RAICERPA",
            "MEDITAR" to "RATEDIM",
            "ALEGRÍA" to "AÍRGELA"
        ).shuffled()
    }

    var currentWordIndex by remember { mutableStateOf(0) }
    var userGuess by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val (correctWord, scrambledWord) = wordList[currentWordIndex]

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LavenderAccentLight.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Extension, contentDescription = activityName, tint = GrayMedium, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text(activityName, fontWeight = FontWeight.Bold, color = GrayDark, fontSize = 17.sp)
            }
            Spacer(Modifier.height(12.dp))

            Text("Organiza las letras:", color = GrayDark, fontSize = 14.sp)
            Text(
                scrambledWord.replace(""," ").trim(),
                color = GrayDark,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = userGuess,
                onValueChange = {
                    userGuess = it
                    if (showError) showError = false
                },
                label = { Text("Tu respuesta", color = GrayMedium) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !isCorrect,
                isError = showError,
                colors = defaultTextFieldColors(isError = showError)
            )
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    if (isCorrect) {
                        val nextIndex = (currentWordIndex + 1) % wordList.size
                        currentWordIndex = nextIndex
                        userGuess = ""
                        isCorrect = false
                    } else {
                        if (userGuess.equals(correctWord, ignoreCase = true)) {
                            isCorrect = true
                            showError = false
                        } else {
                            showError = true
                        }
                    }
                },
                enabled = userGuess.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCorrect) GreenCompleted else LavenderAccentLight,
                    contentColor = if (isCorrect) Color.White else GrayDark,
                    disabledContainerColor = Color.White.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                if (isCorrect) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Correcto", modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = if (isCorrect) "¡Correcto! Siguiente" else "Comprobar",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


/**
 * Tarjeta para actividades (MODIFICADA para permitir desmarcar)
 */
@Composable
fun TrackableActivityCard(
    title: String,
    description: String,
    icon: ImageVector,
    isCompleted: Boolean,
    onToggleComplete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LavenderAccentLight.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = title, tint = GrayMedium, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.Bold, color = GrayDark, fontSize = 17.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(description, color = GrayMedium, fontSize = 14.sp, lineHeight = 20.sp)
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onToggleComplete,
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) GreenCompleted else LavenderAccentLight,
                    contentColor = if (isCompleted) Color.White else GrayDark,
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Completado", modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = if (isCompleted) "Completado" else "Marcar como completado",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Cabecera de Categoría
 */
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

/**
 * Función helper para formatear fechas
 */
private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale("es", "ES"))
    return sdf.format(Date(timestamp))
}

/**
 * Función helper para los colores de OutlinedTextField
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun defaultTextFieldColors(isError: Boolean = false): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = GrayDark,
        unfocusedTextColor = GrayDark,
        focusedBorderColor = LavenderAccent,
        unfocusedBorderColor = GrayMedium,
        focusedLabelColor = LavenderAccent,
        unfocusedLabelColor = GrayMedium,
        cursorColor = LavenderAccent,
        errorBorderColor = RedDelete,
        errorLabelColor = RedDelete,
        disabledLabelColor = GrayMedium,
        disabledTextColor = GrayDark
    )
}