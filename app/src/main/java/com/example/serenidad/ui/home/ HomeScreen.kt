package com.example.serenidad.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
// --- 1. AÑADE LA IMPORTACIÓN DEL NUEVO ICONO ---
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serenidad.R
import com.example.serenidad.data.viewmodel.AuthViewModel
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay

data class HomeItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(onNavigate: (String) -> Unit,
               authViewModel: AuthViewModel
) {

    // --- 2. AÑADE "Mi Diario" A LA LISTA ---
    val items = listOf(
        HomeItem("Estado de ánimo", Icons.Filled.Mood, "mood"),
        HomeItem("Sueño", Icons.Filled.Nightlight, "sleep"),
        HomeItem("Estrés", Icons.Filled.Favorite, "stress"),
        HomeItem("Actividades", Icons.Filled.SelfImprovement, "activities"),
        HomeItem("Relajación", Icons.Filled.Spa, "relax"),
        HomeItem("Mi Diario", Icons.Filled.Book, "journal"),
        HomeItem("Recursos de Ayuda", Icons.Filled.HelpOutline, "help")
    )

    //Carrusel de imágenes + frases sobrepuestas
    val carrusel = listOf(
        Pair(R.drawable.relax_1, "Respira profundo, todo está bien."),
        Pair(R.drawable.relax_2, "Calma tu mente, nutre tu alma."),
        Pair(R.drawable.relax_3, "Eres suficiente, tal como eres."),
        Pair(R.drawable.relax_4, "Cada día es una nueva oportunidad."),
        Pair(R.drawable.relax_5, "Está bien no estar bien."),
        Pair(R.drawable.relax_6, "La amistad es tan importante como el amor."),
        Pair(R.drawable.relax_7, "No importa lo que digan los demás, vive la vida que quieres vivir."),
        Pair(R.drawable.relax_8, "No es egoísta priorizar tu salud mental."),
        Pair(R.drawable.relax_9, "Pedir ayuda no es un símbolo de debilidad, sino de fortaleza."),
        Pair(R.drawable.relax_10, "La vida es una cosa momentánea, vienes y te vas...")
    )

    val pagerState = rememberPagerState(initialPage = 0)

    // Animación automática cada 7 segundos
    LaunchedEffect(Unit) {
        while (true) {
            delay(8000)
            val nextPage = (pagerState.currentPage + 1) % carrusel.size
            pagerState.animateScrollToPage(nextPage)
        }
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
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.serenidad_logo),
                contentDescription = "Logo Serenidad",
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Título
            Text(
                text = "Serenidad",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF8B5CF6),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            //Carrusel con frases sobre las imágenes
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    HorizontalPager(
                        count = carrusel.size,
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val (imagen, frase) = carrusel[page]

                        Box(modifier = Modifier.fillMaxSize()) {
                            // Imagen de fondo
                            Image(
                                painter = painterResource(id = imagen),
                                contentDescription = "Imagen relajante",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Gradiente translúcido para texto
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Black.copy(alpha = 0.1f),
                                                Color(0xFF8B5CF6).copy(alpha = 0.4f)
                                            )
                                        )
                                    )
                            )

                            // Frase sobrepuesta
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp)
                            ) {
                                Text(
                                    text = frase,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }

                    // Indicadores del carrusel
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp)
                    ) {
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            activeColor = Color(0xFF8B5CF6),
                            inactiveColor = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
// 🔐 BOTÓN EXCLUSIVO PARA ADMIN
            val currentUser = authViewModel.currentUser.collectAsState().value

            if (currentUser?.isAdmin == true) {
                Button(
                    onClick = { onNavigate("admin_users") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B5CF6),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(50.dp)
                ) {
                    Text("Administrar Usuarios")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Grid de opciones
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items) { item ->
                    HomeCard(item = item, onClick = { onNavigate(item.route) })
                }
            }
        }
    }
}

@Composable
fun HomeCard(item: HomeItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = Color(0xFF8B5CF6),
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF6A1B9A),
                textAlign = TextAlign.Center
            )
        }
    }
}