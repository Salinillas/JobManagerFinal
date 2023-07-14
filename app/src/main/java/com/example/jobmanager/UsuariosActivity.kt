package com.example.jobmanager

import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.jobmanager.Database.DatabaseHandler
import com.example.jobmanager.Fichaje
import com.example.jobmanager.User

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UsuariosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FichajeAdapter
    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuarios)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FichajeAdapter()
        recyclerView.adapter = adapter

        databaseHandler = DatabaseHandler.getInstance(this)

        val fichajes = databaseHandler.verFichajes()
        adapter.setFichajes(fichajes)
    }
}