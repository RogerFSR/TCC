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

class FornecedoresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fornecedores)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnFornToAdd = findViewById<Button>(R.id.add_fornecedor)
        btnFornToAdd.setOnClickListener {
            val intent = Intent(this, NovoFornecedorActivity::class.java)
            startActivity(intent)
        }

        val btnFornToList = findViewById<Button>(R.id.listar_fornecedor)
        btnFornToList.setOnClickListener {
            val intent = Intent(this, NovoFornecedorActivity::class.java)
            startActivity(intent)
        }
    }
}