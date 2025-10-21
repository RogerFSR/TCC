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
        val tvProdutoNome: TextView = itemView.findViewById(R.id.tvProdutoNome)
        val tvPrecoUnitario: TextView = itemView.findViewById(R.id.tvPrecoUnitario)
        val tvQuantidade: TextView = itemView.findViewById(R.id.tvQuantidade)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venda, parent, false)
        return VendaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendaViewHolder, position: Int) {
        val item = listaDeItens[position]

        // Aqui você pode ajustar de acordo com os campos reais do seu ItemVenda
        holder.tvProdutoNome.text = "Produto: ${item.codigobarras}"
        holder.tvPrecoUnitario.text = "Preço Unitário: R$ %.2f".format(item.valorUnitario)
        holder.tvQuantidade.text = "Quantidade: ${item.quantidade}"

        val total = item.valorUnitario * item.quantidade
        holder.tvTotal.text = "Total: R$ %.2f".format(total)
    }

    override fun getItemCount() = listaDeItens.size
}