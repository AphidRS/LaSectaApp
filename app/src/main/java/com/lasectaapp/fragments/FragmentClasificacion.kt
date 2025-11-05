package com.lasectaapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lasectaapp.URLManager
import com.lasectaapp.databinding.FragmentClasificacionBinding
import com.lasectaapp.ui.ScoreboardAdapter
import com.lasectaapp.ui.ScoreboardViewModel

class FragmentClasificacion : Fragment() {

    private var _binding: FragmentClasificacionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScoreboardViewModel by viewModels()


    private lateinit var scoreboardAdapter: ScoreboardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClasificacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        val classificationUrl = URLManager.getClassificationUrl()
        // ERROR 3: Se llama a la función 'loadClassification' del ViewModel.
        viewModel.loadClassification(classificationUrl)
    }

    private fun setupRecyclerView() {
        // Inicializamos el adapter correcto.
        scoreboardAdapter = ScoreboardAdapter(mutableListOf())
        // Asignamos el adapter al RecyclerView.
        binding.recyclerViewClassification.adapter = scoreboardAdapter
    }

    private fun observeViewModel() {
        // ERROR 4: Ahora 'viewModel.standings' existe y es un LiveData.
        viewModel.standings.observe(viewLifecycleOwner) { standingsList ->
            scoreboardAdapter.updateData(standingsList)
        }

        // ERROR 5: Ahora 'viewModel.isLoading' existe y es un LiveData.
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        // ERROR 6: Ahora 'viewModel.error' existe y es un LiveData.
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpieza para evitar memory leaks
    }
}
