package com.example.teste

import android.content.Intent
import com.example.teste.database.AppDatabase
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.teste.database.Funcionario
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    private fun setupMainLayout() {
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnInfosToMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnMainToRegister = findViewById<Button>(R.id.Registrar_Venda)
        btnMainToRegister.setOnClickListener {
            val intent = Intent(this, IdentificacaoVendaActivity::class.java)
            startActivity(intent)
        }

        val btnMainToNew = findViewById<Button>(R.id.novo_cliente)
        btnMainToNew.setOnClickListener {
            val intent = Intent(this, NovoClienteActivity::class.java)
            startActivity(intent)
        }

        val btnMainToList = findViewById<Button>(R.id.listar_clientes)
        btnMainToList.setOnClickListener {
            val intent = Intent(this, InfoClientesActivity::class.java)
            startActivity(intent)
        }

        val btnGenerateReport = findViewById<Button>(R.id.gerar_recibo)
        btnGenerateReport.setOnClickListener{

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
