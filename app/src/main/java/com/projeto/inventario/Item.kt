package com.projeto.inventario

data class Item(
    val id: String,
    val nome: String,
    val modelo: String,
    val codigo: String,
    val quantidade: Int,
    val setorId: String
)