package com.example.jobmanager

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.jobmanager.Database.DatabaseHandler
import com.example.jobmanager.Oficina

class OficinasActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oficinas)

        listView = findViewById(R.id.listView)
        databaseHandler = DatabaseHandler.getInstance(this)

        var oficinas = databaseHandler.getOficinasDisponibles() + databaseHandler.getOficinasOcupadas()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, oficinas)
        listView.adapter = adapter
    }
}
