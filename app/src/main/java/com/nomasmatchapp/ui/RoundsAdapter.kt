package com.nomasmatchapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nomasmatchapp.R
import com.nomasmatchapp.fragments.FragmentHome
import com.nomasmatchapp.model.Round

class RoundsAdapter(
    private var rounds: MutableList<Round>,
    private val matchClickListener: FragmentHome
) : RecyclerView.Adapter<RoundsAdapter.RoundViewHolder>() {

    class RoundViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roundName: TextView = view.findViewById(R.id.tv_round_title)
        val roundDate: TextView = view.findViewById(R.id.tv_round_date) // <
        val matchesRecyclerView: RecyclerView = view.findViewById(R.id.recyclerViewMatches)
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
        round.partidos.firstOrNull()?.let { primerPartido ->
            holder.roundDate.text = primerPartido.fecha
        }
        // 1. Asignar el nombre de la jornada (sin cambios)
        holder.matchesRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)

        val matchesAdapter = MatchesAdapter(round.partidos, matchClickListener)
        holder.matchesRecyclerView.adapter = matchesAdapter

    }
}
