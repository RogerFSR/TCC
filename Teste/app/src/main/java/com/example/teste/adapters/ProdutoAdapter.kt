package com.example.teste.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.R
import com.example.teste.database.Produto

class ProdutoAdapter(
    private var produtos: List<Produto>,
    private val onEditarClickListener: (Produto) -> Unit,
    private val onExcluirClickListener: (Produto) -> Unit
) : RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    inner class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNome: TextView = itemView.findViewById(R.id.txtNomeProduto)
        val txtCodBarra: TextView = itemView.findViewById(R.id.txtCodBarra)
        val txtEstoque: TextView = itemView.findViewById(R.id.txtEstoque)
        val txtTipoFornecedor: TextView = itemView.findViewById(R.id.txtTipoFornecedor)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditarProduto)
        val btnExcluir: Button = itemView.findViewById(R.id.btnExcluirProduto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = produtos[position]
        holder.txtNome.text = produto.nome
        holder.txtCodBarra.text = produto.codigobarras
        holder.txtEstoque.text = "Estoque: ${produto.estoque_atual}/${produto.estoque_max}"
        holder.txtTipoFornecedor.text = "Tipo: ${produto.tipo} | Fornecedor: ${produto.fornecedor}"

        holder.btnEditar.setOnClickListener { onEditarClickListener(produto) }
        holder.btnExcluir.setOnClickListener { onExcluirClickListener(produto) }
    }

    override fun getItemCount(): Int = produtos.size
}
