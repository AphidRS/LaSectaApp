package com.lasectaapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lasectaapp.R
import com.lasectaapp.model.Match

class MatchesAdapter (
    private val matches: List<Match>,
    private val listener: OnMatchClickListener
) : RecyclerView.Adapter<MatchesAdapter.MatchViewHolder>() {

    class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val localShield: ImageView = view.findViewById(R.id.iv_local_shield)
        val localTeamName: TextView = view.findViewById(R.id.tv_local_team)
        val result: TextView = view.findViewById(R.id.tv_score)
        val visitorTeamName: TextView = view.findViewById(R.id.tv_visitor_team)
        val visitorShield: ImageView = view.findViewById(R.id.iv_visitor_shield)
        val viewMatchReport: TextView = view.findViewById(R.id.tv_view_match_report)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_match, parent, false)
        return MatchViewHolder(view)
    }

    override fun getItemCount(): Int = matches.size

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = matches[position]

        holder.localTeamName.text = match.equipo_local
        holder.visitorTeamName.text = match.equipo_visitante

        if (match.goles_casa.isNullOrEmpty() || match.goles_visitante.isNullOrEmpty()) {

            holder.result.visibility = View.GONE
            holder.viewMatchReport.visibility = View.GONE
        } else {
            holder.result.visibility = View.VISIBLE
            holder.result.text = "${match.goles_casa} - ${match.goles_visitante}"
            holder.viewMatchReport.visibility = View.GONE
        }

        holder.viewMatchReport.setOnClickListener {
            listener.onActaClick(match)
        }

        val baseImageUrl = "https://appweb.rffm.es"
        holder.localShield.load(baseImageUrl + match.escudo_equipo_local) {
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
        }

        holder.visitorShield.load(baseImageUrl + match.escudo_equipo_visitante) {
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
        }
    }
}


interface OnMatchClickListener {
    fun onActaClick(match: Match)
}
