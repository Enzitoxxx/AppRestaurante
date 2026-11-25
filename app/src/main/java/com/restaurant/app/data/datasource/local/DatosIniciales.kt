package com.restaurant.app.data.datasource.local

import androidx.sqlite.db.SupportSQLiteDatabase
import com.restaurant.app.data.datasource.local.entity.UsuarioEntity

/*
 * DATOS INICIALES DE DEMOSTRACIÓN (Capa data/datasource/local).
 *
 * Este archivo se utiliza únicamente para cargar información inicial de prueba en SQLite
 * la primera vez que se crea la base de datos en el dispositivo (mediante el callback `onCreate`).
 * Más adelante, en un entorno de producción, la información inicial provendrá directamente
 * de la API remota (Retrofit).
 */
fun cargarDatosIniciales(db: SupportSQLiteDatabase) {
    // 1. Usuarios iniciales de prueba (Lucía Fernández e Usuario Inactivo)
    val sqlUsuario = """
        INSERT INTO usuarios (id, nombre, correo, telefono, restauranteNombre, estaActivo, contrasenaHash)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """.trimIndent()

    val hashMesa1234 = UsuarioEntity.generarHash("Mesa1234")

    db.execSQL(
        sqlUsuario,
        arrayOf<Any?>(
            "1",
            "Lucía Fernández",
            "lucia@labuenamesa.com.ar",
            "+54 9 11 1234-5678",
            "La Buena Mesa",
            1, // estaActivo = true (1 en SQLite)
            hashMesa1234
        )
    )

    db.execSQL(
        sqlUsuario,
        arrayOf<Any?>(
            "2",
            "Usuario Inactivo",
            "inactivo@labuenamesa.com.ar",
            "+54 9 11 0000-0000",
            "La Buena Mesa",
            0, // estaActivo = false (0 en SQLite)
            hashMesa1234
        )
    )

    // 2. Productos iniciales de prueba para la Carta
    val sqlProducto = """
        INSERT INTO productos (id, nombre, categoria, area, precio, estadoDisponible, estadoTexto, descripcion, estaDadoDeBaja)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """.trimIndent()

    db.execSQL(
        sqlProducto,
        arrayOf<Any?>(
            "1",
            "Empanadas de carne",
            "Entradas",
            "Cocina",
            2200.0,
            1, // estadoDisponible = true
            null, // estadoTexto
            "Carne cortada a cuchillo con cebolla de verdeo, huevo y aceitunas.",
            0 // estaDadoDeBaja = false
        )
    )

    db.execSQL(
        sqlProducto,
        arrayOf<Any?>(
            "2",
            "Bife de chorizo con papas",
            "Platos principales",
            "Cocina",
            14800.0,
            1,
            null,
            "Carne de novillo a la parrilla con papas fritas provenzal.",
            0
        )
    )

    db.execSQL(
        sqlProducto,
        arrayOf<Any?>(
            "3",
            "Cerveza IPA Artesanal",
            "Bebidas",
            "Bar",
            3500.0,
            0, // estadoDisponible = false
            "Pausado por stock",
            "Cerveza artesanal tirada de 500cc.",
            0
        )
    )

    db.execSQL(
        sqlProducto,
        arrayOf<Any?>(
            "4",
            "Flan casero mixto",
            "Postres",
            "Cocina",
            3200.0,
            1,
            null,
            "Receta tradicional con dulce de leche y crema.",
            0
        )
    )
}
