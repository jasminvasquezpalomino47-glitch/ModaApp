package com.vasquez.modaapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.vasquez.modaapp.databinding.ActivityRopaBinding

class RopaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRopaBinding
    private lateinit var dbHelper: DbHelper
    private var imagenUriSeleccionada: Uri? = null

    // Lanzador para seleccionar imagen desde la galería con permisos persistentes
    private val seleccionarImagenLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            // Otorgar permiso persistente para leer la imagen en el catálogo
            val contentResolver = applicationContext.contentResolver
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
            try {
                contentResolver.takePersistableUriPermission(it, takeFlags)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            imagenUriSeleccionada = it
            binding.ivPrenda.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRopaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DbHelper(this)

        // Evento para abrir el selector de imágenes
        binding.btnSeleccionarImagen.setOnClickListener {
            seleccionarImagenLauncher.launch(arrayOf("image/*"))
        }

        // Evento para guardar prenda
        binding.btnGuardarPrenda.setOnClickListener {
            guardarPrenda()
        }
    }

    private fun guardarPrenda() {
        val nombre = binding.etNombrePrenda.text.toString().trim()
        val categoria = binding.etCategoriaPrenda.text.toString().trim()
        val precioStr = binding.etPrecioPrenda.text.toString().trim()
        val stockStr = binding.etStockPrenda.text.toString().trim()

        if (nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Por favor completa los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val precio = precioStr.toDoubleOrNull() ?: 0.0
        val stock = stockStr.toIntOrNull() ?: 0
        val imagenUriString = imagenUriSeleccionada?.toString()

        val resultado = dbHelper.insertarRopa(nombre, categoria, precio, stock, imagenUriString)

        if (resultado != -1L) {
            Toast.makeText(this, "Prenda registrada correctamente", Toast.LENGTH_SHORT).show()
            limpiarFormulario()
        } else {
            Toast.makeText(this, "Error al registrar la prenda", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarFormulario() {
        binding.etNombrePrenda.setText("")
        binding.etCategoriaPrenda.setText("")
        binding.etPrecioPrenda.setText("")
        binding.etStockPrenda.setText("")
        binding.ivPrenda.setImageResource(android.R.drawable.ic_menu_camera)
        imagenUriSeleccionada = null
    }
}