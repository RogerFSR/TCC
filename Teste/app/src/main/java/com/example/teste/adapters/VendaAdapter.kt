package com.example.teste.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.R
import com.example.teste.database.ProdutoVendido
import java.text.NumberFormat
import java.util.*

class VendaAdapter(
    private val itens: MutableList<ProdutoVendido>,
    private val onQuantityChanged: (ProdutoVendido) -> Unit
) : RecyclerView.Adapter<VendaAdapter.VendaViewHolder>() {

    private val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt","BR"))

    inner class VendaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNome: TextView = itemView.findViewById(R.id.txtNomeVendaItem)
        val txtDetalhe: TextView = itemView.findViewById(R.id.txtNomeVendaItem)
        val btnAdd: Button = itemView.findViewById(R.id.btnAddQuantidade)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemoveQuantidade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_venda, parent, false)
        return VendaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendaViewHolder, position: Int) {
        val item = itens[position]
        // item.valorvenda e item.qtd e item.codigobarras e item.subtotal
        holder.txtNome.text = item.codigobarras // substitua por nome se disponível (veja nota)
        holder.txtDetalhe.text = "${item.qtd} x ${currencyFormatter.format(item.valorvenda)} = ${currencyFormatter.format(item.subtotal)}"

        holder.btnAdd.setOnClickListener {
            item.qtd = item.qtd + 1
            item.subtotal = item.qtd * item.valorvenda
            notifyItemChanged(position)
            onQuantityChanged(item)
        }

        holder.btnRemove.setOnClickListener {
            if (item.qtd > 1) {
                item.qtd = item.qtd - 1
                item.subtotal = item.qtd * item.valorvenda
                notifyItemChanged(position)
                onQuantityChanged(item)
            }
        }
    }

    override fun getItemCount(): Int = itens.size
}
