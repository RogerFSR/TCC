package com.example.teste.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.NovoFornecedorActivity
import com.example.teste.R
import com.example.teste.database.AppDatabase
import com.example.teste.database.Fornecedor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FornecedorAdapter(
    private var listaFornecedores: MutableList<Fornecedor>, // mudou para var
    private val onEditarClickListener: (Fornecedor) -> Unit,
    private val onExcluirClickListener: (Fornecedor) -> Unit
) : RecyclerView.Adapter<FornecedorAdapter.FornecedorViewHolder>() {

    inner class FornecedorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeFornecedor: TextView = itemView.findViewById(R.id.nomeFornecedor)
        val telFornecedor: TextView = itemView.findViewById(R.id.telFornecedor)
        val cidadeFornecedor: TextView = itemView.findViewById(R.id.cidadeFornecedor)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditarFornecedor)
        val btnExcluir: Button = itemView.findViewById(R.id.btnExcluirFornecedor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FornecedorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fornecedor, parent, false)
        return FornecedorViewHolder(view)
    }

    override fun onBindViewHolder(holder: FornecedorViewHolder, position: Int) {
        val fornecedor = listaFornecedores[position]

        holder.nomeFornecedor.text = fornecedor.nome
        holder.telFornecedor.text = "Telefone: ${fornecedor.telefone}"
        holder.cidadeFornecedor.text = "Cidade: ${fornecedor.cidade}"

        holder.btnEditar.setOnClickListener {
            onEditarClickListener(fornecedor)
        }

        holder.btnExcluir.setOnClickListener {
            onExcluirClickListener(fornecedor)
        }
    }

    fun updateData(novaLista: List<Fornecedor>) {
        listaFornecedores.clear()          // limpa a lista atual
        listaFornecedores.addAll(novaLista) // adiciona os novos itens
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listaFornecedores.size
}
