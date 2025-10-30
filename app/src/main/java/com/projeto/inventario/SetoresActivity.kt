package com.projeto.inventario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout

class SetoresActivity : AppCompatActivity() {

    private var inventarioId: String? = null
    private var inventarioNome: String? = null

    private lateinit var rvSetores: RecyclerView
    private lateinit var tvSetoresEmptyState: TextView
    private lateinit var fabAddSetor: FloatingActionButton

    private lateinit var setorAdapter: SetorAdapter
    private var listaDeSetores = mutableListOf<Setor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setores)

        val toolbar = findViewById<Toolbar>(R.id.toolbarSetores)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        inventarioId = intent.getStringExtra("INVENTARIO_ID")
        inventarioNome = intent.getStringExtra("INVENTARIO_NOME")

        if (inventarioId == null || inventarioNome == null) {
            Toast.makeText(this, getString(R.string.setores_error_loading), Toast.LENGTH_LONG).show()
            finish()
            return
        }

        supportActionBar?.title = inventarioNome

        rvSetores = findViewById(R.id.rvSetores)
        tvSetoresEmptyState = findViewById(R.id.tvSetoresEmptyState)
        fabAddSetor = findViewById(R.id.fabAddSetor)

        setupRecyclerView()

        carregarSetores()

        fabAddSetor.setOnClickListener {
            mostrarDialogoAddSetor()
        }
    }

    private fun carregarSetores() {
        val idDoInventarioPai = inventarioId ?: return

        listaDeSetores.clear()
        listaDeSetores.addAll(GestorDeDados.getSetores(idDoInventarioPai))
        setorAdapter.notifyDataSetChanged()

        verificarEstadoDaListaSetores()
    }

    private fun setupRecyclerView() {
        setorAdapter = SetorAdapter(listaDeSetores) { setorClicado ->
            mostrarDialogoOpcoes(setorClicado)
        }
        rvSetores.layoutManager = GridLayoutManager(this, 2)
        rvSetores.adapter = setorAdapter
    }

    private fun verificarEstadoDaListaSetores() {
        if (listaDeSetores.isEmpty()) {
            rvSetores.visibility = View.GONE
            tvSetoresEmptyState.visibility = View.VISIBLE
        } else {
            rvSetores.visibility = View.VISIBLE
            tvSetoresEmptyState.visibility = View.GONE
        }
    }

    private fun mostrarDialogoAddSetor() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_add_setor, null)
        val textInputLayout = dialogView.findViewById<TextInputLayout>(R.id.tilNomeNovoSetor)

        builder.setTitle(getString(R.string.dialog_add_setor_title))
        builder.setView(dialogView)

        builder.setPositiveButton(getString(R.string.dialog_add_inventory_button_create)) { dialog, _ ->
            val nomeDoSetor = textInputLayout.editText?.text.toString().trim()

            if (nomeDoSetor.isNotEmpty()) {
                adicionarNovoSetor(nomeDoSetor)
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

    private fun adicionarNovoSetor(nome: String) {
        val idDoInventarioPai = inventarioId ?: return

        val novoSetor = GestorDeDados.addSetor(nome, idDoInventarioPai)
        listaDeSetores.add(novoSetor)
        val novaPosicao = listaDeSetores.size - 1
        setorAdapter.notifyItemInserted(novaPosicao)

        verificarEstadoDaListaSetores()

        Toast.makeText(this, getString(R.string.dialog_add_setor_toast_success), Toast.LENGTH_SHORT).show()
    }

    private fun mostrarDialogoOpcoes(setor: Setor) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_options_title))

        val opcoes = arrayOf(
            getString(R.string.action_edit),
            getString(R.string.dialog_delete_button_delete)
        )

        builder.setItems(opcoes) { dialog, which ->
            when (which) {
                0 -> {
                    mostrarDialogoEditarSetor(setor)
                }
                1 -> {
                    mostrarDialogoExcluirSetor(setor)
                }
            }
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.dialog_add_inventory_button_cancel)) { dialog, _ ->
            dialog.cancel()
        }

        builder.create().show()
    }

    private fun mostrarDialogoEditarSetor(setorParaEditar: Setor) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_add_setor, null)
        val textInputLayout = dialogView.findViewById<TextInputLayout>(R.id.tilNomeNovoSetor)

        textInputLayout.editText?.setText(setorParaEditar.nome)

        builder.setTitle(getString(R.string.action_edit))
        builder.setView(dialogView)

        builder.setPositiveButton(getString(R.string.action_save)) { dialog, _ ->
            val nome = textInputLayout.editText?.text.toString().trim()

            if (nome.isNotEmpty()) {
                atualizarSetor(setorParaEditar, nome)
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

    private fun atualizarSetor(setorOriginal: Setor, nome: String) {
        GestorDeDados.updateSetor(setorOriginal, nome)

        val posicao = listaDeSetores.indexOf(setorOriginal)
        if (posicao != -1) {
            val setorAtualizado = setorOriginal.copy(nome = nome)
            listaDeSetores[posicao] = setorAtualizado
            setorAdapter.notifyItemChanged(posicao)
        }

        Toast.makeText(this, getString(R.string.dialog_edit_setor_toast_success), Toast.LENGTH_SHORT).show()
    }

    private fun mostrarDialogoExcluirSetor(setorParaExcluir: Setor) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_delete_title))
        builder.setMessage(getString(R.string.dialog_delete_message))

        builder.setPositiveButton(getString(R.string.dialog_delete_button_delete)) { dialog, _ ->
            excluirSetor(setorParaExcluir)
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.dialog_add_inventory_button_cancel)) { dialog, _ ->
            dialog.cancel()
        }

        builder.create().show()
    }

    private fun excluirSetor(setorParaExcluir: Setor) {
        val posicao = listaDeSetores.indexOf(setorParaExcluir)
        if (posicao == -1) {
            return
        }

        GestorDeDados.removeSetor(setorParaExcluir)
        listaDeSetores.removeAt(posicao)
        setorAdapter.notifyItemRemoved(posicao)

        verificarEstadoDaListaSetores()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}