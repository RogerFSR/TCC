package com.example.teste.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.R
import com.example.teste.database.Venda
import com.example.teste.utils.PdfHelper
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_relatorio, parent, false)
        return VendaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendaViewHolder, position: Int) {
        val venda = listaVendas[position]

        holder.textDataHora.text = "Data/Hora: ${venda.data} ${venda.hora}"
        holder.textFuncionario.text = "Funcionário ID: ${venda.funcionario}"
        holder.textCliente.text = "Cliente CPF: ${venda.cliente}"
        holder.textPagou.text = "Pagou: ${if (venda.pagou) "Sim" else "Não"}"

        holder.btnImprimir.setOnClickListener {
            val context = holder.itemView.context
            val vendaAtual = venda
            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                PdfHelper.gerarPdfVendaPublicDocuments(context, vendaAtual)
            }
        }

    }

    override fun getItemCount(): Int = listaVendas.size
}
