package com.example.serenidad.ui.journal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Importaciones de ViewModel ---
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serenidad.data.model.JournalEntity
import com.example.serenidad.data.viewmodel.AuthViewModel
import com.example.serenidad.data.viewmodel.JournalViewModel
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
private val RedDelete = Color(0xFFD32F2F)

// --- Colores de "Papel" para la libreta ---
private val paperColors = listOf(
    "#FFFFE0", // Crema (LightYellow)
    "#E0FFFF", // Celeste (LightCyan)
    "#FFE0F0", // Rosa (LightPink)
    "#FFFFFF"  // Blanco
)
private val defaultPaperColor = paperColors.first()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    title: String = "Mi Diario",
    authViewModel: AuthViewModel,
    journalViewModel: JournalViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentUser by authViewModel.currentUser.collectAsState()
    val currentUserId = currentUser?.uid

    // --- Estados del Editor ---
    var currentText by remember { mutableStateOf("") }
    var currentColorHex by remember { mutableStateOf(defaultPaperColor) }
    var editingEntry by remember { mutableStateOf<JournalEntity?>(null) } // Si no es null, estamos editando

    // --- Obtener entradas anteriores ---
    val journalEntriesFlow = remember(currentUserId) {
        currentUserId?.let { userId ->
            journalViewModel.getJournalEntries(userId)
        } ?: emptyFlow()
    }
    val journalEntries by journalEntriesFlow.collectAsState(initial = emptyList<JournalEntity>())

    // --- Lógica para limpiar el editor ---
    fun clearEditor() {
        currentText = ""
        currentColorHex = defaultPaperColor
        editingEntry = null
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
                        "Un espacio seguro para tus pensamientos.",
                        color = GrayMedium,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                }

                // --- Categoría 1: Editor de Diario (La "Libreta") ---
                item {
                    CategoryHeader(
                        title = if (editingEntry == null) "Nueva Entrada" else "Editando Entrada",
                        icon = if (editingEntry == null) Icons.Default.Add else Icons.Default.Edit
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // La tarjeta de "papel"
                item {
                    JournalEditorCard(
                        text = currentText,
                        onTextChange = { currentText = it },
                        selectedColorHex = currentColorHex,
                        onColorSelect = { currentColorHex = it },
                        isEditing = editingEntry != null,
                        onSave = {
                            currentUserId?.let { userId ->
                                if (editingEntry == null) {
                                    // --- Lógica de GUARDAR NUEVO ---
                                    journalViewModel.addEntry(currentText, currentColorHex, userId)
                                    scope.launch { snackbarHostState.showSnackbar("Entrada guardada") }
                                } else {
                                    // --- Lógica de ACTUALIZAR ---
                                    val updatedEntry = editingEntry!!.copy(
                                        content = currentText,
                                        colorHex = currentColorHex
                                    )
                                    journalViewModel.updateEntry(updatedEntry)
                                    scope.launch { snackbarHostState.showSnackbar("Entrada actualizada") }
                                }
                                clearEditor()
                            }
                        },
                        onCancel = { clearEditor() }, // "Cancelar" solo funciona en modo edición
                        onDelete = {
                            editingEntry?.let {
                                journalViewModel.deleteEntry(it.id)
                                scope.launch { snackbarHostState.showSnackbar("Entrada borrada") }
                                clearEditor()
                            }
                        }
                    )
                    Spacer(Modifier.height(24.dp))
                }

                // --- Categoría 2: Entradas Anteriores ---
                item {
                    CategoryHeader(
                        title = "Entradas Anteriores",
                        icon = Icons.Default.History
                    )
                    Spacer(Modifier.height(16.dp))
                }

                if (journalEntries.isEmpty()) {
                    item {
                        Text(
                            "Aún no tienes entradas en tu diario.",
                            color = GrayMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                } else {
                    items(journalEntries) { entry ->
                        JournalLogCard(
                            entry = entry,
                            onClick = {
                                // Carga la entrada en el editor
                                editingEntry = entry
                                currentText = entry.content
                                currentColorHex = entry.colorHex
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}


/**
 * El editor de texto principal (la "libreta").
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEditorCard(
    text: String,
    onTextChange: (String) -> Unit,
    selectedColorHex: String,
    onColorSelect: (String) -> Unit,
    isEditing: Boolean,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            // El color del "papel" se aplica aquí
            containerColor = Color(android.graphics.Color.parseColor(selectedColorHex))
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // El campo de texto que parece papel
            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp), // Altura mínima
                placeholder = { Text("Escribe lo que sientes...", color = GrayMedium.copy(alpha = 0.5f)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent, // Sin línea abajo
                    unfocusedIndicatorColor = Color.Transparent, // Sin línea abajo
                    cursorColor = GrayDark,
                    focusedTextColor = GrayDark,
                    unfocusedTextColor = GrayDark
                )
            )

            // Selector de color
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                paperColors.forEach { colorHex ->
                    ColorSelectorDot(
                        colorHex = colorHex,
                        isSelected = colorHex == selectedColorHex,
                        onClick = { onColorSelect(colorHex) }
                    )
                }
            }

            // Botones de acción
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isEditing) {
                    // Botón de Borrar (solo en modo edición)
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = RedDelete)
                    }
                    // Botón de Cancelar (solo en modo edición)
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.Close, contentDescription = "Cancelar Edición", tint = GrayMedium)
                    }
                }

                // Botón de Guardar / Actualizar
                Button(
                    onClick = onSave,
                    enabled = text.isNotBlank(), // Solo se activa si hay texto
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LavenderAccentLight,
                        contentColor = GrayDark
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isEditing) "Actualizar" else "Guardar",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * Un punto de color seleccionable.
 */
@Composable
fun ColorSelectorDot(
    colorHex: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .padding(4.dp)
            .clip(CircleShape)
            .background(Color(android.graphics.Color.parseColor(colorHex)))
            .border(
                width = 2.dp,
                color = if (isSelected) LavenderAccent else Color.Gray.copy(alpha = 0.5f),
                shape = CircleShape
            )
            .clickable(onClick = onClick)
    )
}

/**
 * La tarjeta que muestra una entrada anterior en la lista.
 */
@Composable
fun JournalLogCard(
    entry: JournalEntity,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            // El color de fondo de la tarjeta coincide con el papel guardado
            containerColor = Color(android.graphics.Color.parseColor(entry.colorHex)).copy(alpha = 0.6f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // <-- Permite editar al hacer clic
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Bookmark, contentDescription = "Entrada", tint = GrayMedium, modifier = Modifier.padding(end = 12.dp))

            // Columna para Fecha y Contenido
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = formatTimestamp(entry.date),
                    fontWeight = FontWeight.Bold,
                    color = GrayDark,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = entry.content,
                    color = GrayMedium,
                    fontSize = 14.sp,
                    maxLines = 1, // Muestra solo la primera línea
                    overflow = TextOverflow.Ellipsis // Añade "..." si es muy largo
                )
            }
        }
    }
}


// --- Funciones Helper ---

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

private fun formatTimestamp(timestamp: Long): String {
    // Formato más amigable para un diario, ej: "15 Nov, 2025 - 06:30 PM"
    val sdf = SimpleDateFormat("dd MMM, yyyy - hh:mm a", Locale("es", "ES"))
    return sdf.format(Date(timestamp))
}