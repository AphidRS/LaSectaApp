package com.lasectaapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lasectaapp.R
import com.lasectaapp.model.Round
import com.lasectaapp.ui.adapter.MatchesAdapter

class RoundsAdapter(private var rounds: MutableList<Round>) : RecyclerView.Adapter<RoundsAdapter.RoundViewHolder>() {

    // ViewHolder que contiene las vistas de item_round.xml
    class RoundViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roundName: TextView = view.findViewById(R.id.tv_round_name)
        val matchesRecyclerView: RecyclerView = view.findViewById(R.id.rv_matches)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoundViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_round, parent, false)
        return RoundViewHolder(view)
    }

    fun updateData(newRounds: List<Round>) {
        rounds.clear()
        rounds.addAll(newRounds)
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }

    override fun getItemCount(): Int = rounds.size

    override fun onBindViewHolder(holder: RoundViewHolder, position: Int) {
        val round = rounds[position]

        // Asignamos el nombre de la jornada
        holder.roundName.text = "JORNADA ${round.jornada}"

        // 1. Asignar el nombre de la jornada (sin cambios)
        holder.roundName.text = "JORNADA ${round.jornada}"

        // 3. Configurar el RecyclerView anidado (sin cambios)
        holder.matchesRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        val matchesAdapter = MatchesAdapter(round.partidos)
        holder.matchesRecyclerView.adapter = matchesAdapter
    }
}
