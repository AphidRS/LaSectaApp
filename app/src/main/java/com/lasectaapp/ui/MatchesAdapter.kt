package com.lasectaapp.ui.adapter // Paquete corregido

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load // Importamos la función 'load' de Coil
import com.lasectaapp.R
import com.lasectaapp.model.Match

class MatchesAdapter(private val matches: List<Match>) : RecyclerView.Adapter<MatchesAdapter.MatchViewHolder>() {

    // El ViewHolder contiene las referencias a las vistas dentro de item_match.xml
    class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val localShield: ImageView = view.findViewById(R.id.iv_local_shield)
        val localTeamName: TextView = view.findViewById(R.id.tv_local_team)
        val result: TextView = view.findViewById(R.id.tv_result)
        val visitorTeamName: TextView = view.findViewById(R.id.tv_visitor_team)
        val visitorShield: ImageView = view.findViewById(R.id.iv_visitor_shield)
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

        // Lógica para mostrar el resultado
        if (match.goles_casa.isNullOrEmpty() || match.goles_visitante.isNullOrEmpty()) {
            holder.result.text = "VER ACTA" // O podrías usar un string resource
        } else {
            holder.result.text = "${match.goles_casa} - ${match.goles_visitante}"
        }

        // --- Carga de imágenes con Coil ---
        // URL base del servidor de imágenes
        val baseImageUrl = "https://appweb.rffm.es"

        // Cargamos el escudo del equipo local
        holder.localShield.load(baseImageUrl + match.escudo_equipo_local) {
            placeholder(R.drawable.ic_launcher_background) // Imagen de carga
            error(R.drawable.ic_launcher_background)       // Imagen si falla la carga
        }

        // Cargamos el escudo del equipo visitante
        holder.visitorShield.load(baseImageUrl + match.escudo_equipo_visitante) {
            placeholder(R.drawable.ic_launcher_background) // Imagen de carga
            error(R.drawable.ic_launcher_background)       // Imagen si falla la carga
        }
    }
}
