package com.example.teste

import android.content.Intent
import com.example.teste.database.AppDatabase
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    private fun setupMainLayout() {
        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnInfosToMain)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val btnMainToRegister = findViewById<Button>(R.id.Registrar_Venda)
        btnMainToRegister.setOnClickListener {
            val intent = Intent(this, IdentificacaoVendaActivity::class.java)
            startActivity(intent)
        }

        val btnMainToNewClient = findViewById<Button>(R.id.novo_cliente)
        btnMainToNewClient.setOnClickListener {
            val intent = Intent(this, NovoClienteActivity::class.java)
            startActivity(intent)
        }

        val btnMainToList = findViewById<Button>(R.id.listar_clientes)
        btnMainToList.setOnClickListener {
            val intent = Intent(this, InfoClientesActivity::class.java)
            startActivity(intent)
        }

        val btnMainToAddFunc = findViewById<Button>(R.id.cadastrar_func)
        btnMainToAddFunc.setOnClickListener {
            val intent = Intent(this, NovoFuncionarioActivity::class.java)
            startActivity(intent)
        }

        val btnGenerateReport = findViewById<Button>(R.id.gerar_recibo)
        btnGenerateReport.setOnClickListener{
            val intent = Intent(this, RelatorioActivity::class.java)
            startActivity(intent)
        }

        val btnListFuncs = findViewById<Button>(R.id.listar_funcs)
        btnListFuncs.setOnClickListener {
            val intent = Intent(this, InfoFuncionariosActivity::class.java)
            startActivity(intent)
        }


        val btnAddProduto = findViewById<Button>(R.id.add_produto)
        btnAddProduto.setOnClickListener {
            val intent = Intent(this, NovoProdutoActivity::class.java)
            startActivity(intent)
        }

        val bntFornecedors = findViewById<Button>(R.id.Fornecedores)
        bntFornecedors.setOnClickListener{
            val intent = Intent(this, FornecedoresActivity::class.java)
            startActivity(intent)
        }

        val bntEstoque = findViewById<Button>(R.id.consultar_estoque)
        bntEstoque.setOnClickListener{
            val intent = Intent(this, InfoEstoqueActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupMainLayout()
        enableEdgeToEdge()

        db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            try {
                // Verifica total de clientes
                val totalClientes = db.clienteDAO().getAllClientes().size

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
