package com.example.teste.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.R
import com.example.teste.database.Venda

class RelatorioVendaAdapter(
    private val listaVendas: List<Venda>
) : RecyclerView.Adapter<RelatorioVendaAdapter.VendaViewHolder>() {

    class VendaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDataHora: TextView = itemView.findViewById(R.id.textDataHora)
        val textFuncionario: TextView = itemView.findViewById(R.id.textFuncionario)
        val textCliente: TextView = itemView.findViewById(R.id.textCliente)
        val textPagou: TextView = itemView.findViewById(R.id.textPagou)
        val btnImprimir: MaterialButton = itemView.findViewById(R.id.btnImprimir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_relatorio, parent, false)
        return VendaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendaViewHolder, position: Int) {
        val venda = listaVendas[position]

        holder.textDataHora.text = "Data/Hora: ${venda.data} ${venda.hora}"
        holder.textFuncionario.text = "Funcionário ID: ${venda.funcionario}"
        holder.textCliente.text = "Cliente CPF: ${venda.cliente}"
        holder.textPagou.text = "Pagou: ${if (venda.pagou) "Sim" else "Não"}"

        holder.btnImprimir.setOnClickListener {
            // TODO: Implementar função de impressão para esta venda
            // printVenda(venda)
        }
    }

    override fun getItemCount(): Int = listaVendas.size
}
