package com.projeto.inventario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(
    private val listaDeItens: List<Item>,
    private val onLongClick: (Item) -> Unit
) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeItem: TextView = itemView.findViewById(R.id.tvItemNome)
        val modeloItem: TextView = itemView.findViewById(R.id.tvItemModelo)
        val quantidadeItem: TextView = itemView.findViewById(R.id.tvItemQuantidade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val itemAtual = listaDeItens[position]
        val context = holder.itemView.context

        holder.nomeItem.text = itemAtual.nome
        holder.modeloItem.text = context.getString(R.string.item_label_modelo, itemAtual.modelo)
        holder.quantidadeItem.text = itemAtual.quantidade.toString()

        holder.itemView.setOnLongClickListener {
            onLongClick(itemAtual)
            true
        }
    }

    override fun getItemCount(): Int {
        return listaDeItens.size
    }
}