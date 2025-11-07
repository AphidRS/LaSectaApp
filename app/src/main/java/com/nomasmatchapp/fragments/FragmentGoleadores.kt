package com.nomasmatchapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nomasmatchapp.URLManager
import com.nomasmatchapp.databinding.FragmentGoleadoresBinding
import com.nomasmatchapp.ui.ScorersAdapter
import com.nomasmatchapp.viewmodels.ScorersViewModel

class FragmentGoleadores : Fragment() {

    private var _binding: FragmentGoleadoresBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScorersViewModel by viewModels()

    private lateinit var scorersAdapter: ScorersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoleadoresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        val goleadoresUrl = URLManager.getScorersUrl()
        viewModel.loadScorers(goleadoresUrl)
    }

    private fun setupRecyclerView() {
        scorersAdapter = ScorersAdapter(mutableListOf())
        binding.rvGoleadores.adapter = scorersAdapter
    }

    private fun observeViewModel() {
        viewModel.scorers.observe(viewLifecycleOwner) { scorersList ->
            scorersAdapter.updateData(scorersList)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(url: String): FragmentGoleadores {
            val fragment = FragmentGoleadores()
            val args = Bundle()
            args.putString("url_goleadores", url)
            fragment.arguments = args
            return fragment
        }
    }
}
