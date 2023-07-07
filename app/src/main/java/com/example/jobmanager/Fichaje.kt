package com.example.jobmanager

data class Fichaje(
    val id: Int,
    val username: String,
    val oficina: String,
    val fechaEntrada: String,
    val fechaSalida: String,
    val horasTrabajadas: Double
)