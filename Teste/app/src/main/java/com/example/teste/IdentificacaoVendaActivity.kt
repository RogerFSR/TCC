package com.example.teste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.teste.database.AppDatabase
import kotlinx.coroutines.launch

class IdentificacaoVendaActivity : AppCompatActivity() {

    private lateinit var funcionarioInput: EditText
    private lateinit var cpfInput: EditText
    private lateinit var btnContinuar: Button
    private lateinit var btnCadastrarCliente: Button
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.identificacao_venda)

        // Ajusta padding do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        funcionarioInput = findViewById(R.id.editFuncionario)
        cpfInput = findViewById(R.id.editCpfCliente)
        btnContinuar = findViewById(R.id.btnContinuarVenda)
        btnCadastrarCliente = findViewById(R.id.btnCadastrarCliente)

        val btnInfosToMain = findViewById<Button>(R.id.infos_to_main)
        btnInfosToMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        db = AppDatabase.getDatabase(this)

        btnCadastrarCliente.setOnClickListener {
            startActivity(Intent(this, NovoClienteActivity::class.java))
        }

        btnContinuar.setOnClickListener {
            val funcionarioId = funcionarioInput.text.toString().trim().toIntOrNull()
            val cpfCliente = cpfInput.text.toString().trim().filter { it.isDigit() } // remove máscara

            if (funcionarioId == null) {
                Toast.makeText(this, "ID do funcionário inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (cpfCliente.length != 11) {
                Toast.makeText(this, "CPF do cliente inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Acesso seguro ao banco
            lifecycleScope.launch {
                try {
                    val funcionario = db.funcionarioDAO().getFuncionarioByRegistro(funcionarioId)
                    val cliente = db.clienteDAO().getClienteByCpf(cpfCliente)

                    if (funcionario == null) {
                        Toast.makeText(this@IdentificacaoVendaActivity, "Funcionário não encontrado", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    if (cliente == null) {
                        Toast.makeText(this@IdentificacaoVendaActivity, "Cliente não encontrado. Cadastre antes.", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val intent = Intent(this@IdentificacaoVendaActivity, RegistroVendasActivity::class.java).apply {
                        putExtra("funcionario_id", funcionarioId)
                        putExtra("cpf_cliente", cpfCliente)
                    }
                    startActivity(intent)

                } catch (e: Exception) {
                    Toast.makeText(this@IdentificacaoVendaActivity, "Erro ao acessar banco: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
