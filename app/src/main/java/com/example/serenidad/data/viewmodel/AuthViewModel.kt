package com.example.serenidad.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.serenidad.data.model.User
import com.example.serenidad.data.repository.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    // -------------------------
    // Registro de usuario
    // -------------------------
    fun registerUser(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // 1️⃣ Crear usuario en Firebase Auth
                val firebaseUser = FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .await()
                    .user ?: throw Exception("Usuario no creado")

                // 2️⃣ Crear documento en Firestore
                val userDoc = hashMapOf(
                    "uid" to firebaseUser.uid,
                    "email" to email,
                    "isAdmin" to false // Por defecto no es admin
                )

                Firebase.firestore
                    .collection("users")
                    .document(firebaseUser.uid)
                    .set(userDoc)
                    .await()

                // 3️⃣ Actualizar estado en Compose
                _currentUser.value = User(firebaseUser.uid, email, isAdmin = false)
                _isAdmin.value = false

                onResult(true, null)

            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }
    // -------------------------
    // Login Firebase
    // -------------------------
    fun loginUser(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // 1️⃣ Login en Firebase Auth
                val firebaseUser = FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .await()
                    .user ?: throw Exception("Usuario no encontrado")

                // 2️⃣ Obtener documento de Firestore
                val docSnapshot = Firebase.firestore
                    .collection("users")
                    .document(firebaseUser.uid)
                    .get()
                    .await()

                if (!docSnapshot.exists()) {
                    throw Exception("No existe el documento del usuario en Firestore")
                }

                val isAdmin = docSnapshot.getBoolean("isAdmin") ?: false
                val userEmail = docSnapshot.getString("email") ?: email

                // 3️⃣ Actualizar estados
                _currentUser.value = User(
                    uid = firebaseUser.uid,
                    email = userEmail,
                    password = password,
                    isAdmin = isAdmin
                )

                _isAdmin.value = isAdmin

                onResult(true, null)

            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    // -------------------------
    // Logout
    // -------------------------
    fun logout() {
        repository.logout()
        _currentUser.value = null
        _isAdmin.value = false
    }

    // -------------------------
    // Obtener todos los usuarios (Admin)
    // -------------------------
    fun getAllRegisteredUsers(onResult: (List<User>) -> Unit) {
        viewModelScope.launch {
            val users = repository.getAllUsers()
            onResult(users)
        }
    }

    // -------------------------
    // Eliminar usuario (Admin)
    // -------------------------
    fun deleteUser(uid: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.deleteUser(uid)
            onResult(success)
        }
    }
}