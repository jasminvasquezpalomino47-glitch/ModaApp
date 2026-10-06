package com.vasquez.modaapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vasquez.modaapp.databinding.ActivityReportesBinding

class ReportesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportesBinding
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DbHelper(this)

        binding.btnVolverReportes.setOnClickListener {
            finish()
        }

        binding.rvStockReporte.layoutManager = LinearLayoutManager(this)

        cargarDatosReporte()
    }

    private fun cargarDatosReporte() {
        // Cargar total vendido
        val total = dbHelper.obtenerTotalVendido()
        binding.tvTotalVendidoMonto.text = String.format("S/ %.2f", total)

        // Cargar cantidad de pedidos
        val atendidos = dbHelper.obtenerCantidadPedidos()
        binding.tvCantidadAtendidos.text = atendidos.toString()
        binding.tvCantidadPendientes.text = "0"

        // Cargar lista de stock por prenda
        val listaStock = dbHelper.obtenerStockPrendas()
        binding.rvStockReporte.adapter = AdaptadorStock(listaStock)
    }

    class AdaptadorStock(private val lista: List<Map<String, Any>>) :
        RecyclerView.Adapter<AdaptadorStock.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNombre: TextView = view.findViewById(R.id.tvNombrePrendaReporte)
            val pbStock: ProgressBar = view.findViewById(R.id.pbStockPrenda)
            val tvNumero: TextView = view.findViewById(R.id.tvStockNumero)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_reporte, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = lista[position]
            val stock = item["stock"].toString().toIntOrNull() ?: 0

            holder.tvNombre.text = item["nombre"].toString()
            holder.tvNumero.text = stock.toString()
            holder.pbStock.progress = stock.coerceAtMost(100)
        }

        override fun getItemCount(): Int = lista.size
    }
}