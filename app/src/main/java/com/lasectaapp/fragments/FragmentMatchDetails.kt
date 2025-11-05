package com.lasectaapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.lasectaapp.R
import com.lasectaapp.databinding.MatchReportBinding
import com.lasectaapp.model.Match

class FragmentMatchDetails : Fragment() {

    private var _binding: MatchReportBinding? = null
    private val binding get() = _binding!!
    private val args: FragmentMatchDetailsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MatchReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val match = args.match
        Log.d("MatchDetailsFragment", "Partido recibido: ${match.equipo_local}")
        populateViews(match)
    }

    private fun populateViews(match: Match) {
        binding.apply {

            tvDetailsLocalTeam.text = match.equipo_local
            tvDetailsVisitorTeam.text = match.equipo_visitante

            val baseUrl = "https://appweb.rffm.es"
            ivDetailsLocalShield.load(baseUrl + match.escudo_equipo_local) {
                placeholder(R.drawable.ic_launcher_background) // Cambia esto por tus placeholders
                error(R.drawable.ic_launcher_background)
            }
            ivDetailsVisitorShield.load(baseUrl + match.escudo_equipo_visitante) {
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }

            if (match.goles_casa.isNullOrEmpty() || match.goles_visitante.isNullOrEmpty()) {
                tvDetailsResult.text = "VS"
            } else {
                // Si hay goles, mostramos el resultado final.
                tvDetailsResult.text = "${match.goles_casa} - ${match.goles_visitante}"
            }
            tvDetailsResult.visibility = View.VISIBLE
            llLocalLineup.removeAllViews()
            llVisitorLineup.removeAllViews()
            llGoalsContainer.removeAllViews()
            llRefereesContainer.removeAllViews()

            val localPlayers: List<String>? = listOf("1. Jugador Local A", "2. Jugador Local B")
            val visitorPlayers: List<String>? = null

            if (localPlayers.isNullOrEmpty()) {
                addViewToLayout("Alineación no disponible", llLocalLineup)
            } else {
                localPlayers.forEach { playerName ->
                    addViewToLayout(playerName, llLocalLineup)
                }
            }

            if (visitorPlayers.isNullOrEmpty()) {
                addViewToLayout("Alineación no disponible", llVisitorLineup)
            } else {
                visitorPlayers.forEach { playerName ->
                    addViewToLayout(playerName, llVisitorLineup)
                }
            }
        }
    }

    private fun addViewToLayout(text: String, layout: ViewGroup) {
        val textView = TextView(requireContext()).apply {
            this.text = text
            textSize = 14f
            setPadding(0, 8, 0, 8)
        }
        layout.addView(textView)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
