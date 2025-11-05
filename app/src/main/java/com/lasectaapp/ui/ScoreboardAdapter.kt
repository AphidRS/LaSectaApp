package com.lasectaapp.ui
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lasectaapp.R
import com.lasectaapp.databinding.ItemClasificationRowBinding
import com.lasectaapp.model.ScoreboardModels

class ScoreboardAdapter(
    private var standings: MutableList<ScoreboardModels>
) : RecyclerView.Adapter<ScoreboardAdapter.StandingViewHolder>() {

    inner class StandingViewHolder(val binding: ItemClasificationRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StandingViewHolder {
        // Usamos ViewBinding para inflar el layout de forma segura
        val binding = ItemClasificationRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StandingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StandingViewHolder, position: Int) {
        // Obtenemos el objeto de datos para la fila actual
        val team = standings[position]

        // Usamos 'with(holder.binding)' para acceder a las vistas de forma más limpia
        with(holder.binding) {
            // Asignamos los datos del objeto 'team' a cada vista del layout
            tvPosition.text = team.position
            tvTeamName.text = team.teamName
            tvPoints.text = team.points
            tvPlayed.text = team.played
            tvWon.text = team.won
            tvDrawn.text = team.drawn
            tvLost.text = team.lost
            tvGoalsFor.text = team.goalsFor
            tvGoalsAgainst.text = team.goalsAgainst
            tvSanction.text = team.sanctionPoints

            val baseImageUrl = "https://appweb.rffm.es"

            ivShield.load(baseImageUrl + team.shieldUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }
        }
    }

    override fun getItemCount(): Int = standings.size

    fun updateData(newStandings: List<ScoreboardModels>) {
        standings.clear() // Vaciamos la lista actual
        standings.addAll(newStandings) // Añadimos los nuevos datos
        notifyDataSetChanged() // Notificamos al RecyclerView que los datos han cambiado y debe redibujarse
    }
}
