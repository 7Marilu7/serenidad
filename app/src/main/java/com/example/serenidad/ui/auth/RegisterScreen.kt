package com.example.serenidad.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serenidad.R
import com.example.serenidad.data.viewmodel.AuthViewModel

// ✔ VALIDACIÓN REAL DE EMAIL
fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

// ✔ VALIDACIÓN DE CONTRASEÑA
fun isValidPassword(password: String): Boolean = password.length >= 8

@Composable
fun RegisterScreen(
    onRegistered: () -> Unit,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var registerSuccess by remember { mutableStateOf<Boolean?>(null) }

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
            .padding(24.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            Image(
                painter = painterResource(id = R.drawable.serenidad_logo),
                contentDescription = "Logo Serenidad",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Crear cuenta",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF8B5CF6)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🌼 EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                    registerSuccess = null
                },
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFB794F6),
                    unfocusedBorderColor = Color(0xFFD1C4E9)
                )
            )

            if (email.isNotEmpty() && !isValidEmail(email)) {
                Text(
                    text = "Correo inválido. Formato correcto: usuario@dominio.com",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔑 CONTRASEÑA
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                    registerSuccess = null
                },
                label = { Text("Contraseña (mínimo 8 caracteres)") },
                singleLine = true,
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painterResource(
                                id = if (passwordVisible)
                                    R.drawable.ic_visibility_off
                                else
                                    R.drawable.ic_visibility
                            ),
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFB794F6),
                    unfocusedBorderColor = Color(0xFFD1C4E9)
                )
            )

            if (password.isNotEmpty() && !isValidPassword(password)) {
                Text(
                    text = "La contraseña debe tener mínimo 8 caracteres",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 🌈 BOTÓN REGISTRAR
            Button(
                onClick = {
                    errorMessage = null

                    if (!isValidEmail(email)) {
                        errorMessage = "Correo inválido"
                        registerSuccess = false
                        return@Button
                    }
                    if (!isValidPassword(password)) {
                        errorMessage = "La contraseña debe tener mínimo 8 caracteres"
                        registerSuccess = false
                        return@Button
                    }

                    authViewModel.registerUser(email, password) { success, errorMsg ->
                        registerSuccess = success
                        if (!success) {
                            errorMessage = errorMsg ?: "Error desconocido"
                        } else {
                            onRegistered()
                        }
                    }
                },
                enabled = email.isNotEmpty() && password.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFB794F6),
                                    Color(0xFFD8B4FE)
                                )
                            ),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Registrar",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ❗ MENSAJES DE ERROR / ÉXITO
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            if (registerSuccess == true) {
                Text(
                    text = "Registro exitoso 🎉",
                    color = Color(0xFF7C4DFF),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}