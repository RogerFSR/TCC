package com.example.teste.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.R
import com.example.teste.database.ItemVenda

class VendaAdapter(private val listaDeItens: List<ItemVenda>) :
    RecyclerView.Adapter<VendaAdapter.VendaViewHolder>() {

    class VendaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNomeProduto: TextView = itemView.findViewById(R.id.nomeProduto)
        val txtQuantidade: TextView = itemView.findViewById(R.id.quantidade)
        val txtPreco: TextView = itemView.findViewById(R.id.precoTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venda, parent, false)
        return VendaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendaViewHolder, position: Int) {
        val item = listaDeItens[position]
        holder.txtNomeProduto.text = item.codigobarras // ou item.nome, se existir
        holder.txtQuantidade.text = item.quantidade.toString()
        holder.txtPreco.text = "R$ %.2f".format(item.valorUnitario)
    }

    override fun getItemCount() = listaDeItens.size
}