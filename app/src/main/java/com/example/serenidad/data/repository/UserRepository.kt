package com.example.serenidad.data.repository

import com.example.serenidad.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // ---------------------------
    // 📌 REGISTRO DE USUARIO
    // ---------------------------
    suspend fun registerUser(
        email: String,
        password: String,
        isAdmin: Boolean = false
    ): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid
                ?: return Result.failure(Exception("No se pudo obtener el UID del usuario"))

            val user = User(
                uid = uid,
                email = email,
                isAdmin = isAdmin
            )

            firestore.collection("users")
                .document(uid)
                .set(user)
                .await()

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------------------------
    // 📌 LOGIN DE USUARIO
    // ---------------------------
    suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid
                ?: return Result.failure(Exception("UID no encontrado en Auth."))

            val doc = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            if (!doc.exists()) {
                return Result.failure(Exception("El usuario no existe en Firestore."))
            }

            val user = doc.toObject(User::class.java)
                ?: return Result.failure(Exception("Error al convertir el usuario."))

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------------------------
    // 📌 LOGOUT
    // ---------------------------
    fun logout() {
        auth.signOut()
    }

    // ---------------------------
    // 📌 OBTENER TODOS LOS USUARIOS (ADMIN)
    // ---------------------------
    suspend fun getAllUsers(): List<User> {
        return try {
            val result = firestore.collection("users").get().await()
            result.documents.mapNotNull { it.toObject(User::class.java) }

        } catch (e: Exception) {
            emptyList()
        }
    }

    // ---------------------------
    // 📌 ELIMINAR USUARIO
    // ---------------------------
    suspend fun deleteUser(uid: String): Boolean {
        return try {
            firestore.collection("users")
                .document(uid)
                .delete()
                .await()

            true

        } catch (e: Exception) {
            false
        }
    }
}