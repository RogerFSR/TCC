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
import com.example.teste.database.Funcionario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NovoFuncionarioActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var nomeInput: EditText
    private lateinit var telInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.novo_funcionario)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.getDatabase(this)

        nomeInput = findViewById(R.id.nomeFuncionario)
        telInput = findViewById(R.id.telFuncionario)

        val btnSalvar = findViewById<Button>(R.id.btnSalvarFuncionario)
        val btnVoltar = findViewById<Button>(R.id.voltar_btn)

        btnVoltar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        btnSalvar.setOnClickListener {
            val nome = nomeInput.text.toString()
            val telefone = telInput.text.toString()

            if (nome.isBlank()) {
                Toast.makeText(this, "Nome é obrigatório!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val telLimpo = telefone.replace(Regex("[^\\d+]"), "")
            if (!telLimpo.matches(Regex("^\\+?[0-9]{8,15}\$"))) {
                Toast.makeText(this, "Telefone inválido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val funcionario = Funcionario(
                registro = 0, // autogerado pelo Room
                nome = nome,
                telefone = telefone
            )

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    db.funcionarioDAO().insert(funcionario)
                    runOnUiThread {
                        Toast.makeText(
                            this@NovoFuncionarioActivity,
                            "Funcionário cadastrado com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()
                        limparCampos()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(
                            this@NovoFuncionarioActivity,
                            "Erro ao salvar funcionário: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun limparCampos() {
        nomeInput.text.clear()
        telInput.text.clear()
    }
}
