package com.vasquez.modaapp

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vasquez.modaapp.databinding.ActivityCatalogoBinding

class CatalogoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogoBinding
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DbHelper(this)

        binding.rvCatalogo.layoutManager = LinearLayoutManager(this)
        cargarCatalogo()
    }

    private fun cargarCatalogo() {
        val listaPrendas = dbHelper.obtenerTodasLasPrendas()
        binding.rvCatalogo.adapter = AdaptadorRopa(listaPrendas)
    }

    // Adaptador interno para el RecyclerView
    class AdaptadorRopa(private val lista: List<Map<String, Any>>) :
        RecyclerView.Adapter<AdaptadorRopa.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivPrenda: ImageView = view.findViewById(R.id.ivItemPrenda)
            val tvNombre: TextView = view.findViewById(R.id.tvItemNombre)
            val tvCategoria: TextView = view.findViewById(R.id.tvItemCategoria)
            val tvPrecio: TextView = view.findViewById(R.id.tvItemPrecio)
            val tvStock: TextView = view.findViewById(R.id.tvItemStock)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_prenda, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = lista[position]
            holder.tvNombre.text = item["nombre"].toString()
            holder.tvCategoria.text = item["categoria"].toString()
            holder.tvPrecio.text = "S/ ${item["precio"]}"
            holder.tvStock.text = "Stock: ${item["stock"]}"

            val uriStr = item["imagenUri"].toString()
            if (uriStr.isNotEmpty()) {
                holder.ivPrenda.setImageURI(Uri.parse(uriStr))
            } else {
                holder.ivPrenda.setImageResource(android.R.drawable.ic_menu_camera)
            }
        }

        override fun getItemCount(): Int = lista.size
    }
}