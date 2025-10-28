package com.projeto.inventario // Certifica-te que este é o teu pacote

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        // 1. Encontrar os componentes
        val etEmailCadastro = findViewById<EditText>(R.id.etEmailCadastro)
        val etPasswordCadastro = findViewById<EditText>(R.id.etPasswordCadastro)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val tvGoToLogin = findViewById<TextView>(R.id.tvGoToLogin)

        // 2. Definir o que acontece ao clicar no botão "Sign Up"
        btnSignUp.setOnClickListener {
            val email = etEmailCadastro.text.toString()
            val password = etPasswordCadastro.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            // Verificações simples
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                // CORRIGIDO: Usa a string
                Toast.makeText(this, getString(R.string.login_error_empty_fields), Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                // CORRIGIDO: Usa a string
                Toast.makeText(this, getString(R.string.cadastro_error_password_mismatch), Toast.LENGTH_SHORT).show()
            } else {
                // Lógica de cadastro (ex: Firebase) viria aqui
                // CORRIGIDO: Usa a string
                Toast.makeText(this, getString(R.string.cadastro_success_simulation), Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }

        // 3. Definir o que acontece ao clicar no link "Sign In"
        tvGoToLogin.setOnClickListener {
            finish()
        }
    }
}