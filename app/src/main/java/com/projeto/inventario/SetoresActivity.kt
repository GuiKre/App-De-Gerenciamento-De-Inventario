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
import java.util.UUID

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

        verificarEstadoDaListaSetores()

        fabAddSetor.setOnClickListener {
            mostrarDialogoAddSetor()
        }
    }

    private fun setupRecyclerView() {
        setorAdapter = SetorAdapter(listaDeSetores)
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
                Toast.makeText(this, getString(R.string.dialog_add_inventory_toast_error), Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton(getString(R.string.dialog_add_inventory_button_cancel)) { dialog, _ ->
            dialog.cancel()
        }

        builder.create().show()
    }

    private fun adicionarNovoSetor(nome: String) {
        val idDoInventarioPai = inventarioId ?: return

        val novoSetor = Setor(
            id = UUID.randomUUID().toString(),
            nome = nome,
            inventarioId = idDoInventarioPai
        )

        listaDeSetores.add(novoSetor)

        val novaPosicao = listaDeSetores.size - 1
        setorAdapter.notifyItemInserted(novaPosicao)

        verificarEstadoDaListaSetores()

        Toast.makeText(this, getString(R.string.dialog_add_setor_toast_success), Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}