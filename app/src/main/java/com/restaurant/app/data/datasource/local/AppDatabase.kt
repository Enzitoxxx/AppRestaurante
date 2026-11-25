package com.restaurant.app.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.restaurant.app.data.datasource.local.dao.ProductoDao
import com.restaurant.app.data.datasource.local.dao.UsuarioDao
import com.restaurant.app.data.datasource.local.entity.ProductoEntity
import com.restaurant.app.data.datasource.local.entity.UsuarioEntity

/*
 * =========================================================================================================
 * RESUMEN DE ARQUITECTURA ROOM (Capa data/datasource/local):
 * =========================================================================================================
 *  Room es la librería de persistencia oficial de Jetpack que actúa como una capa de abstracción
 *  sobre la base de datos nativa SQLite del dispositivo Android. Se compone de cuatro pilares principales:
 *
 *  1. Entity (ej. UsuarioEntity, ProductoEntity): Son data classes anotadas con @Entity que definen la estructura
 *     de las tablas en SQLite. Cada instancia de la Entity equivale a una fila de la tabla.
 *  2. DAO (ej. UsuarioDao, ProductoDao): Son interfaces con métodos anotados como @Query, @Insert o @Upsert
 *     donde escribimos consultas SQL tipadas. Room valida el SQL y genera el código en compilación.
 *  3. Database (AppDatabase): Es la clase abstracta que hereda de RoomDatabase. Funciona como el punto de acceso
 *     central a las entidades y expone los DAOs.
 *  4. RoomDatabase.Callback: Permite ejecutar código (ej. `cargarDatosIniciales`) UNA SOLA VEZ cuando el
 *     archivo SQLite se crea por primera vez en el dispositivo.
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
    version = 2,
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
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Se ejecuta únicamente la primera vez que el archivo SQLite se crea en el celular
                            cargarDatosIniciales(db)
                        }
                    })
                    .build()
                INSTANCE = instancia
                instancia
            }
        }
    }
}
