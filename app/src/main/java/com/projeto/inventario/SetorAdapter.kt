package com.projeto.inventario

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SetorAdapter(
    private val listaDeSetores: List<Setor>,
    private val onLongClick: (Setor) -> Unit
) :
    RecyclerView.Adapter<SetorAdapter.SetorViewHolder>() {

    class SetorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeSetor: TextView = itemView.findViewById(R.id.tvSetorNome)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_setor, parent, false)
        return SetorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetorViewHolder, position: Int) {
        val setorAtual = listaDeSetores[position]
        holder.nomeSetor.text = setorAtual.nome

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ItensActivity::class.java)
            intent.putExtra("SETOR_ID", setorAtual.id)
            intent.putExtra("SETOR_NOME", setorAtual.nome)
            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(setorAtual)
            true
        }
    }

    override fun getItemCount(): Int {
        return listaDeSetores.size
    }
}