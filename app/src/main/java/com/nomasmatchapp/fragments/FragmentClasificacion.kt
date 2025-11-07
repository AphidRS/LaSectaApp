package com.nomasmatchapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nomasmatchapp.URLManager
import com.nomasmatchapp.databinding.FragmentClasificacionBinding
import com.nomasmatchapp.ui.ScoreboardAdapter
import com.nomasmatchapp.viewmodels.ScoreboardViewModel

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
        viewModel.loadClassification(classificationUrl)
    }

    private fun setupRecyclerView() {
        scoreboardAdapter = ScoreboardAdapter(mutableListOf())
        binding.recyclerViewClassification.adapter = scoreboardAdapter
    }

    private fun observeViewModel() {
        viewModel.standings.observe(viewLifecycleOwner) { standingsList ->
            scoreboardAdapter.updateData(standingsList)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

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
