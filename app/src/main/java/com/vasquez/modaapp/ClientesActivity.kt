package com.vasquez.modaapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vasquez.modaapp.databinding.ActivityClientesBinding

class ClientesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientesBinding
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DbHelper(this)

        binding.rvClientes.layoutManager = LinearLayoutManager(this)

        // Cargar lista al iniciar
        cargarClientes()

        // Evento para guardar cliente
        binding.btnGuardarCliente.setOnClickListener {
            guardarCliente()
        }
    }

    private fun guardarCliente() {
        val dni = binding.etDniCliente.text.toString().trim()
        val nombre = binding.etNombreCliente.text.toString().trim()
        val telefono = binding.etTelefonoCliente.text.toString().trim()

        if (dni.isEmpty() || nombre.isEmpty()) {
            Toast.makeText(this, "El DNI y el Nombre son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val resultado = dbHelper.insertarCliente(dni, nombre, telefono)

        if (resultado != -1L) {
            Toast.makeText(this, "Cliente registrado correctamente", Toast.LENGTH_SHORT).show()
            limpiarFormulario()
            cargarClientes()
        } else {
            Toast.makeText(this, "Error al registrar el cliente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarClientes() {
        val listaClientes = dbHelper.obtenerTodosLosClientes()
        binding.rvClientes.adapter = AdaptadorClientes(listaClientes)
    }

    private fun limpiarFormulario() {
        binding.etDniCliente.setText("")
        binding.etNombreCliente.setText("")
        binding.etTelefonoCliente.setText("")
        binding.etDniCliente.clearFocus()
    }

    // Adaptador interno para el RecyclerView de Clientes
    class AdaptadorClientes(private val lista: List<Map<String, Any>>) :
        RecyclerView.Adapter<AdaptadorClientes.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNombre: TextView = view.findViewById(R.id.tvItemClienteNombre)
            val tvDni: TextView = view.findViewById(R.id.tvItemClienteDni)
            val tvTelefono: TextView = view.findViewById(R.id.tvItemClienteTelefono)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cliente, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = lista[position]
            holder.tvNombre.text = item["nombre"].toString()
            holder.tvDni.text = "DNI: ${item["dni"]}"
            holder.tvTelefono.text = "Tel: ${item["telefono"]}"
        }

        override fun getItemCount(): Int = lista.size
    }
}