package com.vasquez.modaapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ModaApp.db"
        private const val DATABASE_VERSION = 3

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

        // Tabla Pedidos / Ventas
        const val TABLE_PEDIDOS = "pedidos"
        const val COLUMN_PEDIDO_ID = "id"
        const val COLUMN_PEDIDO_CLIENTE = "cliente_nombre"
        const val COLUMN_PEDIDO_PRENDA = "prenda_nombre"
        const val COLUMN_PEDIDO_CANTIDAD = "cantidad"
        const val COLUMN_PEDIDO_TOTAL = "total"
        const val COLUMN_PEDIDO_FECHA = "fecha"
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

        // Creación tabla Pedidos
        val createTablePedidos = ("CREATE TABLE " + TABLE_PEDIDOS + " ("
                + COLUMN_PEDIDO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PEDIDO_CLIENTE + " TEXT NOT NULL, "
                + COLUMN_PEDIDO_PRENDA + " TEXT NOT NULL, "
                + COLUMN_PEDIDO_CANTIDAD + " INTEGER NOT NULL, "
                + COLUMN_PEDIDO_TOTAL + " REAL NOT NULL, "
                + COLUMN_PEDIDO_FECHA + " TEXT NOT NULL)")
        db.execSQL(createTablePedidos)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROPA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PEDIDOS")
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

    // --- MÉTODOS PARA PEDIDOS / VENTAS ---

    // Registrar nuevo pedido
    fun insertarPedido(clienteNombre: String, prendaNombre: String, cantidad: Int, total: Double, fecha: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PEDIDO_CLIENTE, clienteNombre)
            put(COLUMN_PEDIDO_PRENDA, prendaNombre)
            put(COLUMN_PEDIDO_CANTIDAD, cantidad)
            put(COLUMN_PEDIDO_TOTAL, total)
            put(COLUMN_PEDIDO_FECHA, fecha)
        }
        val result = db.insert(TABLE_PEDIDOS, null, values)
        db.close()
        return result
    }

    // Obtener lista de todos los pedidos registrados
    fun obtenerTodosLosPedidos(): List<Map<String, Any>> {
        val lista = mutableListOf<Map<String, Any>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PEDIDOS ORDER BY $COLUMN_PEDIDO_ID DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val mapa = mapOf(
                    "id" to cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PEDIDO_ID)),
                    "cliente" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PEDIDO_CLIENTE)),
                    "prenda" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PEDIDO_PRENDA)),
                    "cantidad" to cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PEDIDO_CANTIDAD)),
                    "total" to cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PEDIDO_TOTAL)),
                    "fecha" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PEDIDO_FECHA))
                )
                lista.add(mapa)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    // --- MÉTODOS PARA REPORTES ---

    // Obtener la suma total de dinero vendido en los pedidos
    fun obtenerTotalVendido(): Double {
        var total = 0.0
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_PEDIDO_TOTAL) FROM $TABLE_PEDIDOS", null)
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }
        cursor.close()
        db.close()
        return total
    }

    // Obtener cantidad total de pedidos atendidos
    fun obtenerCantidadPedidos(): Int {
        var cantidad = 0
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_PEDIDOS", null)
        if (cursor.moveToFirst()) {
            cantidad = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return cantidad
    }

    // Obtener lista de ropa con su stock para las barras del reporte
    fun obtenerStockPrendas(): List<Map<String, Any>> {
        val lista = mutableListOf<Map<String, Any>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_NOMBRE, $COLUMN_STOCK FROM $TABLE_ROPA ORDER BY $COLUMN_STOCK DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val mapa = mapOf(
                    "nombre" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                    "stock" to cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK))
                )
                lista.add(mapa)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
}