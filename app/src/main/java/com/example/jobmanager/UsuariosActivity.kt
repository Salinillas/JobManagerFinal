package com.example.jobmanager

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.jobmanager.Database.DatabaseHandler
import com.example.jobmanager.Fichaje
import com.example.jobmanager.User

class UsuariosActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oficinas)

        listView = findViewById(R.id.listView)
        databaseHandler = DatabaseHandler.getInstance(this)

        val users = databaseHandler.getAllUsers()
        val userFichajes = mutableListOf<String>()

        for (user in users) {
            val fichajes = databaseHandler.getFichajesByUser(user)
            if (fichajes.isNotEmpty()) {
                val lastFichaje = fichajes.last()
                val horasTrabajadas = lastFichaje.horasTrabajadas
                val userFichaje = "${user.name}: Último fichaje - ${lastFichaje.fechaSalida}, Horas trabajadas - $horasTrabajadas"
                userFichajes.add(userFichaje)
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userFichajes)
        listView.adapter = adapter
    }
}
