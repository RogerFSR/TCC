package com.example.teste.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.R
import com.example.teste.database.Funcionario

class FuncionarioAdapter(private val funcionarios: List<Funcionario>) :
    RecyclerView.Adapter<FuncionarioAdapter.FuncionarioViewHolder>() {

    class FuncionarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.nomeFuncionario)
        val telefone: TextView = itemView.findViewById(R.id.telefoneFuncionario)
        val registro: TextView = itemView.findViewById(R.id.registroFuncionario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuncionarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_funcionario, parent, false)
        return FuncionarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: FuncionarioViewHolder, position: Int) {
        val funcionario = funcionarios[position]
        holder.nome.text = funcionario.nome
        holder.telefone.text = funcionario.telefone
        holder.registro.text = "Registro: ${funcionario.registro}"
    }

    override fun getItemCount() = funcionarios.size
}
