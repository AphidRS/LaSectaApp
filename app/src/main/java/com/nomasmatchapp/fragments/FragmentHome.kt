package com.nomasmatchapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nomasmatchapp.URLManager
import com.nomasmatchapp.databinding.FragmentHomeBinding
import com.nomasmatchapp.model.Match
import com.nomasmatchapp.model.Round
import com.nomasmatchapp.ui.OnMatchClickListener
import com.nomasmatchapp.ui.RoundsAdapter
import com.nomasmatchapp.ui.home.HomeViewModel
class FragmentHome : Fragment(), OnMatchClickListener {

    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var roundsAdapter: RoundsAdapter
    private lateinit var urlToFetch: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        urlToFetch = URLManager.getCalendarUrl()
        homeViewModel.loadRounds(urlToFetch)
    }

    private fun setupRecyclerView() {
        // 1. Creamos el adapter pasándole solo el listener ('this' porque el Fragment implementa la interfaz)
        roundsAdapter = RoundsAdapter(this)

        // 2. Configuramos el RecyclerView
        binding.recyclerViewRounds.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = roundsAdapter // 3. Asignamos el adapter
        }
    }

    private fun observeViewModel() {
        homeViewModel.rounds.observe(viewLifecycleOwner) { rounds ->
            handleRounds(rounds)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        homeViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleRounds(rounds: List<Round>) {
        Log.d("HomeFragment", "Número de jornadas recibidas: ${rounds.size}")
        if (rounds.isNotEmpty()) {
            // Le pasamos la lista COMPLETA de jornadas al adapter.
            // El método se llama 'submitList', no 'updateRounds'.
            roundsAdapter.submitList(rounds)
        } else {
            Toast.makeText(requireContext(), "No se encontraron jornadas", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMatchClick(actaId: Match) {
        // 1. Obtenemos el código del acta del partido en el que se ha hecho clic.
        val codigoActa = actaId.codacta

        // 2. Usamos el URLManager para construir la URL del acta.
        //    URLManager se encargará de saber qué opción del Spinner está seleccionada.
        val fullUrl = URLManager.getActaUrl(codigoActa)

        // 3. Navegamos al fragmento de detalles (MatchReportFragment), pasándole la URL completa.
        //    Asegúrate de que el ID del action en tu nav_graph.xml sea correcto.
        val action = FragmentHomeDirections.actionFragmentHomeToFragmentMatchDetails(fullUrl)
        findNavController().navigate(action)

        // Opcional: Muestra un Toast para depuración
        Toast.makeText(requireContext(), "Cargando acta: $codigoActa", Toast.LENGTH_SHORT).show()

    }
}
