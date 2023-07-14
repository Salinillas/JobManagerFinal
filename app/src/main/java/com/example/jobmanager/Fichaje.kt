package com.example.jobmanager

data class Fichaje(
    val id: Int,
    val username: String,
    val oficina: String,
    val fechaEntrada: String,
    var fechaSalida: String,
    var horasTrabajadas: Double
)