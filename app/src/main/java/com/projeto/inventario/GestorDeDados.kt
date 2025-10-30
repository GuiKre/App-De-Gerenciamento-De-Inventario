package com.projeto.inventario

import java.util.UUID

object GestorDeDados {

    private val listaDeInventarios = mutableListOf<Inventario>()
    private val listaDeSetores = mutableListOf<Setor>()
    private val listaDeItens = mutableListOf<Item>()

    fun getInventarios(): List<Inventario> {
        return listaDeInventarios
    }

    fun addInventario(nome: String): Inventario {
        val novoInventario = Inventario(
            id = UUID.randomUUID().toString(),
            nome = nome
        )
        listaDeInventarios.add(novoInventario)
        return novoInventario
    }

    fun getSetores(inventarioId: String): List<Setor> {
        return listaDeSetores.filter { it.inventarioId == inventarioId }
    }

    fun addSetor(nome: String, inventarioId: String): Setor {
        val novoSetor = Setor(
            id = UUID.randomUUID().toString(),
            nome = nome,
            inventarioId = inventarioId
        )
        listaDeSetores.add(novoSetor)
        return novoSetor
    }

    fun getItens(setorId: String): List<Item> {
        return listaDeItens.filter { it.setorId == setorId }
    }

    fun addItem(nome: String, modelo: String, codigo: String, quantidade: Int, setorId: String): Item {
        val novoItem = Item(
            id = UUID.randomUUID().toString(),
            nome = nome,
            modelo = modelo,
            codigo = codigo,
            quantidade = quantidade,
            setorId = setorId
        )
        listaDeItens.add(novoItem)
        return novoItem
    }

    fun removeItem(itemParaRemover: Item) {
        listaDeItens.remove(itemParaRemover)
    }

    fun removeSetor(setorParaRemover: Setor) {
        listaDeSetores.remove(setorParaRemover)
    }

    fun removeInventario(inventarioParaRemover: Inventario) {
        listaDeInventarios.remove(inventarioParaRemover)
    }

    fun updateItem(itemOriginal: Item, nome: String, modelo: String, codigo: String, quantidade: Int) {
        val index = listaDeItens.indexOf(itemOriginal)
        if (index != -1) {
            val itemAtualizado = itemOriginal.copy(
                nome = nome,
                modelo = modelo,
                codigo = codigo,
                quantidade = quantidade
            )
            listaDeItens[index] = itemAtualizado
        }
    }

    fun updateSetor(setorOriginal: Setor, nome: String) {
        val index = listaDeSetores.indexOf(setorOriginal)
        if (index != -1) {
            val setorAtualizado = setorOriginal.copy(nome = nome)
            listaDeSetores[index] = setorAtualizado
        }
    }

    fun updateInventario(inventarioOriginal: Inventario, nome: String) {
        val index = listaDeInventarios.indexOf(inventarioOriginal)
        if (index != -1) {
            val inventarioAtualizado = inventarioOriginal.copy(nome = nome)
            listaDeInventarios[index] = inventarioAtualizado
        }
    }
}