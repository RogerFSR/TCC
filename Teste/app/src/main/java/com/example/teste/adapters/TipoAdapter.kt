package com.example.teste.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.EditarTipoActivity
import com.example.teste.R
import com.example.teste.database.Tipo

class TipoAdapter(
    private val listaTipos: List<Tipo>,
    private val context: Context,
    private val onDeleteClickListener: (Tipo) -> Unit
) : RecyclerView.Adapter<TipoAdapter.TipoViewHolder>() {

    inner class TipoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descricao: TextView = itemView.findViewById(R.id.descricaoTipo)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditarTipo)
        val btnExcluir: Button = itemView.findViewById(R.id.btnExcluirTipo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tipo, parent, false)
        return TipoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TipoViewHolder, position: Int) {
        val tipo = listaTipos[position]
        holder.descricao.text = tipo.descricao

        holder.btnEditar.setOnClickListener {
            val intent = Intent(context, EditarTipoActivity::class.java).apply {
                putExtra("COD_TIPO", tipo.codtipo)
            }
            context.startActivity(intent)
        }

        holder.btnExcluir.setOnClickListener {
            onDeleteClickListener(tipo)
        }
    }

    override fun getItemCount(): Int = listaTipos.size
}
