package com.example.jobmanager


import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobmanager.Database.DatabaseHandler
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



class FicharUsuario : AppCompatActivity() {

    private lateinit var disponiblesRecyclerView: RecyclerView
    private lateinit var ocupadasRecyclerView: RecyclerView
    private lateinit var btnFicharEntrada: Button
    private lateinit var btnFicharSalida: Button
    private lateinit var oficinaEditText: EditText
    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var disponiblesAdapter: OficinaAdapter
    private lateinit var ocupadasAdapter: OficinaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fichar_usuario)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        disponiblesRecyclerView = findViewById(R.id.disponiblesRecyclerView)
        ocupadasRecyclerView = findViewById(R.id.ocupadasRecyclerView)
        btnFicharEntrada = findViewById(R.id.btnFicharEntrada)
        btnFicharSalida = findViewById(R.id.btnFicharSalida)
        oficinaEditText = findViewById(R.id.officeSelector)

        databaseHandler = DatabaseHandler.getInstance(this)

        // Configurar RecyclerViews
        disponiblesRecyclerView.layoutManager = LinearLayoutManager(this)
        ocupadasRecyclerView.layoutManager = LinearLayoutManager(this)

        // Obtener las oficinas disponibles y ocupadas
        val oficinasDisponibles = getOficinasDisponibles()
        val oficinasOcupadas = getOficinasOcupadas()

        // Crear los adapters para los RecyclerViews
        disponiblesAdapter = OficinaAdapter(oficinasDisponibles,databaseHandler)
        ocupadasAdapter = OficinaAdapter(oficinasOcupadas,databaseHandler)

        // Asignar los adapters a los RecyclerViews
        disponiblesRecyclerView.adapter = disponiblesAdapter
        ocupadasRecyclerView.adapter = ocupadasAdapter

        btnFicharEntrada.setOnClickListener {
            // Obtener la oficina seleccionada por el usuario
            val oficinaSeleccionada = obtenerOficinaSeleccionada()
            if (oficinaSeleccionada != null && databaseHandler.getCurrentUser()!=null
                && !databaseHandler.usuarioTieneFichajeAbierto(databaseHandler.getCurrentUser()!!)){
                if (oficinaSeleccionada.ocupada)
                    mostrarMensaje("Oficina ocupada, seleccione otra para fichar entrada")
                else {

                    realizarFichajeEntrada(oficinaSeleccionada)

                    // Actualizar la lista de oficinas disponibles y ocupadas
                    disponiblesAdapter.setOficinas(getOficinasDisponibles())
                    ocupadasAdapter.setOficinas(getOficinasOcupadas())

                    // Mostrar un mensaje de éxito
                    mostrarMensaje("Fichaje de entrada realizado correctamente")
                }
            } else {
                mostrarMensaje("Por favor, seleccione una oficina válida")
            }
        }

        btnFicharSalida.setOnClickListener {
            // Obtener la oficina seleccionada por el usuario
            val oficinaSeleccionada = obtenerOficinaSeleccionada()
            if (oficinaSeleccionada != null && databaseHandler.getCurrentUser()!=null
                && databaseHandler.usuarioTieneFichajeAbierto(databaseHandler.getCurrentUser()!!)) {
                if (!oficinaSeleccionada.ocupada) {
                    mostrarMensaje("Oficina no ocupada, seleccione otra para fichar salida")
                    // Obtener la fecha y hora actual
                }else {
                    val fechaHoraActual = obtenerFechaHoraActual()

                    // Realizar el fichaje y almacenarlo en la base de datos
                    realizarFichajeSalida(oficinaSeleccionada, fechaHoraActual)

                    // Actualizar la lista de oficinas disponibles y ocupadas
                    disponiblesAdapter.setOficinas(getOficinasDisponibles())
                    ocupadasAdapter.setOficinas(getOficinasOcupadas())
                }

            } else {
                mostrarMensaje("Por favor, seleccione una oficina válida")
                }
            }
        }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                logout()
                true
            }
            R.id.action_preguntas_frecuentes -> {
                // Navegar a la actividad FAQ
                faq()
                true
            }
            R.id.action_cerrar_sesion -> {
                // Cerrar sesión y navegar a la actividad de inicio de sesión
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("¿Desea salir de JobManager y conservar su sesión?")
                .setPositiveButton("Sí") { _, _ ->
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_HOME)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
            builder.show()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun getOficinasDisponibles(): List<Oficina> {
        return databaseHandler.getOficinasDisponibles()
    }

    private fun getOficinasOcupadas(): List<Oficina> {
        return databaseHandler.getOficinasOcupadas()
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

    private fun realizarFichajeEntrada(oficina: Oficina) {
        val fechaHora = obtenerFechaHoraActual()
        val usuario = databaseHandler.getCurrentUser()
        if (usuario != null) {
            databaseHandler.addFichaje(usuario.username, oficina.nombre, fechaHora)
            databaseHandler.cambiarOficinaAOcupada(oficina.nombre)
        } else {
            mostrarMensaje("No se ha podido realizar el fichaje")
        }
    }

    private fun realizarFichajeSalida(oficina: Oficina, fechaHora: String) {
        val usuario = databaseHandler.getCurrentUser()
        if (usuario!=null) {
            val fichaje = databaseHandler.getFichaje(usuario.username)
            if (fichaje != null && fichaje.fechaSalida == "") {
                val horasTrabajadas =
                    databaseHandler.calcularHorasTrabajadas(fichaje.fechaEntrada, fechaHora)
                fichaje.fechaSalida = fechaHora
                fichaje.horasTrabajadas = horasTrabajadas
                databaseHandler.updateFichaje(fichaje)
                databaseHandler.cambiarOficinaANoOcupada(oficina.nombre)
                mostrarMensaje("Fichaje de salida realizado correctamente. Horas trabajadas: " + horasTrabajadas)
            } else {
                mostrarMensaje("No se ha encontrado el fichaje anterior")
            }
        }
    }




    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun logout() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun faq() {
        startActivity(Intent(this, FAQActivity::class.java))
        finish()
    }
}