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
import com.vasquez.modaapp.databinding.ActivityPedidosBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PedidosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidosBinding
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DbHelper(this)

        binding.rvPedidos.layoutManager = LinearLayoutManager(this)

        cargarPedidos()

        binding.btnGuardarPedido.setOnClickListener {
            guardarPedido()
        }
    }

    private fun guardarPedido() {
        val cliente = binding.etPedidoCliente.text.toString().trim()
        val prenda = binding.etPedidoPrenda.text.toString().trim()
        val cantidadStr = binding.etPedidoCantidad.text.toString().trim()
        val totalStr = binding.etPedidoTotal.text.toString().trim()

        if (cliente.isEmpty() || prenda.isEmpty() || cantidadStr.isEmpty() || totalStr.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val cantidad = cantidadStr.toIntOrNull() ?: 1
        val total = totalStr.toDoubleOrNull() ?: 0.0
        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        val resultado = dbHelper.insertarPedido(cliente, prenda, cantidad, total, fechaActual)

        if (resultado != -1L) {
            Toast.makeText(this, "Pedido registrado exitosamente", Toast.LENGTH_SHORT).show()
            limpiarFormulario()
            cargarPedidos()
        } else {
            Toast.makeText(this, "Error al registrar el pedido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarPedidos() {
        val listaPedidos = dbHelper.obtenerTodosLosPedidos()
        binding.rvPedidos.adapter = AdaptadorPedidos(listaPedidos)
    }

    private fun limpiarFormulario() {
        binding.etPedidoCliente.setText("")
        binding.etPedidoPrenda.setText("")
        binding.etPedidoCantidad.setText("")
        binding.etPedidoTotal.setText("")
        binding.etPedidoCliente.clearFocus()
    }

    class AdaptadorPedidos(private val lista: List<Map<String, Any>>) :
        RecyclerView.Adapter<AdaptadorPedidos.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvCliente: TextView = view.findViewById(R.id.tvItemPedidoCliente)
            val tvPrenda: TextView = view.findViewById(R.id.tvItemPedidoPrenda)
            val tvFecha: TextView = view.findViewById(R.id.tvItemPedidoFecha)
            val tvCantidad: TextView = view.findViewById(R.id.tvItemPedidoCantidad)
            val tvTotal: TextView = view.findViewById(R.id.tvItemPedidoTotal)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pedido, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = lista[position]
            holder.tvCliente.text = "Cliente: ${item["cliente"]}"
            holder.tvPrenda.text = "Prenda: ${item["prenda"]}"
            holder.tvFecha.text = item["fecha"].toString()
            holder.tvCantidad.text = "Cant: ${item["cantidad"]}"
            holder.tvTotal.text = "Total: S/ ${item["total"]}"
        }

        override fun getItemCount(): Int = lista.size
    }
}