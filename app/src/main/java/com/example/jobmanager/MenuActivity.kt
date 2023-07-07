package com.example.jobmanager

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val btnVerOficinas: Button = findViewById(R.id.btnVerOficinas)
        val btnVerFichajes: Button = findViewById(R.id.btnVerFichajes)
        val btnPanelControl: Button = findViewById(R.id.btnPanelControl)

        btnVerOficinas.setOnClickListener {
            // Acción para ver oficinas
            startActivity(Intent(this, OficinasActivity::class.java))
        }

        btnVerFichajes.setOnClickListener {
            // Acción para ver fichajes
            startActivity(Intent(this, UsuariosActivity::class.java))
        }

        btnPanelControl.setOnClickListener {
            // Acción para ir al panel de control
            startActivity(Intent(this, UserConfigActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_preguntas_frecuentes -> {
                // Navegar a la actividad FAQ
                startActivity(Intent(this, FAQActivity::class.java))
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

    private fun logout() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}