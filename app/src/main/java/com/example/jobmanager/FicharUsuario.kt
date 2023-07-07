package com.example.jobmanager

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.jobmanager.Database.DatabaseHandler
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FicharUsuario : AppCompatActivity() {

    private lateinit var disponiblesLayout: LinearLayout
    private lateinit var ocupadasLayout: LinearLayout
    private lateinit var btnFicharEntrada: Button
    private lateinit var btnFicharSalida: Button
    private lateinit var oficinaEditText: EditText
    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fichar_usuario)

        disponiblesLayout = findViewById(R.id.disponiblesLayout)
        ocupadasLayout = findViewById(R.id.ocupadasLayout)
        btnFicharEntrada = findViewById(R.id.btnFicharEntrada)
        btnFicharSalida = findViewById(R.id.btnFicharSalida)
        oficinaEditText = findViewById(R.id.officeSelector)

        databaseHandler = DatabaseHandler.getInstance(this)

        // Mostrar las oficinas disponibles
        val oficinasDisponibles = getOficinasDisponibles()
        mostrarOficinas(oficinasDisponibles, disponiblesLayout)

        // Mostrar las oficinas ocupadas
        val oficinasOcupadas = getOficinasOcupadas()
        mostrarOficinas(oficinasOcupadas, ocupadasLayout)

        btnFicharEntrada.setOnClickListener {
            btnFicharEntrada.isEnabled = false
            btnFicharSalida.isEnabled = true
            // Obtener la oficina seleccionada por el usuario
            val oficinaSeleccionada = obtenerOficinaSeleccionada()
            if (oficinaSeleccionada != null) {
                // Obtener la fecha y hora actual
                val fechaHoraActual = obtenerFechaHoraActual()

                // Realizar el fichaje y almacenarlo en la base de datos
                realizarFichaje(oficinaSeleccionada, fechaHoraActual)

                // Mostrar un mensaje de éxito
                mostrarMensaje("Fichaje realizado correctamente")

            } else {
                mostrarMensaje("Por favor, seleccione una oficina")
            }
        }

        btnFicharSalida.setOnClickListener {
            btnFicharSalida.isEnabled = false
            btnFicharEntrada.isEnabled = true
            // Obtener la oficina seleccionada por el usuario
            val oficinaSeleccionada = obtenerOficinaSeleccionada()
            if (oficinaSeleccionada != null) {
                // Obtener la fecha y hora actual
                val fechaHoraActual = obtenerFechaHoraActual()

                // Realizar el fichaje y almacenarlo en la base de datos
                realizarFichaje(oficinaSeleccionada, fechaHoraActual)

                // Mostrar un mensaje de éxito
                mostrarMensaje("Fichaje de salida realizado correctamente")
            } else {
                mostrarMensaje("Por favor, seleccione una oficina")
            }
        }


    }

    private fun getOficinasDisponibles(): List<Oficina> {
        return databaseHandler.getOficinasDisponibles()
    }

    private fun getOficinasOcupadas(): List<Oficina> {
        return databaseHandler.getOficinasOcupadas()
    }

    private fun mostrarOficinas(oficinas: List<Oficina>, layout: LinearLayout) {
        for (oficina in oficinas) {
            val textView = TextView(this)
            textView.text = oficina.nombre
            layout.addView(textView)
        }
    }

    private fun obtenerOficinaSeleccionada(): Oficina? {
        val nombreOficina = oficinaEditText.text.toString()
        return databaseHandler.getOficina(nombreOficina)
    }

    private fun obtenerFechaHoraActual(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    }

    private fun realizarFichaje(oficina: Oficina, fechaHora: String) {
        val usuario = databaseHandler.getCurrentUser()
        if (usuario != null) {
            databaseHandler.addFichaje(usuario.username, oficina.nombre, fechaHora)
            databaseHandler.cambiarEstadoOficina(oficina.nombre)
        } else {
            mostrarMensaje("No se ha podido realizar el fichaje")
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        }
}