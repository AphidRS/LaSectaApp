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
        binding.recyclerViewRounds.layoutManager = LinearLayoutManager(requireContext())
        roundsAdapter = RoundsAdapter(mutableListOf(), this)        // 2. Asigna el adaptador inmediatamente.
        binding.recyclerViewRounds.adapter = roundsAdapter
    }

    private fun observeViewModel() {
        homeViewModel.rounds.observe(viewLifecycleOwner) { rounds ->
            scrollToCurrentRound(rounds)
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
        Log.d("HomeFragment", "Número de jornadas recibidas: ${rounds.size}") // <-- AÑADE ESTO
        if (rounds.isNotEmpty()) {
            roundsAdapter.updateData(rounds)
        } else {
            Toast.makeText(requireContext(), "No se encontraron jornadas", Toast.LENGTH_LONG).show()
        }
    }

    private fun scrollToCurrentRound(rounds: List<Round>) {
        val currentRoundIndex = findCurrentRoundIndex(rounds)
        if (currentRoundIndex != -1) {
            binding.recyclerViewRounds.post {
                (binding.recyclerViewRounds.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(currentRoundIndex, 0)
            }
        }
    }

    private fun findCurrentRoundIndex(rounds: List<Round>): Int {
        val regex = "jornada=(\\d+)".toRegex()
        val matchResult = regex.find(urlToFetch)
        matchResult?.let { match ->
            val roundNumberString = match.groupValues[1]
            val roundNumber = roundNumberString.toIntOrNull() ?: return -1
            val index = rounds.indexOfFirst { it.jornada == roundNumber }

            if (index != -1) {
                return index
            }
        }
        return 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActaClick(match: Match) {
        val action = FragmentHomeDirections.actionFragmentHomeToFragmentMatchDetails(match)
        findNavController().navigate(action)
    }
}
