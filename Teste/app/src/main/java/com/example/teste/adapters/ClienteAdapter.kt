package com.example.teste.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.R
import com.example.teste.database.Cliente

class ClienteAdapter(
    private val clientes: List<Cliente>,
    private val onEditarClickListener: (Cliente) -> Unit,
    private val onExcluirClickListener: (Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNome: TextView = itemView.findViewById(R.id.txtNome)
        val txtCpf: TextView = itemView.findViewById(R.id.txtCpf)
        val txtEndereco: TextView = itemView.findViewById(R.id.txtEndereco)
        val txtTelefone: TextView = itemView.findViewById(R.id.txtTelefone)
        val txtEmail: TextView = itemView.findViewById(R.id.txtEmail)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnExcluir: Button = itemView.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clitente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]

        holder.txtNome.text = cliente.nome
        holder.txtCpf.text = cliente.cpf
        holder.txtEndereco.text = "${cliente.endereco}, ${cliente.bairro}, ${cliente.cidade} - CEP: ${cliente.cep}"
        holder.txtTelefone.text = "Tel: ${cliente.telefone} / ${cliente.telefone2}"
        holder.txtEmail.text = cliente.email

        holder.btnEditar.setOnClickListener { onEditarClickListener(cliente) }
        holder.btnExcluir.setOnClickListener { onExcluirClickListener(cliente) }
    }

    override fun getItemCount() = clientes.size
}