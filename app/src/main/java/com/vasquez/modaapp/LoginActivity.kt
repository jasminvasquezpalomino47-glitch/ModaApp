package com.vasquez.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.modaapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIngresar.setOnClickListener {
            if (validarCampos()) {
                val usuario = binding.etUsuario.text.toString().trim()
                val clave = binding.etClave.text.toString().trim()

                // Validamos credenciales iniciales para el Sprint 1
                if (usuario == "admin" && clave == "1234") {
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                    finish() // Cierra el Login
                } else {
                    Toast.makeText(this, getString(R.string.err_credenciales), Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnVerCatalogo.setOnClickListener {
            val intent = Intent(this, CatalogoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validarCampos(): Boolean {
        var valido = true
        binding.tilUsuario.error = null
        binding.tilClave.error = null

        if (binding.etUsuario.text.toString().trim().isEmpty()) {
            binding.tilUsuario.error = getString(R.string.err_campo_requerido)
            valido = false
        }
        if (binding.etClave.text.toString().trim().isEmpty()) {
            binding.tilClave.error = getString(R.string.err_campo_requerido)
            valido = false
        }
        return valido
    }
}