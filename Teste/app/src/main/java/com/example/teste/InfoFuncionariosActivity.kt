package com.example.teste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.adapter.FuncionarioAdapter
import com.example.teste.database.AppDatabase
import kotlinx.coroutines.launch

class InfoFuncionariosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.info_funcionarios)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnInfosToMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.funcionariosRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = FuncionarioAdapter(emptyList())
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val funcionarios = db.funcionarioDAO().getAllFuncionarios()
            recyclerView.adapter = FuncionarioAdapter(funcionarios)
        }

        val btnVoltar = findViewById<Button>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
