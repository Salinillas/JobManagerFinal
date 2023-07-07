package com.example.jobmanager

data class User(
    val id: Int,
    var name: String,
    var username: String,
    var password: String,
    var isAdmin: Boolean
){
    fun isAdminUser(): Boolean {
        return isAdmin
    }
}