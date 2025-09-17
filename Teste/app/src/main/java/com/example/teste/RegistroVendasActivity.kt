package com.example.teste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.adapters.VendaAdapter
import com.example.teste.database.AppDatabase
import com.example.teste.database.ItemVenda
import com.example.teste.database.ProdutoVendido
import com.example.teste.database.Venda
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegistroVendasActivity : AppCompatActivity() {

    // Simulação de dados recebidos via Intent
    private var idFuncionario: Int = 1
    private var cpfCliente: String = "00000000000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.registro_vendas)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnInfosToMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botão de voltar ao menu principal
        val btnListToMain = findViewById<Button>(R.id.sale_to_main)
        btnListToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Botão de finalizar venda
        val btnFinalizar = findViewById<Button>(R.id.confirm_sale)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerItensVenda)
        val listaDeItens = mutableListOf<ItemVenda>()

        // Exemplo de item
        listaDeItens.add(ItemVenda("7891000055128", 2, 12.99))  // código de barras, qtd, valor unitário

        val adapter = VendaAdapter(listaDeItens)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.notifyDataSetChanged()

        btnFinalizar.setOnClickListener {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(this@RegistroVendasActivity)
                val vendaDao = db.vendaDAO()
                val produtoVendidoDao = db.produtoVendidoDAO()

                val dataAtual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val horaAtual = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                // 1. Criar e salvar a venda
                val venda = Venda(
                    nVenda = 0,
                    data = dataAtual,
                    hora = horaAtual,
                    funcionario = idFuncionario,
                    cliente = cpfCliente,
                    pagou = false
                )
                val idVenda = vendaDao.insertAndReturnId(venda)  // esse método você precisa implementar

                // 2. Salvar os produtos vendidos
                for (item in listaDeItens) {
                    val subtotal = item.quantidade * item.valorUnitario

                    val produtoVendido = ProdutoVendido(
                        nVenda = idVenda.toInt(),
                        codigobarras = item.codigobarras,
                        qtd = item.quantidade,
                        valorvenda = item.valorUnitario,
                        subtotal = subtotal
                    )
                    produtoVendidoDao.insert(produtoVendido)
                }

                Toast.makeText(this@RegistroVendasActivity, "Venda registrada com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
