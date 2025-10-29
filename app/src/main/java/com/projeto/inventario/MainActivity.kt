package com.projeto.inventario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var rvInventarios: RecyclerView
    private lateinit var tvEmptyState: TextView
    private lateinit var fabAddInventario: FloatingActionButton
    private lateinit var inventarioAdapter: InventarioAdapter

    private var listaDeInventarios = mutableListOf<Inventario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvInventarios = findViewById(R.id.rvInventarios)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        fabAddInventario = findViewById(R.id.fabAddInventario)

        setupRecyclerView()

        carregarDadosSimulados()

        verificarEstadoDaLista()

        fabAddInventario.setOnClickListener {
            mostrarDialogoAddInventario()
        }
    }

    private fun setupRecyclerView() {
        inventarioAdapter = InventarioAdapter(listaDeInventarios)
        rvInventarios.layoutManager = LinearLayoutManager(this)
        rvInventarios.adapter = inventarioAdapter
    }

    private fun carregarDadosSimulados() {
        // Esta função agora serve apenas para carregar dados iniciais (se houver)
        // Se quisermos que comece vazia, deixamos esta função vazia.
        /*
        val novosInventarios = listOf(
            Inventario("id1", "Inventário Empresa"),
            Inventário("id2", "Inventário Casa")
        )
        val posicaoInicial = listaDeInventarios.size
        listaDeInventarios.addAll(novosInventarios)
        inventarioAdapter.notifyItemRangeInserted(posicaoInicial, novosInventarios.size)
        */
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

        val textInputLayout = dialogView.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tilNomeNovoInventario)

        builder.setTitle(getString(R.string.dialog_add_inventory_title))
        builder.setView(dialogView)

        builder.setPositiveButton(getString(R.string.dialog_add_inventory_button_create)) { dialog, _ ->
            val nomeDoInventario = textInputLayout.editText?.text.toString().trim()

            if (nomeDoInventario.isNotEmpty()) {
                adicionarNovoInventario(nomeDoInventario)
                dialog.dismiss()
            } else {
                Toast.makeText(this, getString(R.string.dialog_add_inventory_toast_error), Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton(getString(R.string.dialog_add_inventory_button_cancel)) { dialog, _ ->
            dialog.cancel()
        }

        builder.create().show()
    }

    private fun adicionarNovoInventario(nome: String) {
        val idUnico = UUID.randomUUID().toString()

        val novoInventario = Inventario(id = idUnico, nome = nome)

        listaDeInventarios.add(novoInventario)

        val novaPosicao = listaDeInventarios.size - 1

        inventarioAdapter.notifyItemInserted(novaPosicao)

        verificarEstadoDaLista()

        Toast.makeText(this, getString(R.string.dialog_add_inventory_toast_success), Toast.LENGTH_SHORT).show()
    }
}