package com.restaurant.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.restaurant.app.data.local.dao.ProductoDao
import com.restaurant.app.data.local.dao.UsuarioDao
import com.restaurant.app.data.local.entity.ProductoEntity
import com.restaurant.app.data.local.entity.UsuarioEntity

/*
 * =========================================================================================================
 * RESUMEN DE ARQUITECTURA ROOM:
 * =========================================================================================================
 *  Room es la librería de persistencia oficial de Jetpack que actúa como una capa de abstracción
 *  sobre la base de datos nativa SQLite del dispositivo Android. Se compone de tres pilares principales:
 *
 *  1. Entity (ej. UsuarioEntity, ProductoEntity): Son data classes anotadas con @Entity que definen la estructura
 *     de las tablas en SQLite. Cada instancia de la Entity equivale a una fila de la tabla.
 *  2. DAO (ej. UsuarioDao, ProductoDao): Son interfaces con métodos anotados como @Query, @Insert o @Upsert
 *     donde escribimos consultas SQL tipadas. Room valida el SQL y genera el código en compilación.
 *  3. Database (AppDatabase): Es la clase abstracta que hereda de RoomDatabase. Funciona como el punto de acceso
 *     central a las entidades y expone los DAOs.
 *
 *  Usamos el patrón Singleton con @Volatile y synchronized para asegurar que exista UNA SOLA instancia de la
 *  base de datos en toda la app, evitando aperturas concurrentes y optimizando el uso de memoria.
 * =========================================================================================================
 */
@Database(
    entities = [
        UsuarioEntity::class,
        ProductoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerInstancia(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "restaurante_database.db"
                )
                    /*
                     * fallbackToDestructiveMigration():
                     * En fase de desarrollo, si cambiamos el esquema de la base de datos o agregamos tablas,
                     * este método recrea la base automáticamente en lugar de requerir migraciones complejas.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instancia
                instancia
            }
        }
    }
}
