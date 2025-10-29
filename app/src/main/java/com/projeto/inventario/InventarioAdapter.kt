package com.projeto.inventario

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InventarioAdapter(private val listaDeInventarios: List<Inventario>) :
    RecyclerView.Adapter<InventarioAdapter.InventarioViewHolder>() {

    class InventarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeInventario: TextView = itemView.findViewById(R.id.tvNomeInventario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventario, parent, false)
        return InventarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: InventarioViewHolder, position: Int) {
        val inventarioAtual = listaDeInventarios[position]
        holder.nomeInventario.text = inventarioAtual.nome

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val intent = Intent(context, SetoresActivity::class.java)

            intent.putExtra("INVENTARIO_ID", inventarioAtual.id)
            intent.putExtra("INVENTARIO_NOME", inventarioAtual.nome)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listaDeInventarios.size
    }
}