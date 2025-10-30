package com.projeto.inventario

import android.os.Bundle
import android.view.LayoutInflater
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
import java.util.UUID

class ItensActivity : AppCompatActivity() {

    private var setorId: String? = null
    private var setorNome: String? = null

    private lateinit var rvItens: RecyclerView
    private lateinit var tvItensEmptyState: TextView
    private lateinit var fabAddItem: FloatingActionButton

    private lateinit var itemAdapter: ItemAdapter
    private var listaDeItens = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itens)

        val toolbar = findViewById<Toolbar>(R.id.toolbarItens)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setorId = intent.getStringExtra("SETOR_ID")
        setorNome = intent.getStringExtra("SETOR_NOME")

        if (setorId == null || setorNome == null) {
            Toast.makeText(this, getString(R.string.itens_error_loading), Toast.LENGTH_LONG).show()
            finish()
            return
        }

        supportActionBar?.title = setorNome

        rvItens = findViewById(R.id.rvItens)
        tvItensEmptyState = findViewById(R.id.tvItensEmptyState)
        fabAddItem = findViewById(R.id.fabAddItem)

        setupRecyclerView()
        verificarEstadoDaListaItens()

        fabAddItem.setOnClickListener {
            mostrarDialogoAddItem()
        }
    }

    private fun setupRecyclerView() {
        itemAdapter = ItemAdapter(listaDeItens)
        rvItens.layoutManager = LinearLayoutManager(this)
        rvItens.adapter = itemAdapter
    }

    private fun verificarEstadoDaListaItens() {
        if (listaDeItens.isEmpty()) {
            rvItens.visibility = View.GONE
            tvItensEmptyState.visibility = View.VISIBLE
        } else {
            rvItens.visibility = View.VISIBLE
            tvItensEmptyState.visibility = View.GONE
        }
    }

    private fun mostrarDialogoAddItem() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_add_item, null)

        val tilNome = dialogView.findViewById<TextInputLayout>(R.id.tilNomeNovoItem)
        val tilModelo = dialogView.findViewById<TextInputLayout>(R.id.tilModeloNovoItem)
        val tilCodigo = dialogView.findViewById<TextInputLayout>(R.id.tilCodigoNovoItem)
        val tilQuantidade = dialogView.findViewById<TextInputLayout>(R.id.tilQuantidadeNovoItem)

        builder.setTitle(getString(R.string.dialog_add_item_title))
        builder.setView(dialogView)

        builder.setPositiveButton(getString(R.string.dialog_add_inventory_button_create)) { dialog, _ ->
            val nome = tilNome.editText?.text.toString().trim()
            val modelo = tilModelo.editText?.text.toString().trim()
            val codigo = tilCodigo.editText?.text.toString().trim()
            val quantidadeStr = tilQuantidade.editText?.text.toString().trim()

            if (nome.isEmpty() || modelo.isEmpty() || codigo.isEmpty() || quantidadeStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.login_error_empty_fields), Toast.LENGTH_SHORT).show()
            } else {
                val quantidade = quantidadeStr.toIntOrNull() ?: 0
                adicionarNovoItem(nome, modelo, codigo, quantidade)
                dialog.dismiss()
            }
        }

        builder.setNegativeButton(getString(R.string.dialog_add_inventory_button_cancel)) { dialog, _ ->
            dialog.cancel()
        }

        builder.create().show()
    }

    private fun adicionarNovoItem(nome: String, modelo: String, codigo: String, quantidade: Int) {
        val idDoSetorPai = setorId ?: return

        val novoItem = Item(
            id = UUID.randomUUID().toString(),
            nome = nome,
            modelo = modelo,
            codigo = codigo,
            quantidade = quantidade,
            setorId = idDoSetorPai
        )

        listaDeItens.add(novoItem)

        val novaPosicao = listaDeItens.size - 1
        itemAdapter.notifyItemInserted(novaPosicao)
        verificarEstadoDaListaItens()

        Toast.makeText(this, getString(R.string.dialog_add_item_toast_success), Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}