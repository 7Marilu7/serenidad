package com.example.serenidad

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.serenidad.ui.auth.LoginScreen
import com.example.serenidad.ui.auth.RegisterScreen
import com.example.serenidad.ui.home.HomeScreen
import com.example.serenidad.ui.mood.MoodScreen
import com.example.serenidad.ui.sleep.SleepScreen
import com.example.serenidad.ui.stress.StressScreen
import com.example.serenidad.ui.activities.ActivitiesScreen
import com.example.serenidad.ui.relax.RelaxScreen
import com.example.serenidad.ui.welcome.WelcomeScreen
import com.example.serenidad.ui.theme.SerenidadTheme

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serenidad.data.viewmodel.AuthViewModel
import com.example.serenidad.ui.admin.UsersListScreen
import com.example.serenidad.ui.journal.JournalScreen
import com.example.serenidad.ui.help.HelpScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SerenidadTheme {
                SerenidadApp()
            }
        }
    }
}

@Composable
fun SerenidadApp() {
    MaterialTheme {
        Surface {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()

            NavHost(
                navController = navController,
                startDestination = "welcome"
            ) {
                composable("welcome") {
                    WelcomeScreen(
                        onContinue = { navController.navigate("auth_login") }
                    )
                }

                composable("auth_login") {
                    LoginScreen(
                        onLogin = {
                            navController.navigate("home") {
                                popUpTo("auth_login") { inclusive = true }
                            }
                        },
                        onRegister = { navController.navigate("auth_register") },
                        authViewModel = authViewModel
                    )
                }

                composable("auth_register") {
                    RegisterScreen(
                        onRegistered = { navController.popBackStack() },
                        authViewModel = authViewModel
                    )
                }

                composable("home") {
                    HomeScreen(
                        onNavigate = { route ->
                            navController.navigate(route)
                        },
                        authViewModel = authViewModel
                    )
                }

                composable("mood") {
                    MoodScreen(
                        title = "Estado de ánimo",
                        authViewModel = authViewModel
                    )
                }

                composable("sleep") {
                    SleepScreen(
                        title = "Sueño",
                        authViewModel = authViewModel
                    )
                }

                composable("stress") {
                    StressScreen(
                        title = "Estrés",
                        authViewModel = authViewModel
                    )
                }

                composable("activities") {
                    ActivitiesScreen(
                        title = "Actividades didácticas",
                        authViewModel = authViewModel
                    )
                }

                composable("relax") {
                    RelaxScreen(title = "Ejercicios de relajación")
                }

                composable("journal") {
                    JournalScreen(
                        title = "Mi Diario",
                        authViewModel = authViewModel
                    )
                }

                composable("help") {
                    HelpScreen(title = "Recursos de Ayuda")
                }

                // ADMIN SCREEN
                composable("admin_users") {

                    val context = LocalContext.current   // 🔥 NECESARIO AQUÍ
                    val currentUser = authViewModel.currentUser.collectAsState().value

                    if (currentUser?.isAdmin == true) {
                        UsersListScreen(
                            authViewModel = authViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Acceso solo para administradores",
                            Toast.LENGTH_SHORT
                        ).show()

                        navController.popBackStack()
                    }
                }
            }
        }
    }
}