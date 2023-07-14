package com.example.jobmanager

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobmanager.Database.DatabaseHandler
import com.example.jobmanager.R

class OficinasActivity : AppCompatActivity() {

    private lateinit var recyclerViewOficinasLibres: RecyclerView
    private lateinit var recyclerViewOficinasOcupadas: RecyclerView
    private lateinit var textViewOficinasLibres: TextView
    private lateinit var textViewOficinasOcupadas: TextView

    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var adapterOficinasLibres: OficinaAdapter
    private lateinit var adapterOficinasOcupadas: OficinaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oficinas)

        recyclerViewOficinasLibres = findViewById(R.id.recyclerViewOficinasLibres)
        recyclerViewOficinasOcupadas = findViewById(R.id.recyclerViewOficinasOcupadas)
        textViewOficinasLibres = findViewById(R.id.textViewOficinasLibres)
        textViewOficinasOcupadas = findViewById(R.id.textViewOficinasOcupadas)

        databaseHandler = DatabaseHandler(this)

        adapterOficinasLibres = OficinaAdapter(emptyList(), databaseHandler)
        adapterOficinasOcupadas = OficinaAdapter(emptyList(), databaseHandler)

        recyclerViewOficinasLibres.adapter = adapterOficinasLibres
        recyclerViewOficinasOcupadas.adapter = adapterOficinasOcupadas

        recyclerViewOficinasLibres.layoutManager = LinearLayoutManager(this)
        recyclerViewOficinasOcupadas.layoutManager = LinearLayoutManager(this)

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        recyclerViewOficinasLibres.addItemDecoration(dividerItemDecoration)
        recyclerViewOficinasOcupadas.addItemDecoration(dividerItemDecoration)

        mostrarOficinas()
    }

    private fun mostrarOficinas() {
        val oficinasLibres = databaseHandler.getOficinasDisponibles()
        val oficinasOcupadas = databaseHandler.getOficinasOcupadas()

        adapterOficinasLibres.setOficinas(oficinasLibres)
        adapterOficinasOcupadas.setOficinasOcupadas(oficinasOcupadas)

        textViewOficinasLibres.text = "Oficinas libres (${oficinasLibres.size})"
        textViewOficinasOcupadas.text = "Oficinas ocupadas (${oficinasOcupadas.size})"
    }
}
