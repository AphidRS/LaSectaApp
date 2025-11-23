package com.nomasmatchapp.ui

import android.R.color.white
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nomasmatchapp.R
import com.nomasmatchapp.databinding.ItemMatchBinding
import com.nomasmatchapp.model.Match

interface OnMatchClickListener {
    fun onMatchClick(match: Match)
}


class MatchesAdapter(
    private val listener: OnMatchClickListener
) : RecyclerView.Adapter<MatchesAdapter.MatchViewHolder>() {

    private var matches: List<Match> = emptyList()

    inner class MatchViewHolder(val binding: ItemMatchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val binding = ItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchViewHolder(binding)
    }

    override fun getItemCount(): Int = matches.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = matches[position]
        with(holder.binding) {
            teamLocalName.text = match.equipo_local
            teamVisitorName.text = match.equipo_visitante
            tvMatchTime.text = match.hora

            if (match.goles_casa.isEmpty() || match.goles_visitante.isEmpty()) {
                result.visibility = View.GONE
                cvResultContainer.visibility = View.INVISIBLE
                cvResultContainer.setCardBackgroundColor(white)
            } else {
                result.visibility = View.VISIBLE
                result.text = "${match.goles_casa} - ${match.goles_visitante}"
                cvResultContainer.visibility = View.VISIBLE
            }

            holder.itemView.setOnClickListener {
                listener.onMatchClick(match)
            }

            val baseImageUrl = "https://appweb.rffm.es"
            imgEscudoLocal.load(baseImageUrl + match.escudo_equipo_local) {
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }

            imgEscudoVisitante.load(baseImageUrl + match.escudo_equipo_visitante) {
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }
        }
    }

    fun submitList(newMatches: List<Match>) {
        matches = newMatches
        notifyDataSetChanged()
    }
}
