package com.example.jobmanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.jobmanager.Database.DatabaseHandler

class LoginActivity : AppCompatActivity() {

    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        databaseHandler = DatabaseHandler.getInstance(this)
        val adminUser = User(1, "admin", "admin", "admin", true)
//        databaseHandler.addOficina(Oficina(0, "Oficina 1", false))
//        databaseHandler.addOficina(Oficina(1, "Oficina 2", false))
//        databaseHandler.addOficina(Oficina(2, "Oficina 3", false))
//        databaseHandler.addOficina(Oficina(3, "Oficina 4", false))
//        databaseHandler.addOficina(Oficina(4, "Oficina 5", false))
        // Se agrega una verificación para evitar agregar el usuario administrador repetidamente
        val adminUserId = databaseHandler.addUser(adminUser)

        if (adminUserId != -1L) {
            // El usuario administrador se ha insertado correctamente
            // adminUserId contiene el ID generado para el usuario administrador
        } else {
            // Error al insertar el usuario administrador
        }

        val buttonLogin = findViewById<Button>(R.id.loginButton)
        buttonLogin.setOnClickListener {
            val username = findViewById<EditText>(R.id.usernameEditText).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditText).text.toString()

            val storedUser = databaseHandler.getUser(username)

            if (storedUser != null && password == storedUser.password) {
                databaseHandler.setCurrentUser(storedUser)
                Toast.makeText(this, "Logeado con éxito", Toast.LENGTH_SHORT).show()

                // Determine the next activity based on user type
                val intent = if (storedUser.isAdminUser()) {
                    Intent(this, MenuActivity::class.java)
                } else {
                    Intent(this, FicharUsuario::class.java)
                }

                startActivity(intent)
                finish() // Finish the current activity
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHandler.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}
