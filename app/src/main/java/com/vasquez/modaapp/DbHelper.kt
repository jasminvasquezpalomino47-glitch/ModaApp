package com.vasquez.modaapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ModaApp.db"
        private const val DATABASE_VERSION = 2

        // Tabla Ropa
        const val TABLE_ROPA = "ropa"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_CATEGORIA = "categoria"
        const val COLUMN_PRECIO = "precio"
        const val COLUMN_STOCK = "stock"
        const val COLUMN_IMAGEN_URI = "imagen_uri"

        // Tabla Clientes
        const val TABLE_CLIENTES = "clientes"
        const val COLUMN_CLIENTE_ID = "id"
        const val COLUMN_CLIENTE_DNI = "dni"
        const val COLUMN_CLIENTE_NOMBRE = "nombre"
        const val COLUMN_CLIENTE_TELEFONO = "telefono"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Creación tabla Ropa
        val createTableRopa = ("CREATE TABLE " + TABLE_ROPA + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOMBRE + " TEXT NOT NULL, "
                + COLUMN_CATEGORIA + " TEXT, "
                + COLUMN_PRECIO + " REAL NOT NULL, "
                + COLUMN_STOCK + " INTEGER NOT NULL, "
                + COLUMN_IMAGEN_URI + " TEXT)")
        db.execSQL(createTableRopa)

        // Creación tabla Clientes
        val createTableClientes = ("CREATE TABLE " + TABLE_CLIENTES + " ("
                + COLUMN_CLIENTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CLIENTE_DNI + " TEXT NOT NULL, "
                + COLUMN_CLIENTE_NOMBRE + " TEXT NOT NULL, "
                + COLUMN_CLIENTE_TELEFONO + " TEXT)")
        db.execSQL(createTableClientes)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROPA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTES")
        onCreate(db)
    }

    // --- MÉTODOS PARA ROPA ---

    // Registra una nueva prenda en la base de datos local
    fun insertarRopa(nombre: String, categoria: String, precio: Double, stock: Int, imagenUri: String?): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_CATEGORIA, categoria)
            put(COLUMN_PRECIO, precio)
            put(COLUMN_STOCK, stock)
            put(COLUMN_IMAGEN_URI, imagenUri)
        }
        val result = db.insert(TABLE_ROPA, null, values)
        db.close()
        return result
    }

    // Método para obtener todas las prendas registradas
    fun obtenerTodasLasPrendas(): List<Map<String, Any>> {
        val lista = mutableListOf<Map<String, Any>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ROPA ORDER BY $COLUMN_ID DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val mapa = mapOf(
                    "id" to cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    "nombre" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                    "categoria" to (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORIA)) ?: ""),
                    "precio" to cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRECIO)),
                    "stock" to cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK)),
                    "imagenUri" to (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGEN_URI)) ?: "")
                )
                lista.add(mapa)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    // --- MÉTODOS PARA CLIENTES ---

    // Registrar nuevo cliente
    fun insertarCliente(dni: String, nombre: String, telefono: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CLIENTE_DNI, dni)
            put(COLUMN_CLIENTE_NOMBRE, nombre)
            put(COLUMN_CLIENTE_TELEFONO, telefono)
        }
        val result = db.insert(TABLE_CLIENTES, null, values)
        db.close()
        return result
    }

    // Obtener lista de todos los clientes
    fun obtenerTodosLosClientes(): List<Map<String, Any>> {
        val lista = mutableListOf<Map<String, Any>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CLIENTES ORDER BY $COLUMN_CLIENTE_ID DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val mapa = mapOf(
                    "id" to cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_ID)),
                    "dni" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_DNI)),
                    "nombre" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_NOMBRE)),
                    "telefono" to (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_TELEFONO)) ?: "")
                )
                lista.add(mapa)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
}