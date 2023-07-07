package com.example.jobmanager.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.jobmanager.Fichaje
import com.example.jobmanager.Oficina
import com.example.jobmanager.User
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHandler constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserDatabase"
        private const val TABLE_USERS = "Users"
        private const val TABLE_FICHAJES = "Fichajes"
        private const val TABLE_OFICINAS = "Oficinas"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_OFICINA = "oficina"
        private const val KEY_FECHA_ENTRADA = "fecha_entrada"
        private const val KEY_FECHA_SALIDA = "fecha_salida"
        private const val KEY_HORAS_TRABAJADAS = "horas_trabajadas"
        private const val KEY_OCUPADA = "ocupada"
        private const val KEY_IS_ADMIN = "is_admin"

        private var instance: DatabaseHandler? = null
        private var currentUser: User? = null

        fun getInstance(context: Context): DatabaseHandler {
            if (instance == null) {
                instance = DatabaseHandler(context.applicationContext)

            }
            return instance as DatabaseHandler
        }
    }
    fun setCurrentUser(User: User) {
        currentUser = User
    }
    fun getCurrentUser(): User? {
        return currentUser
    }


    fun logoutUser() {
        currentUser = null
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTableQuery =
            "CREATE TABLE $TABLE_USERS ($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_USERNAME TEXT, $KEY_PASSWORD TEXT, $KEY_IS_ADMIN INTEGER)"
        db.execSQL(createUserTableQuery)

        val createFichajeTableQuery =
            "CREATE TABLE $TABLE_FICHAJES ($KEY_ID INTEGER PRIMARY KEY, $KEY_USERNAME TEXT, $KEY_OFICINA TEXT, $KEY_FECHA_ENTRADA TEXT, $KEY_FECHA_SALIDA TEXT, $KEY_HORAS_TRABAJADAS REAL)"
        db.execSQL(createFichajeTableQuery)

        val createOficinasTableQuery =
            "CREATE TABLE IF NOT EXISTS $TABLE_OFICINAS ($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_OCUPADA INTEGER)"
        db.execSQL(createOficinasTableQuery)


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FICHAJES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_OFICINAS")
        onCreate(db)
    }



    fun addUser(user: User): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, user.name)
        values.put(KEY_USERNAME, user.username)
        values.put(KEY_PASSWORD, user.password)
        values.put(KEY_IS_ADMIN, if (user.isAdmin) 1 else 0)
        val id = db.insert(TABLE_USERS, null, values)
        db.close()
        return id
    }

    fun getUser(username: String): User? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $KEY_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val user: User? = if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            val name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
            val password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD))
            val isAdmin = cursor.getInt(cursor.getColumnIndex(KEY_IS_ADMIN)) == 1
            User(id, name, username, password, isAdmin)
        } else {
            null
        }
        cursor.close()
        db.close()
        return user
    }

    fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            val name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
            val username = cursor.getString(cursor.getColumnIndex(KEY_USERNAME))
            val password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD))
            val isAdmin = cursor.getInt(cursor.getColumnIndex(KEY_IS_ADMIN)) == 1 // Se obtiene el valor de isAdmin
            val user = User(id, name, username, password, isAdmin)
            users.add(user)
        }
        cursor.close()
        db.close()
        return users
    }

    fun isUserExists(username: String): Boolean {
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_USERS WHERE $KEY_USERNAME = ?"
        val cursor: Cursor = db.rawQuery(selectQuery, arrayOf(username))
        val userExists = cursor.count > 0
        cursor.close()
        db.close()
        return userExists
    }

    fun saveUser(user: User) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, user.name)
        contentValues.put(KEY_USERNAME, user.username)
        contentValues.put(KEY_PASSWORD, user.password)
        db.update(
            TABLE_USERS,
            contentValues,
            "$KEY_ID = ?",
            arrayOf(user.id.toString())
        )
        db.close()
    }

    fun deleteUser(user: User) {
        val db = writableDatabase
        db.delete(
            TABLE_USERS,
            "$KEY_ID = ?",
            arrayOf(user.id.toString())
        )
        db.close()
    }

    fun addFichaje(username: String, oficina: String, fechaEntrada: String): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_USERNAME, username)
        values.put(KEY_OFICINA, oficina)
        values.put(KEY_FECHA_ENTRADA, fechaEntrada)
        val id = db.insert(TABLE_FICHAJES, null, values)
        //db.close()
        return id
    }

    fun updateFichaje(fichaje: Fichaje) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_FECHA_SALIDA, fichaje.fechaSalida)
        values.put(KEY_HORAS_TRABAJADAS, fichaje.horasTrabajadas)
        db.update(
            TABLE_FICHAJES,
            values,
            "$KEY_ID = ?",
            arrayOf(fichaje.id.toString())
        )
        db.close()
    }
    fun calcularHorasTrabajadas(fechaEntrada: String, fechaSalida: String): Double {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dateEntrada = format.parse(fechaEntrada)
        val dateSalida = format.parse(fechaSalida)
        val diff = dateSalida.time - dateEntrada.time
        val horasTrabajadas = diff.toDouble() / (1000 * 60 * 60)
        return horasTrabajadas
    }

    fun verFichajes(): List<Fichaje> {
        val fichajes = mutableListOf<Fichaje>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_FICHAJES"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            val username = cursor.getString(cursor.getColumnIndex(KEY_USERNAME))
            val oficina = cursor.getString(cursor.getColumnIndex(KEY_OFICINA))
            val fechaEntrada = cursor.getString(cursor.getColumnIndex(KEY_FECHA_ENTRADA))
            val fechaSalida = cursor.getString(cursor.getColumnIndex(KEY_FECHA_SALIDA))
            val horasTrabajadas = cursor.getDouble(cursor.getColumnIndex(KEY_HORAS_TRABAJADAS))

            val fichaje = Fichaje(id, username, oficina, fechaEntrada, fechaSalida, horasTrabajadas)
            fichajes.add(fichaje)
        }
        cursor.close()
        db.close()
        return fichajes
    }
    fun getFichaje(id: Int): Fichaje? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_FICHAJES WHERE $KEY_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        val fichaje: Fichaje? = if (cursor.moveToFirst()) {
            val username = cursor.getString(cursor.getColumnIndex(KEY_USERNAME))
            val oficina = cursor.getString(cursor.getColumnIndex(KEY_OFICINA))
            val fechaEntrada = cursor.getString(cursor.getColumnIndex(KEY_FECHA_ENTRADA))
            val fechaSalida = cursor.getString(cursor.getColumnIndex(KEY_FECHA_SALIDA))
            val horasTrabajadas = cursor.getDouble(cursor.getColumnIndex(KEY_HORAS_TRABAJADAS))

            Fichaje(id, username, oficina, fechaEntrada, fechaSalida, horasTrabajadas)
        } else {
            null
        }
        cursor.close()
        //db.close()
        return fichaje
    }

    fun getFichajesByUser(user: User): List<Fichaje> {
        val fichajes = mutableListOf<Fichaje>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_FICHAJES WHERE $KEY_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(user.username))
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            val oficina = cursor.getString(cursor.getColumnIndex(KEY_OFICINA))
            val fechaEntrada = cursor.getString(cursor.getColumnIndex(KEY_FECHA_ENTRADA))
            val fechaSalida = cursor.getString(cursor.getColumnIndex(KEY_FECHA_SALIDA))
            val horasTrabajadas = cursor.getDouble(cursor.getColumnIndex(KEY_HORAS_TRABAJADAS))
            val fichaje = Fichaje(id, user.username, oficina, fechaEntrada, fechaSalida, horasTrabajadas)
            fichajes.add(fichaje)
        }
        cursor.close()
        //db.close()
        return fichajes
    }

    fun getOficinasOcupadas(): List<Oficina> {
        val oficinasOcupadas = mutableListOf<Oficina>()
        val db = readableDatabase
        val query =
            "SELECT * FROM $TABLE_OFICINAS  WHERE $KEY_OCUPADA = 1"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val oficinaId = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            val oficinaNombre = cursor.getString(cursor.getColumnIndex(KEY_OFICINA))
            val oficina = Oficina(oficinaId, oficinaNombre, true)
            oficinasOcupadas.add(oficina)
        }
        cursor.close()
        //db.close()
        return oficinasOcupadas
    }

    fun getOficinasDisponibles(): List<Oficina> {
        val oficinasDisponibles = mutableListOf<Oficina>()
        val db = readableDatabase
        val query =
            "SELECT * FROM $TABLE_OFICINAS WHERE $KEY_OCUPADA = 0"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val oficinaId = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            val oficinaNombre = cursor.getString(cursor.getColumnIndex(KEY_OFICINA))
            val oficina = Oficina(oficinaId, oficinaNombre, false)
            oficinasDisponibles.add(oficina)
        }
        cursor.close()
        db.close()
        return oficinasDisponibles
    }
    fun addOficina(oficina: Oficina): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, oficina.nombre)
        values.put(KEY_OCUPADA, if (oficina.ocupada) 1 else 0)
        val id = db.insert(TABLE_OFICINAS, null, values)
        //db.close()
        return id
    }

    fun getOficina(nombre: String): Oficina? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_OFICINAS WHERE $KEY_NAME = ?"
        val cursor = db.rawQuery(query, arrayOf(nombre))
        val oficina: Oficina? = if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            val nombreOficina = cursor.getString(cursor.getColumnIndex(KEY_NAME))
            val ocupada = cursor.getInt(cursor.getColumnIndex(KEY_OCUPADA)) == 1
            Oficina(id, nombreOficina, ocupada)
        } else {
            null
        }
        cursor.close()
        //db.close()
        return oficina
    }
    fun cambiarEstadoOficina(nombre: String) {
        val db = writableDatabase
        val oficina = getOficina(nombre)
        if (oficina != null) {
            val nuevoEstado = !oficina.ocupada
            val values = ContentValues()
            values.put(KEY_OCUPADA, if (nuevoEstado) 1 else 0)
            db.update(TABLE_OFICINAS, values, "$KEY_NAME = ?", arrayOf(nombre))
        }
        //db.close()
    }
    fun obtenerFechaHoraUltimoFichaje(oficina: String): String? {
        val db = readableDatabase
        val query =
            "SELECT $KEY_FECHA_SALIDA FROM $TABLE_FICHAJES WHERE $KEY_OFICINA = ? ORDER BY $KEY_FECHA_SALIDA DESC LIMIT 1"
        val cursor = db.rawQuery(query, arrayOf(oficina))
        val fechaHora: String? = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(KEY_FECHA_SALIDA))
        } else {
            null
        }
        cursor.close()
        //db.close()
        return fechaHora
    }

}
