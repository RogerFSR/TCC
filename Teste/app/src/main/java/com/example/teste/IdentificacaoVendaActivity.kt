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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        db = AppDatabase.getDatabase(this)

        btnCadastrarCliente.setOnClickListener {
            val intent = Intent(this, NovoClienteActivity::class.java)
            startActivity(intent)
        }

        btnContinuar.setOnClickListener {
            val funcionarioId = funcionarioInput.text.toString().toIntOrNull()
            val cpfCliente = cpfInput.text.toString()

            if (funcionarioId == null || cpfCliente.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificar se o funcionário e cliente existem no banco
            lifecycleScope.launch {
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

                val currentDateTime = Date()
                val data = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDateTime)
                val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(currentDateTime)

                val intent = Intent(this@IdentificacaoVendaActivity, RegistroVendasActivity::class.java).apply {
                    putExtra("funcionario_id", funcionarioId)
                    putExtra("cpf_cliente", cpfCliente)
                    putExtra("data_venda", data)
                    putExtra("hora_venda", hora)
                }

                startActivity(intent)
            }
        }
    }
}
