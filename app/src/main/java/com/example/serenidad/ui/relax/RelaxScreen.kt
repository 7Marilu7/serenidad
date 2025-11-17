package com.example.serenidad.ui.relax

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
// --- Importaciones de iconos necesarias ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Waves
// ------------------------------------------
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

private val GrayMedium = Color(0xFF562B8D)
private val GrayDark = Color(0xFF424242)
private val LavenderAccentLight = Color(0xFFEFE6FF)
private val GlassBackground = Color(0x30FFFFFF)
private val GlassBorder = Color(0x50FFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelaxScreen(title: String = "Relajación y Respiración") {

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
                    .border(
                        1.dp,
                        GlassBorder,
                        RoundedCornerShape(24.dp)
                    )
                    .padding(16.dp),
            ) {

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
                        "Técnicas basadas en la ciencia para calmar tu sistema nervioso.",
                        color = GrayMedium,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                }

                item {
                    CategoryHeader(
                        title = "Ejercicios de respiración",
                        icon = Icons.Default.Air
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    RelaxSectionCard(
                        title = "Respiración 4-7-8",
                        description = "1. Inhala por la nariz contando 4 segundos.\n" +
                                "\n" +
                                "2. Mantén la respiración 7 segundos.\n" +
                                "\n" +
                                "3. Exhala lentamente por la boca durante 8 segundos.\n" +
                                "\n" +
                                "4. Repite el ciclo 4 veces (o más, según necesidad).",
                        icon = Icons.Default.BubbleChart
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    RelaxSectionCard(
                        title = "Respiración diafragmática",
                        description = "1. Sentado o acostado, coloca una mano en el abdomen y otra en el pecho.\n" +
                                "\n" +
                                "2. Inhala lentamente por la nariz, sintiendo que el abdomen se eleva mientras el pecho permanece casi quieto.\n" +
                                "\n" +
                                "3. Exhala despacio por la boca, notando cómo el abdomen desciende.\n" +
                                "\n" +
                                "4. Repite durante varios minutos, concentrándote en la respiración profunda y relajante.",
                        icon = Icons.Default.Waves
                    )
                    Spacer(Modifier.height(30.dp))
                }
                item {
                    CategoryHeader(
                        title = "Atención plena (Mindfulness)",
                        icon = Icons.Default.Spa
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    RelaxSectionCard(
                        title = "Escaneo corporal (Body Scan)",
                        description = "1. Acuéstate o siéntate cómodamente.\n" +
                                "\n" +
                                "2. Cierra los ojos y lleva tu atención a los pies, notando cualquier sensación (calor, tensión, cosquilleo).\n" +
                                "\n" +
                                "3. Lentamente sube la atención por piernas, abdomen, pecho, brazos, cuello y cabeza, percibiendo cada área con plena conciencia.\n" +
                                "\n" +
                                "4. Mantén la atención en cada sensación unos segundos y sigue sin apresurarte.\n" +
                                "\n" +
                                "5. Termina respirando profundamente y abriendo los ojos lentamente.",
                        icon = Icons.Default.AccessibilityNew
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    RelaxSectionCard(
                        title = "Mindfulness de la respiración",
                        description = "1. Siéntate o acuéstate cómodamente y cierra los ojos.\n" +
                                "\n" +
                                "2. Lleva la atención a la entrada y salida del aire por la nariz o al movimiento del abdomen al respirar.\n" +
                                "\n" +
                                "3. Observa la respiración tal como es, notando ritmo, profundidad y sensación.\n" +
                                "\n" +
                                "4. Cuando la mente divague, suavemente regresa la atención a la respiración.\n" +
                                "\n" +
                                "5. Repite durante varios minutos.",
                        icon = Icons.Default.SelfImprovement
                    )
                    Spacer(Modifier.height(30.dp))
                }

                item {
                    CategoryHeader(
                        title = "Técnicas de anclaje (Grounding)",
                        icon = Icons.Default.Visibility
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    RelaxSectionCard(
                        title = "Técnica Sensorial 5-4-3-2-1",
                        description = "Menciona: 5 cosas que puedes ver: observa detalles a tu alrededor.\n" +
                                "\n" +
                                "4 cosas que puedes tocar: siente texturas u objetos cercanos.\n" +
                                "\n" +
                                "3 cosas que puedes escuchar: identifica sonidos en el entorno.\n" +
                                "\n" +
                                "2 cosas que puedes oler: percibe aromas presentes.\n" +
                                "\n" +
                                "1 cosa que puedes saborear: nota un sabor o simplemente concéntrate en tu boca.",
                        icon = Icons.Default.Visibility
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    RelaxSectionCard(
                        title = "Anclaje por texturas",
                        description = "1. Escoge un objeto con una textura interesante (por ejemplo, una piedra, tela, pelota antiestrés).\n" +
                                "\n" +
                                "2. Sostén el objeto y concéntrate en cómo se siente: rugoso, suave, frío, cálido, áspero, etc.\n" +
                                "\n" +
                                "3. Explora la textura con los dedos, sintiendo cada detalle y manteniendo la atención plena en la sensación.\n" +
                                "\n" +
                                "4. Si la mente se distrae, suavemente vuelve al tacto del objeto.",
                        icon = Icons.Default.TouchApp
                    )
                    Spacer(Modifier.height(30.dp))
                }

                item {
                    CategoryHeader(
                        title = "Visualización y cognición",
                        icon = Icons.Default.Landscape
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    RelaxSectionCard(
                        title = "Visualización de lugar Seguro",
                        description = "1. Siéntate o recuéstate en un lugar cómodo y cierra los ojos.\n" +
                                "\n" +
                                "2. Imagina un lugar donde te sientas completamente seguro y relajado (real o imaginario).\n" +
                                "\n" +
                                "3. Visualiza los detalles: colores, sonidos, olores, texturas y cualquier elemento que haga sentir tranquilidad.\n" +
                                "\n" +
                                "4. Sumérgete en la experiencia, respirando lenta y profundamente mientras exploras mentalmente tu lugar seguro.\n" +
                                "\n" +
                                "5.  Permanece allí unos minutos hasta sentir calma, luego abre los ojos suavemente.",
                        icon = Icons.Default.Landscape
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    RelaxSectionCard(
                        title = "Práctica de gratitud",
                        description = "1. Dedica unos minutos al día a reflexionar sobre cosas, personas o experiencias por las que te sientes agradecido.\n" +
                                "\n" +
                                "2. Puedes escribirlas en un diario o simplemente pensarlas en silencio, notando cómo te hacen sentir.\n" +
                                "\n" +
                                "3.  específico y enfócate en los detalles que hacen que esa experiencia sea significativa.\n" +
                                "\n" +
                                "4. Mantén la atención en la sensación de gratitud y bienestar que surge.",
                        icon = Icons.Default.FavoriteBorder
                    )
                    Spacer(Modifier.height(30.dp))
                }

                item {
                    CategoryHeader(
                        title = "Sonidos Relajantes",
                        icon = Icons.Default.MusicNote // Icono para la categoría
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    RelaxSectionCard(
                        title = "Biblioteca de Sonidos",
                        description = "Paisajes sonoros (lluvia, olas del mar, bosque, ruido blanco) diseñados para promover un estado de calma.",
                        icon = Icons.Default.MusicNote
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

/**
 * NUEVO Composable para la cabecera de cada categoría.
 */
@Composable
fun CategoryHeader(title: String, icon: ImageVector) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp) // Un ligero padding
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = GrayMedium, // Color morado
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = GrayDark // Color oscuro
            )
        }
        Spacer(Modifier.height(4.dp))
        Divider(color = LavenderAccentLight, thickness = 1.dp) // Un divisor sutil
    }
}


/**
 * Composable reutilizable para mostrar cada técnica de relajación.
 * (Este no se modifica)
 */
@Composable
fun RelaxSectionCard(
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
                    lineHeight = 20.sp // Espacio entre líneas para mejor lectura
                )
            }
        }
    }
}