//package com.example.serenidad.data.dao

//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.example.serenidad.data.model.User

//@Dao
//interface UserDao {

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //suspend fun insertUser(user: User)

    //@Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    //suspend fun getUserByEmail(email: String): User?

    //@Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    //suspend fun login(email: String, password: String): User?

    // Nuevo: obtener todos los usuarios registrados
    //@Query("SELECT * FROM users")
    //suspend fun getAllUsers(): List<User>

    //PARA ADMIN MARILUU
    //@Delete
    //suspend fun deleteUser(user: User)
//}
