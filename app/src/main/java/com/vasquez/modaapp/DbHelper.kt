package com.vasquez.modaapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ModaApp.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_ROPA = "ropa"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_CATEGORIA = "categoria"
        const val COLUMN_PRECIO = "precio"
        const val COLUMN_STOCK = "stock"
        const val COLUMN_IMAGEN_URI = "imagen_uri"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableRopa = ("CREATE TABLE " + TABLE_ROPA + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOMBRE + " TEXT NOT NULL, "
                + COLUMN_CATEGORIA + " TEXT, "
                + COLUMN_PRECIO + " REAL NOT NULL, "
                + COLUMN_STOCK + " INTEGER NOT NULL, "
                + COLUMN_IMAGEN_URI + " TEXT)")
        db.execSQL(createTableRopa)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROPA")
        onCreate(db)
    }

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
}