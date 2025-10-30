package com.projeto.inventario

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var rvInventarios: RecyclerView
    private lateinit var tvEmptyState: TextView
    private lateinit var fabAddInventario: FloatingActionButton
    private lateinit var inventarioAdapter: InventarioAdapter

    private var listaDeInventarios = mutableListOf<Inventario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbarMain)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.main_title)

        rvInventarios = findViewById(R.id.rvInventarios)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        fabAddInventario = findViewById(R.id.fabAddInventario)

        setupRecyclerView()

        carregarInventarios()

        fabAddInventario.setOnClickListener {
            mostrarDialogoAddInventario()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                fazerLogout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fazerLogout() {
        GestorDeDados.fazerLogout()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupRecyclerView() {
        inventarioAdapter = InventarioAdapter(listaDeInventarios) { inventarioClicado ->
            mostrarDialogoOpcoes(inventarioClicado)
        }
        rvInventarios.layoutManager = LinearLayoutManager(this)
        rvInventarios.adapter = inventarioAdapter
    }

    private fun carregarInventarios() {
        listaDeInventarios.clear()
        listaDeInventarios.addAll(GestorDeDados.getInventarios())
        inventarioAdapter.notifyDataSetChanged()

        verificarEstadoDaLista()
    }

    private fun verificarEstadoDaLista() {
        if (listaDeInventarios.isEmpty()) {
            rvInventarios.visibility = View.GONE
            tvEmptyState.visibility = View.VISIBLE
        } else {
            rvInventarios.visibility = View.VISIBLE
            tvEmptyState.visibility = View.GONE
        }
    }

    private fun mostrarDialogoAddInventario() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_add_inventory, null)
        val textInputLayout = dialogView.findViewById<TextInputLayout>(R.id.tilNomeNovoInventario)

        builder.setTitle(getString(R.string.dialog_add_inventory_title))
        builder.setView(dialogView)

        builder.setPositiveButton(getString(R.string.dialog_add_inventory_button_create)) { dialog, _ ->
            val nomeDoInventario = textInputLayout.editText?.text.toString().trim()

            if (nomeDoInventario.isNotEmpty()) {
                adicionarNovoInventario(nomeDoInventario)
                dialog.dismiss()
            } else {
                Toast.makeText(this, getString(R.string.login_error_empty_fields), Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton(getString(R.string.dialog_add_inventory_button_cancel)) { dialog, _ ->
            dialog.cancel()
        }

        builder.create().show()
    }

    private fun adicionarNovoInventario(nome: String) {
        val novoInventario = GestorDeDados.addInventario(nome)

        if (novoInventario != null) {
            listaDeInventarios.add(novoInventario)
            val novaPosicao = listaDeInventarios.size - 1
            inventarioAdapter.notifyItemInserted(novaPosicao)
            verificarEstadoDaLista()
            Toast.makeText(this, getString(R.string.dialog_add_inventory_toast_success), Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDialogoOpcoes(inventario: Inventario) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_options_title))

        val opcoes = arrayOf(
            getString(R.string.action_edit),
            getString(R.string.dialog_delete_button_delete)
        )

        builder.setItems(opcoes) { dialog, which ->
            when (which) {
                0 -> {
                    mostrarDialogoEditarInventario(inventario)
                }
                1 -> {
                    mostrarDialogoExcluirInventario(inventario)
                }
            }
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.dialog_add_inventory_button_cancel)) { dialog, _ ->
            dialog.cancel()
        }

        builder.create().show()
    }

    private fun mostrarDialogoEditarInventario(inventarioParaEditar: Inventario) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_add_inventory, null)
        val textInputLayout = dialogView.findViewById<TextInputLayout>(R.id.tilNomeNovoInventario)

        textInputLayout.editText?.setText(inventarioParaEditar.nome)

        builder.setTitle(getString(R.string.action_edit))
        builder.setView(dialogView)

        builder.setPositiveButton(getString(R.string.action_save)) { dialog, _ ->
            val nome = textInputLayout.editText?.text.toString().trim()

            if (nome.isNotEmpty()) {
                atualizarInventario(inventarioParaEditar, nome)
                dialog.dismiss()
            } else {
                Toast.makeText(this, getString(R.string.login_error_empty_fields), Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton(getString(R.string.dialog_add_inventory_button_cancel)) { dialog, _ ->
            dialog.cancel()
        }

        builder.create().show()
    }

    private fun atualizarInventario(inventarioOriginal: Inventario, nome: String) {
        GestorDeDados.updateInventario(inventarioOriginal, nome)

        val posicao = listaDeInventarios.indexOf(inventarioOriginal)
        if (posicao != -1) {
            val inventarioAtualizado = inventarioOriginal.copy(nome = nome)
            listaDeInventarios[posicao] = inventarioAtualizado
            inventarioAdapter.notifyItemChanged(posicao)
        }

        Toast.makeText(this, getString(R.string.dialog_edit_inventario_toast_success), Toast.LENGTH_SHORT).show()
    }

    private fun mostrarDialogoExcluirInventario(inventarioParaExcluir: Inventario) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_delete_title))
        builder.setMessage(getString(R.string.dialog_delete_message))

        builder.setPositiveButton(getString(R.string.dialog_delete_button_delete)) { dialog, _ ->
            excluirInventario(inventarioParaExcluir)
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.dialog_add_inventory_button_cancel)) { dialog, _ ->
            dialog.cancel()
        }

        builder.create().show()
    }

    private fun excluirInventario(inventarioParaExcluir: Inventario) {
        val posicao = listaDeInventarios.indexOf(inventarioParaExcluir)
        if (posicao == -1) {
            return
        }

        GestorDeDados.removeInventario(inventarioParaExcluir)
        listaDeInventarios.removeAt(posicao)
        inventarioAdapter.notifyItemRemoved(posicao)

        verificarEstadoDaLista()
    }
}