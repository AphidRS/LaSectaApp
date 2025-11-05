package com.lasectaapp.fragments

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
import com.lasectaapp.URLManager
import com.lasectaapp.databinding.FragmentHomeBinding
import com.lasectaapp.model.Match
import com.lasectaapp.model.Round
import com.lasectaapp.ui.OnMatchClickListener
import com.lasectaapp.ui.RoundsAdapter
import com.lasectaapp.ui.home.HomeViewModel

class FragmentHome : Fragment(), OnMatchClickListener {

    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var roundsAdapter: RoundsAdapter

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
        val urlToFetch = URLManager.getCalendarUrl()
        homeViewModel.loadRounds(urlToFetch)
    }

    private fun setupRecyclerView() {
        // Usamos el RecyclerView del binding y le asignamos un LayoutManager.
        binding.recyclerViewRounds.layoutManager = LinearLayoutManager(requireContext())
        // El adaptador se creará y asignará cuando lleguen los datos.
        // --- CAMBIO CLAVE ---
        // 1. Crea el adaptador con una lista vacía.
        roundsAdapter = RoundsAdapter(mutableListOf(), this)        // 2. Asigna el adaptador inmediatamente.
        binding.recyclerViewRounds.adapter = roundsAdapter
    }

    private fun observeViewModel() {
        // Observador para la lista de jornadas (el resultado exitoso)
        homeViewModel.rounds.observe(viewLifecycleOwner) { rounds ->
            // Cuando la lista de jornadas llega, la procesamos.
            handleRounds(rounds)
        }

        // Observador para el estado de carga (ProgressBar)
        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Hacemos visible o invisible el ProgressBar según el estado.
            binding.progressBar.isVisible = isLoading
        }

        // Observador para los mensajes de error
        homeViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            // Si llega un mensaje de error, lo mostramos en un Toast.
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleRounds(rounds: List<Round>) {
        Log.d("HomeFragment", "Número de jornadas recibidas: ${rounds.size}") // <-- AÑADE ESTO
        if (rounds.isNotEmpty()) {
            // Si la lista no está vacía:
            // 1. Creamos el adaptador con la lista de jornadas.
            roundsAdapter.updateData(rounds)
        } else {
            // Opcional: Manejar el caso de que la lista llegue vacía.
            // podrías mostrar un mensaje de "No hay jornadas disponibles".
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpiamos la referencia al binding para evitar memory leaks.
        _binding = null
    }

    override fun onActaClick(match: Match) {
        // SafeArgs genera este método a partir del ID de la acción que acabas de corregir
        val action = FragmentHomeDirections.actionFragmentHomeToFragmentMatchDetails(match)
        findNavController().navigate(action)
    }
}
