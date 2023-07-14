package com.example.jobmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jobmanager.Database.DatabaseHandler


class RegisterActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        databaseHelper = DatabaseHandler(this)

        val buttonRegister = findViewById<Button>(R.id.registerButton)
        buttonRegister.setOnClickListener {
            val id = findViewById<EditText>(R.id.nameEditText).id
            val name = findViewById<EditText>(R.id.napeEditText).text.toString()
            val username = findViewById<EditText>(R.id.nameEditText).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
            val password2 = findViewById<EditText>(R.id.password2EditText).text.toString()

            if (username.isNotEmpty() && password.isNotEmpty() && password2.isNotEmpty()
                && password==password2) {

                    val user = User(id, name, username, password, false)
                    databaseHelper.addUser(user)
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                    finish() // Finish the activity and return to the login screen

            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        val buttonBorrar = findViewById<Button>(R.id.borrarButton)
        buttonBorrar.setOnClickListener {

            val username = findViewById<EditText>(R.id.nameEditText).text.toString()
            if(databaseHelper.isUserExists(username)) {
                databaseHelper.deleteUser(username)
                Toast.makeText(this, "Usuario borrado correctamente", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Introduce un usuario válido", Toast.LENGTH_SHORT).show()

            }

        }

    }
}