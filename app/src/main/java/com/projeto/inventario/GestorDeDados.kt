package com.projeto.inventario

import java.util.UUID

object GestorDeDados {

    private val listaDeUsuarios = mutableListOf<Usuario>()
    private var emailUsuarioAtual: String? = null

    private val listaDeInventarios = mutableListOf<Inventario>()
    private val listaDeSetores = mutableListOf<Setor>()
    private val listaDeItens = mutableListOf<Item>()

    fun registrarUsuario(email: String, senha: String): Boolean {
        val usuarioExistente = listaDeUsuarios.find { it.email == email }
        if (usuarioExistente != null) {
            return false
        }
        listaDeUsuarios.add(Usuario(email, senha))
        return true
    }

    fun verificarLogin(email: String, senha: String): Boolean {
        val usuario = listaDeUsuarios.find { it.email == email }
        val sucesso = usuario != null && usuario.senha == senha
        if (sucesso) {
            emailUsuarioAtual = email
        }
        return sucesso
    }

    fun fazerLogout() {
        emailUsuarioAtual = null
    }

    fun getInventarios(): List<Inventario> {
        val email = emailUsuarioAtual ?: return emptyList()
        return listaDeInventarios.filter { it.ownerEmail == email }
    }

    fun addInventario(nome: String): Inventario? {
        val email = emailUsuarioAtual ?: return null

        val novoInventario = Inventario(
            id = UUID.randomUUID().toString(),
            nome = nome,
            ownerEmail = email
        )
        listaDeInventarios.add(novoInventario)
        return novoInventario
    }

    fun removeInventario(inventarioParaRemover: Inventario) {
        listaDeInventarios.remove(inventarioParaRemover)
    }

    fun updateInventario(inventarioOriginal: Inventario, nome: String) {
        val index = listaDeInventarios.indexOf(inventarioOriginal)
        if (index != -1) {
            val inventarioAtualizado = inventarioOriginal.copy(nome = nome)
            listaDeInventarios[index] = inventarioAtualizado
        }
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

    fun removeSetor(setorParaRemover: Setor) {
        listaDeSetores.remove(setorParaRemover)
    }

    fun updateSetor(setorOriginal: Setor, nome: String) {
        val index = listaDeSetores.indexOf(setorOriginal)
        if (index != -1) {
            val setorAtualizado = setorOriginal.copy(nome = nome)
            listaDeSetores[index] = setorAtualizado
        }
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
}