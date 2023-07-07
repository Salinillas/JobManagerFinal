package com.example.jobmanager

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jobmanager.Database.DatabaseHandler

class UserConfigActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var nameLabelTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var userLabelTextView: TextView
    private lateinit var userEditText: EditText
    private lateinit var passwordLabelTextView: TextView
    private lateinit var passwordEditText: EditText
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_config)

        titleTextView = findViewById(R.id.titleTextView)
        nameLabelTextView = findViewById(R.id.nameLabelTextView)
        nameEditText = findViewById(R.id.nameEditText)
        userLabelTextView = findViewById(R.id.userLabelTextView)
        userEditText = findViewById(R.id.userEditText)
        passwordLabelTextView = findViewById(R.id.passwordLabelTextView)
        passwordEditText = findViewById(R.id.passwordEditText)
        cancelButton = findViewById(R.id.cancelButton)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)

        // Set the title of the user settings activity
        titleTextView.text = "Configuración de Usuario"

        // Set up database handler
        databaseHandler = DatabaseHandler.getInstance(this)

        // Fetch and display user settings
        val username = userEditText.text.toString()
        currentUser = databaseHandler.getUser(username)!!
        nameEditText.setText(currentUser.name)
        userEditText.setText(currentUser.username)
        passwordEditText.setText(currentUser.password)

        cancelButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val username = userEditText.text.toString()
            val password = passwordEditText.text.toString()

            currentUser.name = name
            currentUser.username = username
            currentUser.password = password

            databaseHandler.saveUser(currentUser)
            Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show()
        }

        deleteButton.setOnClickListener {
            databaseHandler.deleteUser(currentUser)
            Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}