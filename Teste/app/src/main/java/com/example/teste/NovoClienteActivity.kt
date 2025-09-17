package com.example.teste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.teste.database.AppDatabase
import com.example.teste.database.Cliente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NovoClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.novo_cliente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnInfosToMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnNewToMain = findViewById<Button>(R.id.voltar_btn)
        btnNewToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val db = AppDatabase.getDatabase(this)
        val clienteDAO = db.clienteDAO()

        val nomeInput = findViewById<EditText>(R.id.nomeCliente)
        val cpfInput = findViewById<EditText>(R.id.cpfCliente)
        val telInput = findViewById<EditText>(R.id.telClienteText8)
        val telInput2 = findViewById<EditText>(R.id.telCliente2)
        val endInput = findViewById<EditText>(R.id.endCliente)
        val bairroInput = findViewById<EditText>(R.id.bairroCliente)
        val cepInput = findViewById<EditText>(R.id.cepCliente)
        val cidadeInput = findViewById<EditText>(R.id.cidCliente)
        val emailInput = findViewById<EditText>(R.id.emailCliente)

        val btnSalvar = findViewById<Button>(R.id.btnSalvarCliente)

        btnSalvar.setOnClickListener {
            val cliente = Cliente(
                nome = nomeInput.text.toString(),
                cpf = cpfInput.text.toString(),
                telefone = telInput.text.toString(),
                telefone2 = telInput2.text.toString(),
                endereco = endInput.text.toString(),
                bairro = bairroInput.text.toString(),
                cep = cepInput.text.toString(),
                cidade = cidadeInput.text.toString(),
                email = emailInput.text.toString()
            )


            lifecycleScope.launch(Dispatchers.IO) {
                clienteDAO.insert(cliente)

                runOnUiThread {
                    Toast.makeText(this@NovoClienteActivity, "Cliente cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    nomeInput.text.clear()
                    cpfInput.text.clear()
                    telInput.text.clear()
                    telInput2.text.clear()
                    endInput.text.clear()
                    bairroInput.text.clear()
                    cepInput.text.clear()
                    cidadeInput.text.clear()
                    emailInput.text.clear()
                }
            }
        }
    }
}
