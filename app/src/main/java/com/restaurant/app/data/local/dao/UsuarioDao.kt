package com.restaurant.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.restaurant.app.data.local.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

/*
 * DAO (Data Access Object) PARA LA TABLA DE USUARIOS (Capa data/local).
 *
 * Un DAO es una interfaz donde declaramos las consultas SQL de forma tipada y segura mediante anotaciones de Room.
 * Room genera automáticamente en tiempo de compilación el código de implementación que interactúa con SQLite.
 */
@Dao
interface UsuarioDao {

    // Busca un usuario por su correo electrónico (útil para inicio de sesión y validación de duplicados)
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): UsuarioEntity?

    // Busca un usuario por su ID único
    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: String): UsuarioEntity?

    // Inserta un nuevo usuario. Con OnConflictStrategy.ABORT, si el correo (único) ya existe, Room lanza una excepción.
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: UsuarioEntity)

    // Observa en tiempo real los datos del usuario logueado mediante un Flow reactivo.
    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    fun observarPorId(id: String): Flow<UsuarioEntity?>

    // Cuenta la cantidad total de usuarios registrados en la base local (para verificar si hace falta insertar usuarios iniciales).
    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int
}
