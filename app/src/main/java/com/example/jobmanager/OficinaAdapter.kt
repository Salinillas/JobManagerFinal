package com.example.jobmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobmanager.Database.DatabaseHandler

class OficinaAdapter(
    private var oficinas: List<Oficina>,
    private val databaseHandler: DatabaseHandler
) : RecyclerView.Adapter<OficinaAdapter.OficinaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OficinaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_oficina, parent, false)
        return OficinaViewHolder(view)
    }

    override fun onBindViewHolder(holder: OficinaViewHolder, position: Int) {
        val oficina = oficinas[position]
        holder.bind(oficina)
    }

    override fun getItemCount(): Int {
        return oficinas.size
    }

    fun setOficinas(oficinas: List<Oficina>) {
        this.oficinas = oficinas
        notifyDataSetChanged()
    }

    fun setOficinasOcupadas(oficinas: List<Oficina>) {
        this.oficinas = oficinas
        notifyDataSetChanged()
    }

    inner class OficinaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        private val usuarioTextView: TextView = itemView.findViewById(R.id.usuarioTextView)

        fun bind(oficina: Oficina) {
            nombreTextView.text = oficina.nombre
            if (oficina.ocupada) {
                usuarioTextView.visibility = View.VISIBLE
                usuarioTextView.text = "Usuario: ${((databaseHandler.buscarFichajeUsuarioConFechaEntradaMasAlta(
                    databaseHandler.getCurrentUser()!!.username))!!.username)}"
            } else {
                usuarioTextView.visibility = View.GONE
            }
        }
    }
}