package com.projeto.inventario

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SetoresActivity : AppCompatActivity() {

    private var inventarioId: String? = null
    private var inventarioNome: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setores)

        inventarioId = intent.getStringExtra("INVENTARIO_ID")
        inventarioNome = intent.getStringExtra("INVENTARIO_NOME")

        if (inventarioId == null || inventarioNome == null) {
            Toast.makeText(this, getString(R.string.setores_error_loading), Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val tvNomeInventarioHeader = findViewById<TextView>(R.id.tvNomeInventarioHeader)

        tvNomeInventarioHeader.text = inventarioNome
    }
}