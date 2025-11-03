package com.example.teste

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
import com.example.teste.database.Tipo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditarTipoActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var codInput: EditText
    private lateinit var descInput: EditText
    private var codTipo: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.editar_tipo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.getDatabase(this)

        codInput = findViewById(R.id.codTipoEdit)
        descInput = findViewById(R.id.descricaoTipoEdit)
        val btnSalvar = findViewById<Button>(R.id.btnSalvarTipoEdit)
        val btnVoltar = findViewById<Button>(R.id.btnVoltarEdit)

        codTipo = intent.getIntExtra("COD_TIPO", -1)

        if (codTipo == -1) {
            Toast.makeText(this, "Erro ao carregar tipo!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            carregarTipo()
        }

        btnVoltar.setOnClickListener {
            finish()
        }

        btnSalvar.setOnClickListener {
            salvarAlteracoes()
        }
    }

    private fun carregarTipo() {
        lifecycleScope.launch(Dispatchers.IO) {
            val tipo = db.tipoDAO().getTipoById(codTipo)
            tipo?.let {
                runOnUiThread {
                    codInput.setText(it.codtipo.toString())
                    descInput.setText(it.descricao)
                }
            }
        }
    }

    private fun salvarAlteracoes() {
        val novaDesc = descInput.text.toString().trim()

        if (novaDesc.isEmpty()) {
            Toast.makeText(this, "Descrição não pode ser vazia!", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            db.tipoDAO().update(Tipo(codTipo, novaDesc))
            runOnUiThread {
                Toast.makeText(this@EditarTipoActivity, "Tipo atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

}
