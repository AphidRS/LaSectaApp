package com.nomasmatchapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import coil.load
import com.nomasmatchapp.R
import com.nomasmatchapp.databinding.MatchReportBinding
import com.nomasmatchapp.model.MatchDetails
import com.nomasmatchapp.model.Player
import com.nomasmatchapp.viewmodel.MatchDetailsViewModel

class FragmentMatchDetails : Fragment() {

    // 1. ViewModel: Gestionará la lógica de negocio y los datos de esta pantalla.
    private val viewModel: MatchDetailsViewModel by viewModels()

    // 2. Safe Args: Recibe los argumentos (la URL) de forma segura desde el FragmentHome.
    private val args: FragmentMatchDetailsArgs by navArgs()

    // 3. View Binding: Para acceder a las vistas del layout de forma segura.
    private var _binding: MatchReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MatchReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 4. Observadores: El Fragment se suscribe a los cambios del ViewModel.
        observeViewModel()

        // 5. Carga de datos: Le pide al ViewModel que empiece a cargar los detalles del partido.
        Log.d("MatchDetails", "Iniciando carga de datos para la URL: ${args.url}")
        viewModel.loadMatchDetails(args.url)
    }

    private fun observeViewModel() {
        // Observador para los detalles del partido. Se activa cuando los datos están listos.
        viewModel.matchDetails.observe(viewLifecycleOwner) { details ->
            details?.let {
                bindMatchDetails(it)
            }
        }

        // Observador para el estado de carga (para mostrar/ocultar un ProgressBar).
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Deberías añadir un ProgressBar a tu layout match_report.xml
            // binding.progressBar.isVisible = isLoading
        }

        // Observador para manejar posibles errores de red o parseo.
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
                Log.e("MatchDetails", "Error recibido del ViewModel: $it")
            }
        }
    }

    /**
     * Rellena la interfaz de usuario con los datos recibidos del ViewModel.
     */
    private fun bindMatchDetails(details: MatchDetails) {
        val baseImageUrl = "https://appweb.rffm.es"

        with(binding) {
            // --- Card 1: Información del Partido ---
            tvMatchHeaderInfo.text = "JORNADA ${details.jornada} - ${details.fecha}"
            tvDetailsResult.text = details.resultado
            tvDetailsLocalTeam.text = details.equipo_local
            tvDetailsVisitorTeam.text = details.equipo_visitante

            ivDetailsLocalShield.load(baseImageUrl + details.escudo_local) {
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }
            ivDetailsVisitorShield.load(baseImageUrl + details.escudo_visitante) {
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }

            // --- Card 2: Detalles del Acta ---

            // Limpia las vistas de alineaciones, goles y árbitros antes de añadir las nuevas.
            llLocalLineup.removeAllViews()
            llVisitorLineup.removeAllViews()
            llGoalsContainer.removeAllViews()
            llRefereesContainer.removeAllViews()

            // Rellenar alineación local
            details.alineacion_local.forEach { player ->
                addPlayerToLayout(llLocalLineup, player)
            }

            // Rellenar alineación visitante
            details.alineacion_visitante.forEach { player ->
                addPlayerToLayout(llVisitorLineup, player)
            }

            // Rellenar goles si existen
            if (details.goles.isNotEmpty()) {
                tvGolesTitle.visibility = View.VISIBLE
                llGoalsContainer.visibility = View.VISIBLE
                details.goles.forEach { goal ->
                    val textView = TextView(context).apply {
                        text = "(${goal.resultado}) ${goal.jugador} - Min ${goal.minuto}"
                        setPadding(0, 4, 0, 4)
                    }
                    llGoalsContainer.addView(textView)
                }
            } else {
                tvGolesTitle.visibility = View.GONE
                llGoalsContainer.visibility = View.GONE
            }

            // Rellenar árbitros
            details.arbitros.forEach { referee ->
                val textView = TextView(context).apply {
                    text = "${referee.rol}: ${referee.nombre}"
                    setPadding(0, 4, 0, 4)
                }
                llRefereesContainer.addView(textView)
            }
        }
    }

    /**
     * Función de ayuda para crear y añadir un TextView de jugador a un LinearLayout.
     */
    private fun addPlayerToLayout(layout: ViewGroup, player: Player) {
        val textView = TextView(context).apply {
            text = "${player.dorsal}. ${player.nombre}"
            setPadding(0, 8, 0, 8)
        }
        layout.addView(textView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Previene fugas de memoria.
    }
}
