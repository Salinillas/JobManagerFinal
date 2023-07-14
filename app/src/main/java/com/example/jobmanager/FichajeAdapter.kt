package com.example.jobmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FichajeAdapter : RecyclerView.Adapter<FichajeAdapter.FichajeViewHolder>() {

    private val fichajes: MutableList<Fichaje> = mutableListOf()

    fun setFichajes(fichajes: List<Fichaje>) {
        this.fichajes.clear()
        this.fichajes.addAll(fichajes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FichajeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fichaje, parent, false)
        return FichajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: FichajeViewHolder, position: Int) {
        val fichaje = fichajes[position]
        holder.bind(fichaje)
    }

    override fun getItemCount(): Int {
        return fichajes.size
    }

    class FichajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        private val oficinaTextView: TextView = itemView.findViewById(R.id.oficinaTextView)
        private val entradaTextView: TextView = itemView.findViewById(R.id.entradaTextView)
        private val salidaTextView: TextView = itemView.findViewById(R.id.salidaTextView)
        private val horasTextView: TextView = itemView.findViewById(R.id.horasTextView)

        fun bind(fichaje: Fichaje) {
            usernameTextView.text = "Username: ${fichaje.username}"
            oficinaTextView.text = "Oficina: ${fichaje.oficina}"
            entradaTextView.text = "Entrada: ${fichaje.fechaEntrada}"
            salidaTextView.text = "Salida: ${fichaje.fechaSalida}"
            horasTextView.text = "Horas trabajadas: ${fichaje.horasTrabajadas}"
        }
    }
}